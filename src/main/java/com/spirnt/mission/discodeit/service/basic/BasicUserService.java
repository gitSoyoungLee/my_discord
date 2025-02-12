package com.spirnt.mission.discodeit.service.basic;

import com.spirnt.mission.discodeit.dto.user.UserCreateRequest;
import com.spirnt.mission.discodeit.dto.user.UserUpdateRequest;
import com.spirnt.mission.discodeit.enity.User;
import com.spirnt.mission.discodeit.repository.UserRepository;
import com.spirnt.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;

    @Override
    public User create(UserCreateRequest dto) {
        if (checkEmailDuplicate(dto.getEmail())) {
            throw new IllegalArgumentException(dto.getEmail() + " Email Already Exists");
        }
        if (checkNameDuplicate(dto.getName())) {
            throw new IllegalArgumentException(dto.getName() + "Name Already Exists");
        }
        User user = new User(dto);
        userRepository.save(user);
        return user;
    }

    @Override
    public User find(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User ID: " + userId + " Not Found"));
    }

    @Override
    public List<User> findAll() {
        Map<UUID, User> data = userRepository.findAll();
        List<User> list = new ArrayList<>();
        if (data.isEmpty()) return list;
        data.values().stream()
                .sorted(Comparator.comparing(user -> user.getCreatedAt()))
                .forEach(user -> {
                    list.add(user);
                });
        return list;
    }

    @Override
    public User update(UUID userId, UserUpdateRequest dto) {
        if (checkEmailDuplicate(dto.getEmail())) {
            throw new IllegalArgumentException(dto.getEmail() + " Email Already Exists");
        }
        if (checkNameDuplicate(dto.getName())) {
            throw new IllegalArgumentException(dto.getName() + "Name Already Exists");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Usre ID: " + userId + " Not found"));
        user.update(dto);
        userRepository.save(user);
        return user;
    }

    @Override
    public void delete(UUID userId) {
        // 존재하는 유저인지 검증
        if (userRepository.existsById(userId)) {
            userRepository.delete(userId);
        } else {
            throw new NoSuchElementException("User ID: " + userId + " Not Found");
        }
    }


    @Override
    public boolean checkEmailDuplicate(String email) {
        Map<UUID, User> users = userRepository.findAll();
        if (users == null || users.isEmpty()) {
            return false;
        }
        return users.values().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public boolean checkNameDuplicate(String name) {
        Map<UUID, User> users = userRepository.findAll();
        if (users == null || users.isEmpty()) {
            return false;
        }
        return users.values().stream()
                .anyMatch(user -> user.getName().equals(name));
    }

    @Override
    public boolean existsById(UUID id) {
        return userRepository.existsById(id);
    }
}
