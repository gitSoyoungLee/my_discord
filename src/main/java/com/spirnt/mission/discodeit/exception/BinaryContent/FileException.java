package com.spirnt.mission.discodeit.exception.BinaryContent;

import com.spirnt.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.Map;

/**
 * BinaryContent를 스토리지에 저장하거나, 스토리지에서 읽을 때 발생하는 에러
 */
public class FileException extends BinaryContentException {

  public FileException(Instant timestamp, Map<String, Object> details) {
    super(timestamp, ErrorCode.FILE_ERROR, details);
  }
}
