package com.spirnt.mission.discodeit.dto.channel;

import com.spirnt.mission.discodeit.enity.Channel;
import com.spirnt.mission.discodeit.enity.ChannelType;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
@Getter
public class ChannelResponse extends ChannelBase
{
    private UUID channelId;
    private ChannelType type;
    private List<UUID> usersId;
    private Instant lastMessage;

    public ChannelResponse(Channel channel, Instant lastMessage) {
        super(channel.getName(), channel.getDescription());
        this.type = channel.getType();
        this.usersId= channel.getUsersId();
        this.lastMessage= lastMessage;
}
}
