package com.spirnt.mission.discodeit.exception.UserStatus;

import com.spirnt.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class UserStatusAlreadyExistException extends UserStatusException {

    public UserStatusAlreadyExistException(Map<String, Object> details) {
        super(ErrorCode.USERSTATUS_ALREADY_EXISTS, details);
    }
}
