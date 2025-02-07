package com.spirnt.mission.discodeit.service;

import com.spirnt.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.spirnt.mission.discodeit.enity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

    void setService(UserService userService, ChannelService channelService);

    ReadStatus create(ReadStatusDto readStatusDto);

    ReadStatus find(UUID readStatusId);

    List<ReadStatus> findAllByUserId(UUID userId);

    ReadStatus update(UUID readStatusId, ReadStatusDto readStatusDto);

    void delete(UUID id);

    void deleteByChannelId(UUID channelId);

}
