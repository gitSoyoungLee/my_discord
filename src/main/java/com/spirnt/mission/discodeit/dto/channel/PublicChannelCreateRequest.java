package com.spirnt.mission.discodeit.dto.channel;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class PublicChannelCreateRequest {
    private String name;
    private String description;
    public PublicChannelCreateRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
