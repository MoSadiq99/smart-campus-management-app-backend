package edu.kingston.smartcampus.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.multipart.MultipartFile;

import edu.kingston.smartcampus.dto.FileDto;
import edu.kingston.smartcampus.dto.GroupCreateDto;
import edu.kingston.smartcampus.dto.GroupDto;
import edu.kingston.smartcampus.dto.MessageCreateDto;
import edu.kingston.smartcampus.dto.MessageDto;
import edu.kingston.smartcampus.dto.TaskCreateDto;
import edu.kingston.smartcampus.dto.TaskDto;
import edu.kingston.smartcampus.model.Course;
import edu.kingston.smartcampus.model.File;
import edu.kingston.smartcampus.model.Group;
import edu.kingston.smartcampus.model.Message;
import edu.kingston.smartcampus.model.Task;
import edu.kingston.smartcampus.model.enums.RoleName;
import edu.kingston.smartcampus.model.user.Admin;
import edu.kingston.smartcampus.model.user.Lecturer;
import edu.kingston.smartcampus.model.user.Role;
import edu.kingston.smartcampus.repository.CourseRepository;
import edu.kingston.smartcampus.repository.FileRepository;
import edu.kingston.smartcampus.repository.GroupRepository;
import edu.kingston.smartcampus.repository.MessageRepository;
import edu.kingston.smartcampus.repository.TaskRepository;
import edu.kingston.smartcampus.repository.UserRepository;

public class GroupServiceTest {
    @Mock
    private GroupRepository groupRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private FileRepository fileRepository;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private GroupService groupService;

    private GroupCreateDto groupCreateDto;
    private Admin admin;
    private Course course;
    private Group group;
    private Lecturer lecturer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        admin = new Admin();
        admin.setId(1L);
        admin.setRole(new Role(RoleName.ROLE_ADMIN));
        admin.setEmail("admin@test.com");

        lecturer = new Lecturer();
        lecturer.setId(2L);
        lecturer.setEmail("lecturer@test.com");

        course = new Course();
        course.setCourseId(1L);
        course.setCourseName("Test Course");

        group = new Group();
        group.setGroupId(1L);
        group.setGroupName("Test Group");
        group.setCreator(admin);
        group.setCourse(course);
        group.setMembers(Arrays.asList(admin, lecturer));
    }

    @Test
    void testCreateGroup() {
        groupCreateDto = new GroupCreateDto();
        groupCreateDto.setGroupName("Test Group");
        groupCreateDto.setCourseId(1L);
        groupCreateDto.setInitialMemberIds(Arrays.asList(2L));

        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(courseRepository.findByCourseId(1L)).thenReturn(Optional.of(course));
        when(userRepository.findAllById(anyList())).thenReturn(Arrays.asList(lecturer));
        when(groupRepository.save(any(Group.class))).thenReturn(group);

        GroupDto result = groupService.createGroup(groupCreateDto, 1L);

        assertNotNull(result);
        assertEquals("Test Group", result.getGroupName());
        verify(groupRepository, times(1)).save(any(Group.class));
    }

    @Test
    void testAddGroupMember() {
        Long groupId = 1L;
        Long userId = 2L;

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(userRepository.findById(userId)).thenReturn(Optional.of(lecturer));

        GroupDto result = groupService.addGroupMember(groupId, userId);

        assertTrue(group.getMembers().contains(lecturer));

        assertNotNull(result);
        assertEquals(groupId, result.getGroupId());
        assertTrue(result.getMemberIds().contains(userId));

        verify(groupRepository, times(1)).save(any(Group.class));
    }

    @Test
    void testSendGroupMessage() {
        MessageCreateDto messageCreateDto = new MessageCreateDto();
        messageCreateDto.setSenderId(1L);
        messageCreateDto.setGroupId(1L);
        messageCreateDto.setContent("Test message");

        Message savedMessage = new Message();
        savedMessage.setSender(admin);
        savedMessage.setGroup(group);
        savedMessage.setContent("Test message");
        savedMessage.setSentTime(LocalDateTime.now());

        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(messageRepository.save(any(Message.class))).thenReturn(savedMessage);

        MessageDto result = groupService.sendGroupMessage(messageCreateDto);

        assertNotNull(result);
        assertEquals("Test message", result.getContent());
        assertEquals(admin.getId(), result.getSenderId());

        verify(messageRepository, times(1)).save(any(Message.class));
        verify(messagingTemplate, times(1)).convertAndSend(anyString(), any(MessageDto.class));
    }

    @Test
    void testCreateGroupTask() {
        TaskCreateDto taskCreateDto = new TaskCreateDto();
        taskCreateDto.setTitle("Test Task");
        taskCreateDto.setDescription("Test Description");
        taskCreateDto.setDueDate(LocalDateTime.now().plusDays(7));
        taskCreateDto.setAssignedToId(2L);

        Task savedTask = new Task();
        savedTask.setTitle("Test Task");
        savedTask.setDescription("Test Description");
        savedTask.setDueDate(taskCreateDto.getDueDate());
        savedTask.setGroup(group);
        savedTask.setAssignedToUsers(Arrays.asList(lecturer));

        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(userRepository.findById(2L)).thenReturn(Optional.of(lecturer));
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        TaskDto result = groupService.createGroupTask(1L, taskCreateDto);

        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
        assertEquals(group.getGroupId(), result.getGroupId()); // Ensure group ID is correctly mapped

        verify(taskRepository, times(1)).save(any(Task.class));
        verify(notificationService, times(1)).sendNotification(anyLong(), anyString(), anyString());
    }

    @Test
    void testUploadGroupFile() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("testFile.txt");
        when(fileStorageService.storeFile(file)).thenReturn("filePath");
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(userRepository.findById(1L)).thenReturn(Optional.of(admin)); // Fix: Mock uploader

        File savedFile = new File();
        savedFile.setFileName("testFile.txt");
        savedFile.setFilePath("filePath");
        savedFile.setGroup(group);
        savedFile.setUploader(admin); // Ensure uploader is set

        when(fileRepository.save(any(File.class))).thenReturn(savedFile);

        FileDto result = groupService.uploadGroupFile(1L, file, 1L);

        assertNotNull(result);
        assertEquals("testFile.txt", result.getFileName());
        verify(fileRepository, times(1)).save(any(File.class));
        verify(notificationService, atLeastOnce()).sendNotification(anyLong(), anyString(), anyString());
    }

    @Test
    void testDownloadFile() throws IOException {
        Long fileId = 1L;
        File file = new File();
        file.setFilePath("uploads/testFile.txt"); // Ensure a valid path

        Path filePath = Paths.get("uploads/testFile.txt");
        Files.createDirectories(filePath.getParent()); // Ensure directory exists
        Files.write(filePath, "Test content".getBytes()); // Create a dummy file

        when(fileRepository.findById(fileId)).thenReturn(Optional.of(file));

        ResponseEntity<Resource> response = groupService.downloadFile(fileId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // Clean up after test
        Files.deleteIfExists(filePath);
    }
}
