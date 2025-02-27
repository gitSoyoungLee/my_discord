package com.spirnt.mission.discodeit.dto.user;

import com.spirnt.mission.discodeit.enity.User;
import com.spirnt.mission.discodeit.enity.UserStatus;
import com.spirnt.mission.discodeit.enity.UserStatusType;
import java.util.UUID;
import lombok.Getter;

@Getter
// 유저 정보를 읽기 위한 DTO
public class UserDto {

  private UUID id;
  private String name;
  private String email;
  private UserStatusType userStatusType;
  private UUID profileImage;

  public UserDto(User user, UserStatus userStatus) {
    this.name = user.getName();
    this.email = user.getEmail();
    this.id = user.getId();
    this.profileImage = user.getProfileId();
    this.userStatusType = userStatus.getUserStatusType();
  }

  @Override
  public String toString() {
    return "User[Name: " + this.getName() +
        " Email: " + this.getEmail() +
        " ID: " + this.id +
        " (" + userStatusType + ")]";
  }
}
