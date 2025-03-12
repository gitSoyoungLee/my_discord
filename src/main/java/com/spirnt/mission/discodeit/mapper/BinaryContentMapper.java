package com.spirnt.mission.discodeit.mapper;

import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.spirnt.mission.discodeit.enity.BinaryContent;
import org.springframework.stereotype.Component;

@Component
public class BinaryContentMapper {

  public BinaryContentDto toDto(BinaryContent binaryContent) {
    if (binaryContent == null) {
      return null;
    }
    return BinaryContentDto.from(binaryContent);
  }
}
