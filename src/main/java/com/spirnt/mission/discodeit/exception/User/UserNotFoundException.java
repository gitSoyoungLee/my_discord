package com.spirnt.mission.discodeit.exception.User;

import com.spirnt.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.Map;

/**
 * ID로 User를 조회했으나 User가 데이터베이스에 조회하지 않을 때 발생하는 예외
 */
public class UserNotFoundException extends UserException {


  public UserNotFoundException(Instant timestamp, Map<String, Object> details) {
    super(timestamp, ErrorCode.USER_NOT_FOUND, details);
  }
}
