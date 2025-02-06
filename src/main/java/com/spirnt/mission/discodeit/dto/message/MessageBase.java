package com.spirnt.mission.discodeit.dto.message;

import lombok.Getter;

@Getter
public abstract class MessageBase {
    private String content;
    public MessageBase(String content) {
        this.content =content;
    }
}
