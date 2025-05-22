package com.spirnt.mission.discodeit.service.basic;

import com.spirnt.mission.discodeit.config.auth.CustomUserDetails;
import com.spirnt.mission.discodeit.dto.auth.UserRoleUpdateRequest;
import com.spirnt.mission.discodeit.dto.user.UserDto;
import com.spirnt.mission.discodeit.entity.User;
import com.spirnt.mission.discodeit.exception.User.UserNotFoundException;
import com.spirnt.mission.discodeit.mapper.UserMapper;
import com.spirnt.mission.discodeit.repository.UserRepository;
import com.spirnt.mission.discodeit.service.AuthService;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto getMe(CustomUserDetails customUserDetails) {
        User user = customUserDetails.getUser();
        return userMapper.toDto(user);
    }

    @Transactional
    @Override
    public UserDto updateRole(UserRoleUpdateRequest userRoleUpdateRequest) {
        UUID userId = userRoleUpdateRequest.userId();
        User user = userRepository.findById(userRoleUpdateRequest.userId())
            .orElseThrow(() -> new UserNotFoundException(Map.of("userId", userId)));

        user.updateRole(userRoleUpdateRequest.newRole());
        return userMapper.toDto(user);
    }
}
