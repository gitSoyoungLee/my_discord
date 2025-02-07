package com.spirnt.mission.discodeit.dto.user;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class UserCreateRequest extends UserBase {

    private String password;
    private MultipartFile profileImage;   // 프로필 사진

    public UserCreateRequest(String name, String email,
                             String password, MultipartFile profileImage) {
        super(name, email);
        this.password = password;
        this.profileImage = profileImage;
    }
}
