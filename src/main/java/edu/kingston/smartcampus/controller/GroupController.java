package edu.kingston.smartcampus.controller;

import edu.kingston.smartcampus.dto.*;
import edu.kingston.smartcampus.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api")
public class GroupController {

    private final GroupService groupService;

    //* Post Mappings

    // Post Mapping: Create Group
    @PostMapping("/groups")
    public ResponseEntity<GroupDto> createGroup(@RequestBody GroupCreateDto dto) {
        GroupDto createdGroup = groupService.createGroup(dto, dto.getCreatorId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGroup);
    }

    // Post Mapping: Add Group Member
    @PostMapping("/groups/{id}/members")
    public ResponseEntity<GroupDto> addGroupMember(@PathVariable Long id, @RequestBody Long userId) {
        GroupDto groupDto = groupService.addGroupMember(id, userId);
        return ResponseEntity.ok(groupDto);
    }

    // Post Mapping: Send Group Message
    @PostMapping("/groups/{id}/messages")
    public ResponseEntity<MessageDto> sendGroupMessage(@PathVariable Long id,
                                                       @Valid @RequestBody MessageCreateDto dto) {
        dto.setGroupId(id);
        MessageDto messageDto = groupService.sendGroupMessage(dto);
        return ResponseEntity.ok(messageDto);
    }

    @PostMapping("/groups/{id}/tasks")
    public ResponseEntity<TaskDto> createGroupTask(@PathVariable Long id,
                                                   @Valid @RequestBody TaskCreateDto dto) {
        log.info("Task: {}", dto);
        TaskDto taskDto = groupService.createGroupTask(id, dto);
        return ResponseEntity.ok(taskDto);
    }

    @PostMapping("/groups/{id}/files")
    public ResponseEntity<FileDto> uploadFile(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "uploaderId", required = false) Long uploaderId) {
        FileDto fileDto = groupService.uploadGroupFile(id, file, uploaderId);
        return ResponseEntity.ok(fileDto);
    }

    //* Get Mappings

    // Get Mapping: Get All Groups
    @GetMapping("/groups")
    public ResponseEntity<List<GroupDto>> getAllGroups() {
        List<GroupDto> groups = groupService.getAllGroups();
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/groups/{id}")
    public ResponseEntity<GroupDto> getGroupById(@PathVariable Long id) {
        GroupDto groupDto = groupService.getGroupById(id);
        return ResponseEntity.ok(groupDto);
    }

    @GetMapping("/groups/{groupId}/files/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId, @PathVariable Long groupId) {
        return groupService.downloadFile(fileId);
    }

    @GetMapping("/groups/{groupId}/files")
    public ResponseEntity<List<FileDto>> getGroupFiles(@PathVariable Long groupId) {
        List<FileDto> files = groupService.getFilesByGroupId(groupId);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/groups/{groupId}/members")
    public ResponseEntity<List<UserDto>> getGroupMembers(@PathVariable Long groupId) {
        return groupService.getGroupMembers(groupId);
    }

    @GetMapping("/groups/{groupId}/tasks")
    public ResponseEntity<List<TaskDto>> getGroupTasks(@PathVariable Long groupId) {
        return groupService.getGroupTasks(groupId);
    }

    //* Delete Mappings

    // Delete Mapping: Delete Task
    @DeleteMapping("/groups/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        groupService.deleteTask(taskId);
        return ResponseEntity.ok().build();
    }
}


