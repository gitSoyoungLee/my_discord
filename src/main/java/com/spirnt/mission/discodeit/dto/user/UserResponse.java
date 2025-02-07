package com.spirnt.mission.discodeit.dto.user;

import com.spirnt.mission.discodeit.enity.User;
import lombok.Getter;

import java.util.UUID;

@Getter
// 유저 정보를 읽기 위한 DTO
public class UserResponse extends UserBase {
    private UUID id;

    public UserResponse(User user) {
        super(user.getName(), user.getEmail());
        this.id = user.getId();
    }

    @Override
    public String toString() {
        return "User[Name: " + this.getName() +
                " Email: " + this.getEmail() +
                " ID: " + this.id + "]";
    }
}
