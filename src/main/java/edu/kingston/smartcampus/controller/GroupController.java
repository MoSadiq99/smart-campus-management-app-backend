package edu.kingston.smartcampus.controller;

import edu.kingston.smartcampus.dto.*;
import edu.kingston.smartcampus.service.GroupService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping("/groups")
    public ResponseEntity<GroupDto> createGroup(
            @Valid @RequestBody GroupCreateDto dto
    ) {
        GroupDto groupDto = groupService.createGroup(dto);
        return ResponseEntity.ok(groupDto);
    }

    @GetMapping("/groups")
    public ResponseEntity<List<GroupDto>> getAllGroups() {
        List<GroupDto> groups = groupService.getAllGroups();
        return ResponseEntity.ok(groups);
    }

    @PostMapping("/groups/{id}/messages")
    public ResponseEntity<MessageDto> sendGroupMessage(
            @PathVariable Long id,
            @Valid @RequestBody MessageCreateDto dto
    ) {
        dto.setGroupId(id);
        MessageDto messageDto = groupService.sendGroupMessage(dto);
        return ResponseEntity.ok(messageDto);
    }
}