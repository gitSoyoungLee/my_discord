package com.spirnt.mission.discodeit.service;

import com.spirnt.mission.discodeit.config.auth.CustomUserDetails;
import com.spirnt.mission.discodeit.dto.user.UserDto;

public interface AuthService {

    UserDto getMe(CustomUserDetails customUserDetails);
}
