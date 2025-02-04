package com.spirnt.mission.discodeit.dto;

import com.spirnt.mission.discodeit.enity.User;

// 유저 정보를 읽기 위한 DTO
public class UserDto {
    private User user;

    public UserDto(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "아이디: " + user.getId() +
                " / 이메일: " + user.getEmail() +
                " / 이름: " + user.getName() +
                " / 생성 시간: " + user.getCreatedAt();
    }
}
