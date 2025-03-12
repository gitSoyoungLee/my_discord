package com.spirnt.mission.discodeit.mapper;

import com.spirnt.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.spirnt.mission.discodeit.enity.ReadStatus;
import org.springframework.stereotype.Component;

@Component
public class ReadStatusMapper {

  public ReadStatusDto toDto(ReadStatus readStatus) {
    return ReadStatusDto.from(readStatus);
  }

}
