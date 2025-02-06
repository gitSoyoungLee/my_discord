package com.spirnt.mission.discodeit.service.basic;

import com.spirnt.mission.discodeit.dto.message.MessageResponse;
import com.spirnt.mission.discodeit.enity.Channel;
import com.spirnt.mission.discodeit.enity.Message;
import com.spirnt.mission.discodeit.enity.User;
import com.spirnt.mission.discodeit.repository.MessageRepository;
import com.spirnt.mission.discodeit.service.ChannelService;
import com.spirnt.mission.discodeit.service.MessageService;
import com.spirnt.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.*;
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private UserService userService;
    private ChannelService channelService;

    @Override
    public void setService(UserService userService, ChannelService channelService) {
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public UUID createMessage(UUID userId, UUID channelId, String content) {
        // 존재하는지 검증
        Channel channel = channelService.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel ID: " + channelId + " Not Found"));
        User user = userService.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User ID: " + userId + " Not Found"));

        if (!channel.getUsersId().contains(userId)) {
            System.out.println("메세지를 보낼 수 없습니다: " +
                    user.getName() + "은 아직 '" + channel.getName() + "' 채널에 입장하지 않았습니다.");
            return null;
        }
        Message message = new Message(userId, channelId, content);
        messageRepository.save(message);
        System.out.println("메세지 전송 완료: " + message.getContent());
        return message.getId();
    }

    @Override
    public MessageResponse getMessageById(UUID messageId) {
        Message message = findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message ID: " + messageId + " Not Found"));
        return new MessageResponse(message, null);
    }

    @Override
    public List<MessageResponse> getAllMessages() {
        Map<UUID, Message> data = messageRepository.findAll();
        if (data == null || data.isEmpty()) {
            System.out.println("메세지가 없습니다.");
            return null;
        }
        List<MessageResponse> list = new ArrayList<>();
        data.values().stream()
                .sorted(Comparator.comparing(message -> message.getCreatedAt()))
                .forEach(message -> {
                    list.add(new MessageResponse(message, null));
                });
        return list;
    }


    @Override
    public void updateMessage(UUID userId, UUID messageId, String newContent) {
        Message message = findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message ID: " + messageId + " Not Found"));
        // 메시지 작성자인 경우에만 수정 가능
        if (!message.getSenderId().equals(userId)) {
            System.out.println("메세지 작성자만 수정 가능합니다.");
            return;
        }
        message.updateContent(newContent);
        // 객체 수정 후 저장
        messageRepository.save(message);
    }

    @Override
    public void deleteMessage(UUID messageId) {
        Message message = findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message ID: " + messageId + " Not Found"));
        messageRepository.delete(messageId);
    }

    @Override
    public Optional<Message> findById(UUID messageId) {
        return messageRepository.findById(messageId);
    }


    // 해당 채널에 쓰여진 메세지들을 모두 삭제
    @Override
    public void deleteMessagesInChannel(UUID channelId) {
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
}
