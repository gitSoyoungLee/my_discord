package com.spirnt.mission.discodeit.dto.message;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class MessageCreateRequest extends MessageBase{
    private UUID userId;
    private UUID channelId;
    private List<String> files;

    public MessageCreateRequest(UUID userId, UUID channelId,
                                String content, List<String> files) {
        super(content);
        this.userId = userId;
        this.channelId = channelId;
        if(files!=null) {
            this.files = files;
        } else {
            this.files = new ArrayList<>();
        }
    }
}
