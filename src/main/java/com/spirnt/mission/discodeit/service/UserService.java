package com.spirnt.mission.discodeit.service;

import com.spirnt.mission.discodeit.dto.user.UserCreateRequest;
import com.spirnt.mission.discodeit.dto.user.UserDto;
import com.spirnt.mission.discodeit.dto.user.UserUpdateRequest;
import com.spirnt.mission.discodeit.enity.User;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

  // Create
  User create(UserCreateRequest userCreateRequest, MultipartFile profileImage);  // 유저 생성

  // Read
  UserDto find(UUID userId);    // 유저 정보 단건 조회

  List<UserDto> findAll(); // 모든 유저 조회

  // Update
  User update(UUID userId, UserUpdateRequest userUpdateRequest, MultipartFile profileImgage);

  // Delete
  void delete(UUID userId);
}
