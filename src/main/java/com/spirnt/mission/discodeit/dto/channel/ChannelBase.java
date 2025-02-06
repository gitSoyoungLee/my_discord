package com.spirnt.mission.discodeit.dto.channel;

import lombok.Getter;

@Getter
public abstract class ChannelBase {
    private String name;
    private String description;

    public ChannelBase(String name, String description) {
        this.name = name;
        this.description= description;
    }
}
