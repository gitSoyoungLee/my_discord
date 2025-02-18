package com.spirnt.mission.discodeit.dto.user;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UserCreateResponse {
    private UUID userId;
    private String name;
    private String email;

    public UserCreateResponse(UUID userId, String name, String email){
        this.userId=userId;
        this.name=name;
        this.email=email;
    }
}
