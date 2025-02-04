package com.spirnt.mission.discodeit.service;

import com.spirnt.mission.discodeit.dto.UserDto;
import com.spirnt.mission.discodeit.enity.User;
import com.spirnt.mission.discodeit.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    void setUserRepository(UserRepository userRepository);
    void setService(ChannelService channelService, MessageService messageService);

    // Create
    UUID createUser(String email, String name, String password);  // 유저 생성

    // Read
    UserDto getUserInfoById(UUID userId);    // 유저 정보 단건 조회

    List<UserDto> getAllUsersInfo(); // 모든 유저 조회

    // Update
    void updateUserName(UUID userId, String name);  // 유저 이름 수정

    void updateUserEmail(UUID userId, String email);  // 유저 이메일 수정

    void updateUserPassword(UUID userId, String password);  // 유저 비밀번호 수정

    // Delete
    void deleteUser(UUID userId);

    Optional<User> findById(UUID userId);

    boolean checkEmailDuplicate(String email);  //이메일 중복 검사
}
