package com.spirnt.mission.discodeit.dto;

import com.spirnt.mission.discodeit.enity.Channel;

public class ChannelDto {
    private Channel channel;

    public ChannelDto(Channel channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "채널: " + channel.getName() +
                " / 설명: " + channel.getDescription() +
                " / 참여 중인 사용자 수: " + channel.getUsersId().size();
    }
}
