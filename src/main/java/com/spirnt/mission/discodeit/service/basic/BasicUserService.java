package com.spirnt.mission.discodeit.service.basic;

import com.spirnt.mission.discodeit.dto.UserDto;
import com.spirnt.mission.discodeit.enity.User;
import com.spirnt.mission.discodeit.repository.UserRepository;
import com.spirnt.mission.discodeit.service.ChannelService;
import com.spirnt.mission.discodeit.service.MessageService;
import com.spirnt.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private ChannelService channelService;
    private MessageService messageService;

    @Override
    public void setService(ChannelService channelService, MessageService messageService) {
        this.channelService = channelService;
        this.messageService = messageService;
    }

    @Override
    public UUID createUser(String email, String name, String password) {
        if (checkEmailDuplicate(email)) {
            System.out.println(email + "은 이미 존재하는 이메일입니다.");
            return null;
        }
        if (password.length() < 5) {
            System.out.println("비밀번호는 최소 5자 이상이어야 합니다.");
            return null;
        }
        User user = new User(name, email, password);
        userRepository.save(user);
        System.out.println(name + "(" + email + ") 사용자가 등록되었습니다.");
        return user.getId();
    }

    @Override
    public UserDto getUserInfoById(UUID userId) {
        User user = findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User ID: " + userId + " Not Found"));

        return new UserDto(user);
    }

    @Override
    public List<UserDto> getAllUsersInfo() {
        Map<UUID, User> data = userRepository.findAll();
        List<UserDto> list = new ArrayList<>();
        if (data.isEmpty()) return list;
        data.values().stream()
                .sorted(Comparator.comparing(user -> user.getCreatedAt()))
                .forEach(user -> {
                    list.add(new UserDto(user));    // 전체 사용자 목록 조회 시에는 소속 채널 출력 x
                });
        return list;
    }


    @Override
    public void updateUserName(UUID userId, String name) {
        User user = findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User ID: " + userId + " Not Found"));
        user.updateName(name);
        userRepository.save(user);
    }

    @Override
    public void updateUserEmail(UUID userId, String email) {
        User user = findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User ID: " + userId + " Not Found"));

        // 중복 이메일인지 검증
        if (checkEmailDuplicate(email)) {
            System.out.println(email + "은 이미 존재하는 이메일입니다.");
            return;
        }
        user.updateEmail(email);
        userRepository.save(user);
    }

    @Override
    public void updateUserPassword(UUID userId, String password) {
        User user = findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User ID: " + userId + " Not Found"));

        if (password.length() < 5) {
            System.out.println("비밀번호는 최소 5자 이상이어야 합니다.");
            return; // 비밀번호가 너무 짧으면 변경하지 않고 종료
        }

        user.updatePassword(password);
        // 비밀번호는 직렬화에서 제외되므로 save() 안함
    }

    @Override
    public void deleteUser(UUID userId) {
        // 먼저 존재하는 유저인지 검증
        User user = findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User ID: " + userId + " Not Found"));
        // 해당 유저가 속한 모든 채널에서 삭제하기 위해 채널 서비스 호출
        channelService.deleteUserInAllChannels(userId);
        userRepository.delete(userId);
    }

    @Override
    public Optional<User> findById(UUID userId) {
        return userRepository.findById(userId);
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

}
