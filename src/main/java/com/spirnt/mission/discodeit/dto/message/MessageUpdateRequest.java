package com.spirnt.mission.discodeit.dto.message;

import lombok.Getter;

@Getter
public class MessageUpdateRequest extends MessageBase {
    //MessageBase와 같은 필드만 가지고 있지만 후에 확장될 가능성을 고려하여 분리
    public MessageUpdateRequest(String content) {
        super(content);
    }
}