package com.spirnt.mission.discodeit.mapper;

import com.spirnt.mission.discodeit.dto.user.UserDto;
import com.spirnt.mission.discodeit.enity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

  private final BinaryContentMapper binaryContentMapper;

  public UserDto toDto(User user) {
    return new UserDto(user.getId(),
        user.getCreatedAt(),
        user.getUpdatedAt(),
        user.getUsername(),
        user.getEmail(),
        user.getStatus().isOnline(),
        binaryContentMapper.toDto(user.getProfile()));
  }
}
