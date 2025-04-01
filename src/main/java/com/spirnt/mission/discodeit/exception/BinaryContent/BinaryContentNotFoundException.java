package com.spirnt.mission.discodeit.exception.BinaryContent;

import com.spirnt.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * ID로 조회했는데 BinaryContent가 존재하지 않는 경우 발생하는 예외
 */
public class BinaryContentNotFoundException extends BinaryContentException {

  public BinaryContentNotFoundException(Instant timestamp,
      UUID binaryContentId) {
    super(timestamp, ErrorCode.BINARYCONTENT_NOT_FOUND, Map.of("id", binaryContentId));
  }
}
