package com.spirnt.mission.discodeit.service.basic;

import com.spirnt.mission.discodeit.config.auth.CustomUserDetails;
import com.spirnt.mission.discodeit.dto.user.UserDto;
import com.spirnt.mission.discodeit.entity.User;
import com.spirnt.mission.discodeit.mapper.UserMapper;
import com.spirnt.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicAuthService implements AuthService {

    private final UserMapper userMapper;

    @Override
    public UserDto getMe(CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();
        return userMapper.toDto(user);
    }
}
