package com.spirnt.mission.discodeit.dto.channel;

import lombok.Getter;

@Getter
public class ChannelUpdateRequest extends ChannelBase {
    // 현재는 ChannelBase와 필드가 같지만 나중에 수정될 가능성을 고려하여 UpdateDTO를 따로 선언함

    public ChannelUpdateRequest(String name, String description) {
        super(name, description);
    }
}
