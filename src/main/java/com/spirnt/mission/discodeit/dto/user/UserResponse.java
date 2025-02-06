package com.spirnt.mission.discodeit.dto.user;

import com.spirnt.mission.discodeit.enity.User;
import com.spirnt.mission.discodeit.enity.UserStatusType;
import lombok.Getter;

import java.util.UUID;

@Getter
// 유저 정보를 읽기 위한 DTO
public class UserResponse extends UserBase {
    private UUID id;
    private UserStatusType type;

    public UserResponse(User user, UserStatusType type) {
       super(user.getName(), user.getEmail());
       this.id = user.getId();
       this.type= type;
    }
}
