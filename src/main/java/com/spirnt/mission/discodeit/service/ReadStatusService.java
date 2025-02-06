package com.spirnt.mission.discodeit.service;

import com.spirnt.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.spirnt.mission.discodeit.enity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

    UUID create(ReadStatusDto readStatusDto);

    ReadStatus find(UUID readStatusId);

    List<ReadStatus> findAllByUserId(UUID userId);

    void update(UUID readStatusId, ReadStatusDto readStatusDto);

    void delete(UUID id);

}
