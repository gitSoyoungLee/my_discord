package com.spirnt.mission.discodeit.service;

import com.spirnt.mission.discodeit.dto.LoginRequest;

import java.util.UUID;

public interface AuthService {
    UUID login(LoginRequest loginRequest);
}
