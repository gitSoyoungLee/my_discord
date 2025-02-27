package com.spirnt.mission.discodeit.service.basic;

import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentCreate;
import com.spirnt.mission.discodeit.dto.user.UserCreateRequest;
import com.spirnt.mission.discodeit.dto.user.UserDto;
import com.spirnt.mission.discodeit.dto.user.UserUpdateRequest;
import com.spirnt.mission.discodeit.dto.userStatus.UserStatusCreate;
import com.spirnt.mission.discodeit.enity.BinaryContent;
import com.spirnt.mission.discodeit.enity.User;
import com.spirnt.mission.discodeit.enity.UserStatus;
import com.spirnt.mission.discodeit.enity.UserStatusType;
import com.spirnt.mission.discodeit.repository.UserRepository;
import com.spirnt.mission.discodeit.service.BinaryContentService;
import com.spirnt.mission.discodeit.service.UserService;
import com.spirnt.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;

  private final BinaryContentService binaryContentService;
  private final UserStatusService userStatusService;

  @Override
  public User create(UserCreateRequest userCreateRequest, MultipartFile profileImage) {
    // 파라미터 검증
    if (checkEmailDuplicate(userCreateRequest.getEmail())) {
      throw new IllegalStateException(userCreateRequest.getEmail() + " Email Already Exists");
    }
    if (checkNameDuplicate(userCreateRequest.getName())) {
      throw new IllegalStateException(userCreateRequest.getName() + " Name Already Exists");
    }
    // 프로필 이미지 저장
    // request dto에서 profileImage가 null이 아니면 BinaryContent 생성
    BinaryContent binaryContent = Optional.ofNullable(profileImage)
        .map(BinaryContentCreate::new)
        .map(binaryContentService::create)
        .orElse(null);
    UUID profileImageId = (binaryContent == null) ? null : binaryContent.getId();

    // User 생성, 저장
    User user = new User(userCreateRequest.getName(),
        userCreateRequest.getEmail(),
        userCreateRequest.getPassword(),
        profileImageId);
    userRepository.save(user);

    // UserStatus 생성
    UserStatusCreate userStatusCreate = new UserStatusCreate(user.getId(), UserStatusType.ONLINE,
        Instant.now());
    userStatusService.create(userStatusCreate);

    return user;
  }

  @Override
  public UserDto find(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User ID: " + userId + " Not Found"));
    UserStatus userStatus = userStatusService.findByUserId(userId);
    return new UserDto(user, userStatus);
  }

  @Override
  public List<UserDto> findAll() {
    Map<UUID, User> data = userRepository.findAll();
    return data.values().stream()
        .sorted(Comparator.comparing(user -> user.getCreatedAt()))
        .map(user -> find(user.getId()))
        .collect(Collectors.toList());
  }

  @Override
  public User update(UUID userId, UserUpdateRequest userUpdateRequest, MultipartFile profileImage) {
    // User 객체 변경
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User ID: " + userId + " Not found"));
    if (checkEmailDuplicate(userUpdateRequest.getEmail())) {
      throw new IllegalArgumentException(userUpdateRequest.getEmail() + " Email Already Exists");
    }
    if (checkNameDuplicate(userUpdateRequest.getName())) {
      throw new IllegalArgumentException(userUpdateRequest.getName() + " Name Already Exists");
    }
    //프로필 이미지 선택적 대체
    UUID profileImageId = null;
    if (profileImage != null) {
      binaryContentService.delete(user.getProfileId());
      BinaryContent newProfileImage = binaryContentService.create(
          new BinaryContentCreate(profileImage));
      profileImageId = newProfileImage.getId();
    }
    user.update(userUpdateRequest.getName(), userUpdateRequest.getEmail(),
        userUpdateRequest.getPassword(), profileImageId);
    userRepository.save(user);
    return user;
  }

  @Override
  public void delete(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User ID: " + userId + " Not found"));
    // 유저의 프로필  BinaryContent 삭제
    binaryContentService.delete(user.getProfileId());
    // UserStatus 삭제
    userStatusService.deleteByUserId(userId);
    userRepository.delete(userId);
  }


  public boolean checkEmailDuplicate(String email) {
    Map<UUID, User> users = userRepository.findAll();
    if (users == null || users.isEmpty()) {
      return false;
    }
    return users.values().stream()
        .anyMatch(user -> user.getEmail().equals(email));
  }

  public boolean checkNameDuplicate(String name) {
    Map<UUID, User> users = userRepository.findAll();
    if (users == null || users.isEmpty()) {
      return false;
    }
    return users.values().stream()
        .anyMatch(user -> user.getName().equals(name));
  }

}
