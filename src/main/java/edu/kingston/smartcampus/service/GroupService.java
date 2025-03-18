package edu.kingston.smartcampus.service;

import edu.kingston.smartcampus.dto.*;
import edu.kingston.smartcampus.model.*;
import edu.kingston.smartcampus.model.user.User;
import edu.kingston.smartcampus.repository.*;
import io.jsonwebtoken.io.IOException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class GroupService {

    private final GroupRepository groupRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final FileRepository fileRepository;
    private final CourseRepository courseRepository;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;
    private final FileStorageService fileStorageService;


    @Transactional
    public GroupDto createGroup(GroupCreateDto dto, Long creatorId) {
        // Verify role from User object
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        log.info("Creator: {}", creator);
        log.info("Creator email: {}", creator.getEmail());
        String roleName = creator.getRole().getRoleName().name();
        log.info("Role name: {}", roleName);
        if (!"ROLE_ADMIN".equals(roleName) && !"ROLE_LECTURER".equals(roleName)) {
            throw new AccessDeniedException("Only admins and lecturers can create groups");
        }

        // Fetch the associated course
        Course course = courseRepository.findByCourseId(dto.getCourseId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found: " + dto.getCourseId()));

        // Create the group entity
        Group group = new Group();
        group.setGroupName(dto.getGroupName());
        group.setDescription(dto.getDescription());
        group.setCourse(course);
        group.setCreator(creator);
        group.setCreationDate(LocalDateTime.now());

        // Add initial members if provided
        if (dto.getInitialMemberIds() != null && !dto.getInitialMemberIds().isEmpty()) {
            List<User> initialMembers = userRepository.findAllById(dto.getInitialMemberIds());
            group.setMembers(initialMembers);
        }

        // Save to database
        Group savedGroup = groupRepository.save(group);
        return respondGroup(savedGroup);
    }

    private GroupDto respondGroup(Group group) {
        GroupDto dto = new GroupDto();
        dto.setGroupId(group.getGroupId());
        dto.setGroupName(group.getGroupName());
        dto.setDescription(group.getDescription());
        dto.setCreationDate(group.getCreationDate());
        return dto;
    }

    public GroupDto addGroupMember(Long groupId, Long userId) {
        return null;
    }

    public MessageDto sendGroupMessage(MessageCreateDto dto) {
        User sender = userRepository.findById(dto.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        Group group = groupRepository.findById(dto.getGroupId())
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        Message message = new Message();
        message.setSender(sender);
        message.setGroup(group);
        message.setContent(dto.getContent());
//        message.setSentTime(LocalDateTime.now()); TODO: Error - LocalDateTime.now() is not serializable
//!        com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Java 8 date/time type `java.time.LocalDateTime` not supported by default
        Message savedMessage = messageRepository.save(message);

        MessageDto messageDto = mapToMessageDto(savedMessage);
        messagingTemplate.convertAndSend("/topic/groups/" + dto.getGroupId() + "/messages", messageDto);

        group.getMembers().forEach(member -> {
            if (!member.getId().equals(sender.getId())) {
                notificationService.sendNotification(member.getId(),
                        "New message in " + group.getGroupName() + ": " + dto.getContent(), "MESSAGE");
            }
        });
        messageDto.setSenderName(sender.getFirstName() + " " + sender.getLastName());
        return messageDto;
    }

    public TaskDto createGroupTask(Long groupId, TaskCreateDto dto) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        Task task = new Task();
        task.setGroup(group);
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDueDate(dto.getDueDate());

        if (dto.getAssignedToId() != null) {
            User assignedTo = userRepository.findById(dto.getAssignedToId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            task.setAssignedToUsers(Collections.singletonList(assignedTo)); // Single user
            notificationService.sendNotification(assignedTo.getId(),
                    "New task in " + group.getGroupName() + ": " + dto.getTitle(), "TASK");
        } else {
            List<User> usersAssignedTo = new ArrayList<>(group.getMembers());
            task.setAssignedToUsers(usersAssignedTo); // All group members
            group.getMembers().forEach(member ->
                    notificationService.sendNotification(member.getId(),
                            "New task in " + group.getGroupName() + ": " + dto.getTitle(), "TASK"));
        }

        Task savedTask = taskRepository.save(task);
        return mapToTaskDto(savedTask);
    }

    public FileDto uploadGroupFile(Long groupId, MultipartFile file, Long uploaderId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));
        String filePath = fileStorageService.storeFile(file);
        File fileEntity = new File();
        fileEntity.setGroup(group);
        fileEntity.setFileName(file.getOriginalFilename());
        fileEntity.setFilePath(filePath);
        fileEntity.setUploadTime(LocalDateTime.now());

        // Set uploader: Use provided uploaderId or infer from SecurityContext
        if (uploaderId != null) {
            User uploader = userRepository.findById(uploaderId)
                    .orElseThrow(() -> new RuntimeException("Uploader not found"));
            fileEntity.setUploader(uploader);
        } else {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            User uploader = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
            fileEntity.setUploader(uploader);
        }
        File savedFile = fileRepository.save(fileEntity);
        group.getMembers().forEach(member ->
                notificationService.sendNotification(member.getId(),
                        "New file in " + group.getGroupName() + ": " + file.getOriginalFilename(), "FILE"));
        return mapToFileDto(savedFile);
    }

    public List<FileDto> getFilesByGroupId(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));
        return group.getFiles().stream().map(this::mapToFileDto).toList();
    }

    public ResponseEntity<Resource> downloadFile(Long fileId) {
        // Find the file entity by ID
        File fileEntity = fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));

        try {
            // Construct the file path from the stored filePath
            Path filePath = Paths.get(fileEntity.getFilePath()).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            // Check if the file exists and is readable
            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            // Set headers for file download
            String contentType = "application/octet-stream"; // Default MIME type
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileEntity.getFileName() + "\"")
                    .body(resource);
        } catch (IOException | MalformedURLException e) {
            return ResponseEntity.status(500).build();
        }
    }

    @Transactional
    public GroupDto getGroupById(Long id) {
        return groupRepository.findById(id)
                .map(this::mapToGroupDto)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));
    }

    // Placeholder mapping methods to convert entities to DTOs
    private GroupDto mapToGroupDto(Group group) {
        GroupDto groupDto = new GroupDto();
        groupDto.setGroupId(group.getGroupId());
        groupDto.setGroupName(group.getGroupName());
        groupDto.setCreatorId(group.getCreator().getId());
        groupDto.setDescription(group.getDescription());
        groupDto.setCreationDate(group.getCreationDate());
        List<UserDto> members = group.getMembers().stream().map(this::mapToUserDto).toList();
        groupDto.setMembers(members);
        groupDto.setMessages(group.getMessages().stream().map(this::mapToMessageDto).toList());
        groupDto.setTasks(group.getTasks().stream().map(this::mapToTaskDto).toList());
        groupDto.setFiles(group.getFiles().stream().map(this::mapToFileDto).toList());
        return groupDto;
    }

    private UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setPhone(user.getPhone());
        userDto.setAddress(user.getAddress());
        userDto.setProfileImage(user.getProfileImage());
        userDto.setRoleName(user.getRole().getRoleName().name());
        return userDto;
    }

    private MessageDto mapToMessageDto(Message message) {
        MessageDto messageDto = new MessageDto();
        messageDto.setMessageId(message.getMessageId());
        messageDto.setSenderId(message.getSender().getId());
        messageDto.setGroupId(message.getGroup().getGroupId());
        messageDto.setContent(message.getContent());
        messageDto.setSentTime(message.getSentTime());
        return messageDto;
    }

    private TaskDto mapToTaskDto(Task task) {
        TaskDto taskDto = new TaskDto();
        taskDto.setTaskId(task.getTaskId());
        taskDto.setGroupId(task.getGroup().getGroupId());
        taskDto.setAssignedToId(task.getAssignedToUsers().stream().map(User::getId).toList());
        taskDto.setTitle(task.getTitle());
        taskDto.setDescription(task.getDescription());
        taskDto.setDueDate(task.getDueDate().toString());
        taskDto.setStatus(task.getStatus());
        return taskDto;
    }

    private FileDto mapToFileDto(File file) {
        FileDto fileDto = new FileDto();
        fileDto.setFileId(file.getFileId());
        fileDto.setGroupId(file.getGroup().getGroupId());
        fileDto.setFileName(file.getFileName());
        fileDto.setFilePath(file.getFilePath());
        fileDto.setUploadTime(file.getUploadTime());
        fileDto.setUploaderId(file.getUploader().getId());
        return fileDto;
    }

    @Transactional
    public List<GroupDto> getAllGroups() {
        List<Group> groups = groupRepository.findAll();
        return groups.stream().map(this::mapToGroupDto).toList();
    }

    @Transactional
    public void deleteTask(Long taskId) {
        log.info("Deleting task with ID: {}", taskId);
        taskRepository.deleteById(taskId);
    }

    @Transactional
    public ResponseEntity<List<UserDto>> getGroupMembers(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        List<UserDto> members = group.getMembers().stream().map(this::mapToUserDto).toList();
        return ResponseEntity.ok(members);
    }

    @Transactional
    public ResponseEntity<List<TaskDto>> getGroupTasks(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));
        List<TaskDto> tasks = group.getTasks().stream().map(this::mapToTaskDto).toList();
        return ResponseEntity.ok(tasks);
    }
}
