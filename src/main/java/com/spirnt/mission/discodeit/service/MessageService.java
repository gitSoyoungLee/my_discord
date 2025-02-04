package com.spirnt.mission.discodeit.service;

import com.spirnt.mission.discodeit.dto.MessageDto;
import com.spirnt.mission.discodeit.enity.Message;
import com.spirnt.mission.discodeit.enity.User;
import com.spirnt.mission.discodeit.repository.MessageRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {
    void setService(UserService userService, ChannelService channelService);
    // Create
    UUID createMessage(UUID userId, UUID channelId, String content); // 메세지 전송

    // Read
    MessageDto getMessageById(UUID messageId);  // 메세지 단건 조회

    List<MessageDto> getAllMessages(); // 모든 메세지 조회

    // Update
    void updateMessage(UUID userId, UUID messageId, String newContent);    // 메시지 수정

    // Delete
    void deleteMessage(UUID messageId);    // 메세지 삭제

    Optional<Message> findById(UUID messageId);

    void deleteMessagesInChannel(UUID channelId);   // 채널 삭제 시 해당 채널에 속한 메세지를 모두 삭제
}
