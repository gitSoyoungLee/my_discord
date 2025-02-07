package com.spirnt.mission.discodeit.dto.user;

import lombok.Getter;

@Getter
public abstract class UserBase {
    private String name;
    private String email;

    public UserBase(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
