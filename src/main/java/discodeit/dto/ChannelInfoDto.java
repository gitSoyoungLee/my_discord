package discodeit.dto;

import discodeit.enity.Channel;

import java.util.List;
import java.util.stream.Collectors;

public class ChannelInfoDto {
    private Channel channel;

    public ChannelInfoDto(Channel channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "채널: " + channel.getName() +
                " / 설명: " + channel.getDescription() +
                " / 참여 중인 사용자 수: " + channel.getUsers().size();
    }
}
