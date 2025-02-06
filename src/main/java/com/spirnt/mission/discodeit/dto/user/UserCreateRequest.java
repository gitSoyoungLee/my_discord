package com.spirnt.mission.discodeit.dto.user;

import lombok.Getter;

@Getter
public class UserCreateRequest extends UserBase{

    private String password;
    private String imagePath;    // 프로필 사진

    public UserCreateRequest(String name, String email,
                             String password, String imagePath) {
        super(name, email);
        this.password = password;
        this.imagePath= imagePath;
    }
}
