package com.spirnt.mission.discodeit.service.basic;

import com.spirnt.mission.discodeit.dto.message.MessageCreateRequest;
import com.spirnt.mission.discodeit.dto.message.MessageUpdateRequest;
import com.spirnt.mission.discodeit.enity.Message;
import com.spirnt.mission.discodeit.repository.MessageRepository;
import com.spirnt.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;


    @Override
    public Message create(MessageCreateRequest dto) {
        Message message = new Message(dto);
        messageRepository.save(message);
        return message;
    }

    @Override
    public Message find(UUID messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message ID: " + messageId + " Not Found"));
    }

    @Override
    public List<Message> findAll() {
        Map<UUID, Message> data = messageRepository.findAll();
        return data.values().stream()
                .sorted(Comparator.comparing(message -> message.getCreatedAt()))
                .collect(Collectors.toList());
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
        if (existsById(messageId)) {
            messageRepository.delete(messageId);
        } else {
            throw new NoSuchElementException("Message ID: " + messageId + "Not Found");
        }

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

    @Override
    public boolean existsById(UUID id) {
        return messageRepository.existsById(id);
    }
}
