package com.spirnt.mission.discodeit.service;

import com.spirnt.mission.discodeit.dto.message.MessageCreateRequest;
import com.spirnt.mission.discodeit.dto.message.MessageResponse;
import com.spirnt.mission.discodeit.dto.message.MessageUpdateRequest;
import com.spirnt.mission.discodeit.enity.Message;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {
    // Create
    Message create(MessageCreateRequest messageCreateRequest); // 메세지 전송

    // Read
    Message find(UUID messageId);  // 메세지 단건 조회

    List<Message> findAll(); // 모든 메세지 조회

    // Update
    void update(UUID messageId, MessageUpdateRequest messageUpdateRequest);    // 메시지 수정

    // Delete
    void delete(UUID messageId);    // 메세지 삭제


    void deleteByChannelId(UUID channelId);   // 채널 삭제 시 해당 채널에 속한 메세지를 모두 삭제

    Optional<Instant> findLastMessageInChannel(UUID channelId);   // 해당 채널의 가장 최근 메세지 시간 정보
    boolean existsById(UUID id);
}
