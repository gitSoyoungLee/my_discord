package com.spirnt.mission.discodeit.exception.ReadStatus;

import com.spirnt.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * ID로 조회했는데 ReadStatus가 존재하지 않는 경우 발생하는 예외
 */
public class ReadStatusNotFoundException extends ReadStatusException {

  public ReadStatusNotFoundException(Instant timestamp,
      UUID readStatusId) {
    super(timestamp, ErrorCode.READSTATUS_NOT_FOUND, Map.of("id", readStatusId));
  }
}
