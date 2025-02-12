package com.spirnt.mission.discodeit.service.facade;

import com.spirnt.mission.discodeit.dto.channel.ChannelCreateRequest;
import com.spirnt.mission.discodeit.dto.channel.ChannelResponse;
import com.spirnt.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.spirnt.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.spirnt.mission.discodeit.enity.Channel;
import com.spirnt.mission.discodeit.enity.ChannelType;
import com.spirnt.mission.discodeit.service.ChannelService;
import com.spirnt.mission.discodeit.service.MessageService;
import com.spirnt.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChannelFacade {
    @Autowired
    private final ChannelService channelService;
    @Autowired
    private final MessageService messageService;
    @Autowired
    private final ReadStatusService readStatusService;

    public Channel createChannelPublic(ChannelCreateRequest channelCreateRequest) {
        return channelService.createChannelPublic(channelCreateRequest);
    }

    public Channel createChannelPrivate(ChannelCreateRequest channelCreateRequest) {
        Channel channel = channelService.createChannelPrivate(channelCreateRequest);
        //ReadStatus 생성
        for (UUID userId : channelCreateRequest.getUsersId()) {
            readStatusService.create(new ReadStatusDto(userId, channel.getId(), Instant.now()));
        }
        return channel;
    }

    public ChannelResponse findChannel(UUID channelId) {
        Channel channel = channelService.find(channelId);
        // 해당 채널의 가장 최근 메세지 작성 시간 찾기
        Instant lastSeenAt = messageService.findLastMessageInChannel(channelId)
                .orElse(channel.getCreatedAt());    // 채널 내 메세지가 없는 경우 채널 생성 시간을 디폴트로 함
        return new ChannelResponse(channel, lastSeenAt);
    }

    public List<ChannelResponse> findAllChannelsByUserId(UUID userId) {
        List<Channel> channels = channelService.findAllByUserId(userId);
        return channels.stream()
                .filter(channel -> (channel.getType() == ChannelType.PUBLIC) ||
                        (channel.getType() == ChannelType.PRIVATE && channel.containsUser(userId)))
                .map(channel -> findChannel(channel.getId()))
                .collect(Collectors.toList());
    }

    public Channel updateChannel(UUID channelId, ChannelUpdateRequest channelUpdateRequest) {
        return channelService.update(channelId, channelUpdateRequest);
    }

    public void deleteChannel(UUID channelId) {
        // 채널에 속해있던 메세지도 삭제
        messageService.deleteByChannelId(channelId);
        // 관련 ReadStatus 삭제하기
        readStatusService.deleteByChannelId(channelId);
        channelService.delete(channelId);
    }

    public void addUserIntoChannel(UUID channelId, UUID userId) {
        channelService.addUserIntoChannel(channelId, userId);
    }

    public void deleteUserInChannel(UUID channelId, UUID userId) {
        channelService.deleteUserInChannel(channelId, userId);
    }

}
