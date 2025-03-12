package com.spirnt.mission.discodeit.mapper;

import com.spirnt.mission.discodeit.dto.userStatus.UserStatusDto;
import com.spirnt.mission.discodeit.enity.UserStatus;
import org.springframework.stereotype.Component;

@Component
public class UserStatusMapper {

  public UserStatusDto toDto(UserStatus userStatus) {
    return UserStatusDto.from(userStatus);
  }
}
