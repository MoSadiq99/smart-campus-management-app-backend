package edu.kingston.smartcampus.service;

import edu.kingston.smartcampus.dto.*;
import edu.kingston.smartcampus.model.*;
import edu.kingston.smartcampus.model.user.User;
import edu.kingston.smartcampus.repository.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public GroupService(GroupRepository groupRepository, MessageRepository messageRepository, UserRepository userRepository, SimpMessagingTemplate messagingTemplate) {
        this.groupRepository = groupRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public GroupDto createGroup(GroupCreateDto dto) {
        User creator = userRepository.findById(dto.getCreatorId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Group group = new Group();
        group.setGroupName(dto.getGroupName());
        group.setCreator(creator);
        group.setDescription(dto.getDescription());
        group.setCreationDate(LocalDateTime.now());
        group.setMembers(List.of(creator));
        Group savedGroup = groupRepository.save(group);
        return mapToGroupDto(savedGroup);
    }

    public List<GroupDto> getAllGroups() {
        return groupRepository.findAll().stream()
                .map(this::mapToGroupDto)
                .collect(Collectors.toList());
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
        message.setSentTime(LocalDateTime.now());
        Message savedMessage = messageRepository.save(message);

        MessageDto messageDto = mapToMessageDto(savedMessage);
        // Broadcast to group topic
        messagingTemplate.convertAndSend("/topic/groups/" + dto.getGroupId() + "/messages", messageDto);

        return messageDto;
    }

    private GroupDto mapToGroupDto(Group group) {
        GroupDto dto = new GroupDto();
        dto.setGroupId(group.getGroupId());
        dto.setGroupName(group.getGroupName());
        dto.setCreatorId(group.getCreator().getId());
        dto.setDescription(group.getDescription());
        dto.setCreationDate(group.getCreationDate());
        dto.setMemberIds(group.getMembers().stream().map(User::getId).collect(Collectors.toList()));
        dto.setMessages(messageRepository.findByGroupGroupId(group.getGroupId()).stream()
                .map(this::mapToMessageDto).collect(Collectors.toList()));
        return dto;
    }

    private MessageDto mapToMessageDto(Message message) {
        MessageDto dto = new MessageDto();
        dto.setMessageId(message.getMessageId());
        dto.setSenderId(message.getSender().getId());
        dto.setGroupId(message.getGroup().getGroupId());
        dto.setContent(message.getContent());
        dto.setSentTime(message.getSentTime());
        return dto;
    }
}
