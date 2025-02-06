package com.spirnt.mission.discodeit.dto.message;

import com.spirnt.mission.discodeit.enity.BinaryContent;
import com.spirnt.mission.discodeit.enity.Message;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
@Getter
public class MessageResponse extends MessageBase {
    private UUID userId;
    private UUID channelId;
    private Instant createdAt;
    private Instant updatedAt;
    private List<UUID> binaryContents;
    public MessageResponse(Message message, List<UUID> binaryContents) {
        super(message.getContent());
        this.userId = message.getSenderId();
        this.channelId = message.getChannelId();
        this.createdAt = message.getCreatedAt();
        this.updatedAt = message.getUpdatedAt();
        this.binaryContents = binaryContents;
    }
}