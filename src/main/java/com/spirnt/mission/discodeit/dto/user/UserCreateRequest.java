package com.spirnt.mission.discodeit.dto.user;

import lombok.Getter;

@Getter
public class UserCreateRequest {

  private String name;
  private String email;
  private String password;

  public UserCreateRequest(String name, String email,
      String password) {
    this.name = name;
    this.email = email;
    this.password = password;
  }
}
