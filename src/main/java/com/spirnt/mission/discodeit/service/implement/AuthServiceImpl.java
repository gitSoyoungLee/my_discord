package com.spirnt.mission.discodeit.service.implement;

import com.spirnt.mission.discodeit.dto.LoginRequest;
import com.spirnt.mission.discodeit.enity.User;
import com.spirnt.mission.discodeit.repository.UserRepository;
import com.spirnt.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;

    @Override
    public User login(LoginRequest loginRequest) {
        Map<UUID, User> users = userRepository.findAll();
        User user = users.values().stream()
                .filter(value -> value.getName().equals(loginRequest.name()) &&
                        value.getPassword().equals(loginRequest.password()))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("User Not Found"));
        return user;
    }
}
