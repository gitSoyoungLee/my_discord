package com.spirnt.mission.discodeit.service;

import com.spirnt.mission.discodeit.dto.channel.ChannelCreateRequest;
import com.spirnt.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.spirnt.mission.discodeit.enity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    // Create
    Channel createChannelPublic(ChannelCreateRequest channelCreateRequest);    // 채널 생성

    Channel createChannelPrivate(ChannelCreateRequest channelCreateRequest);    // 채널 생성

    // Read
    Channel find(UUID channelId);  // 채널 정보 단건 조회

    List<Channel> findAllByUserId(UUID userId); // 전체 채널 다건 조회

    // Update
    Channel update(UUID channelId, ChannelUpdateRequest channelUpdateRequest);

    // Delete
    void delete(UUID channelId);    // 채널 삭제

    void addUserIntoChannel(UUID channelId, UUID userId);    // 유저가 채널에 입장

    void deleteUserInChannel(UUID channelId, UUID userId);   // 유저를 채널에서 삭제

    void deleteUserInAllChannels(UUID userId);  // 유저를 모든 채널에서 삭제(유저 삭제를 위한 메소드)

    boolean existsById(UUID id);
}
