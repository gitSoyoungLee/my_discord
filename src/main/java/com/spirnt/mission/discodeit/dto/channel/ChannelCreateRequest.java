package com.spirnt.mission.discodeit.dto.channel;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class ChannelCreateRequest extends ChannelBase {
    private List<UUID> usersId;

    public ChannelCreateRequest(String name, String description, List<UUID> users) {
        super(name, description);
        this.usersId = users;
    }
}
