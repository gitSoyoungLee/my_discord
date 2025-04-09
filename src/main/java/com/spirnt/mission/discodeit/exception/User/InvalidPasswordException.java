package com.spirnt.mission.discodeit.exception.User;

import com.spirnt.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.Map;

/**
 * 로그인을 할 때 비밀번호가 DB에 있는 값과 다른 경우 발생하는 예외
 */
public class InvalidPasswordException extends UserException {

  public InvalidPasswordException(Instant timestamp,
      Map<String, Object> details) {
    super(timestamp, ErrorCode.INVALID_PASSWORD, details);
  }
}
