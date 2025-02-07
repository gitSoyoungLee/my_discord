package com.spirnt.mission.discodeit.service.basic;

import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentCreate;
import com.spirnt.mission.discodeit.dto.channel.ChannelResponse;
import com.spirnt.mission.discodeit.dto.message.MessageCreateRequest;
import com.spirnt.mission.discodeit.dto.message.MessageResponse;
import com.spirnt.mission.discodeit.dto.message.MessageUpdateRequest;
import com.spirnt.mission.discodeit.dto.user.UserResponse;
import com.spirnt.mission.discodeit.enity.ChannelType;
import com.spirnt.mission.discodeit.enity.Message;
import com.spirnt.mission.discodeit.repository.MessageRepository;
import com.spirnt.mission.discodeit.service.BinaryContentService;
import com.spirnt.mission.discodeit.service.ChannelService;
import com.spirnt.mission.discodeit.service.MessageService;
import com.spirnt.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.*;

@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private UserService userService;
    private ChannelService channelService;
    private BinaryContentService binaryContentService;

    @Override
    public void setService(UserService userService, ChannelService channelService, BinaryContentService binaryContentService) {
        this.userService = userService;
        this.channelService = channelService;
        this.binaryContentService = binaryContentService;
    }

    @Override
    public Message create(MessageCreateRequest dto) {
        ChannelResponse channel;
        UserResponse user;
        // 존재하는 User, Channel인지 검증
        try {
            channel = channelService.find(dto.getChannelId());
            user = userService.find(dto.getUserId());
        } catch (NoSuchElementException e) {
            throw e;
        }
        // Private 채널은 입장한 경우에만 가능
        if (channel.getType() == ChannelType.PRIVATE &&
                !channel.getUsersId().contains(user.getId())) {
            throw new IllegalStateException("User has not joined this private channel.");
        }
        Message message = new Message(dto);
        messageRepository.save(message);
        // 첨부 파일 업로드
        for (MultipartFile file : dto.getFiles()) {
            BinaryContentCreate binaryContentCreate = new BinaryContentCreate(dto.getUserId(), message.getId(), file);
            binaryContentService.create(binaryContentCreate);
        }
        return message;
    }

    @Override
    public MessageResponse find(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message ID: " + messageId + " Not Found"));
        return new MessageResponse(message);
    }

    @Override
    public List<MessageResponse> findAll() {
        Map<UUID, Message> data = messageRepository.findAll();
        if (data == null || data.isEmpty()) {
            System.out.println("메세지가 없습니다.");
            return new ArrayList<>();
        }
        List<MessageResponse> list = new ArrayList<>();
        data.values().stream()
                .sorted(Comparator.comparing(message -> message.getCreatedAt()))
                .forEach(message -> {
                    list.add(new MessageResponse(message));
                });
        return list;
    }


    @Override
    public void update(UUID messageId, MessageUpdateRequest dto) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message ID: " + messageId + " Not Found"));
        message.update(dto);
        // 객체 수정 후 저장
        messageRepository.save(message);
    }

    @Override
    public void delete(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message ID: " + messageId + " Not Found"));
        messageRepository.delete(messageId);
    }

    // 해당 채널에 쓰여진 메세지들을 모두 삭제
    @Override
    public void deleteByChannelId(UUID channelId) {
        Map<UUID, Message> data = messageRepository.findAll();
        if (data == null) return;
        data.values().stream()
                .forEach(message -> {
                    // 해당 채널에 쓰여진 메세지 객체를 찾고
                    if (message.getChannelId().equals(channelId)) {
                        // 그 객체를 레포지토리에서 삭제
                        messageRepository.delete(message.getId());
                    }
                });
    }

    // 해당 채널 메세지를 정렬하여 가장 최근 메세지 시간 찾기
    @Override
    public Optional<Instant> findLastMessageInChannel(UUID channelId) {
        List<Message> messages = messageRepository.findAll().values().stream().toList();
        Optional<Instant> lastSeenAt = messages.stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
                .map(Message::getCreatedAt)
                .findFirst();
        return lastSeenAt;
    }
}
