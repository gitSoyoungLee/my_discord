package com.spirnt.mission.discodeit.exception.BinaryContent;

import com.spirnt.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.Map;

/**
 * DB에 BinaryContent 정보는 있는데 실제 파일은 스토리지에 없는 경우 발생하는 예외
 */
public class BinaryContentFileNotFoundException extends BinaryContentException {

  public BinaryContentFileNotFoundException(Instant timestamp,
      ErrorCode errorCode,
      Map<String, Object> details) {
    super(timestamp, errorCode, details);
  }
}
