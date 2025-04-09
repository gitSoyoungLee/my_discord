package com.spirnt.mission.discodeit.service.basic;

import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.spirnt.mission.discodeit.dto.user.UserCreateRequest;
import com.spirnt.mission.discodeit.dto.user.UserDto;
import com.spirnt.mission.discodeit.dto.user.UserUpdateRequest;
import com.spirnt.mission.discodeit.entity.BinaryContent;
import com.spirnt.mission.discodeit.entity.User;
import com.spirnt.mission.discodeit.entity.UserStatus;
import com.spirnt.mission.discodeit.entity.UserStatusType;
import com.spirnt.mission.discodeit.exception.BinaryContent.BinaryContentNotFoundException;
import com.spirnt.mission.discodeit.exception.User.UserAlreadyExistException;
import com.spirnt.mission.discodeit.exception.User.UserNotFoundException;
import com.spirnt.mission.discodeit.mapper.UserMapper;
import com.spirnt.mission.discodeit.repository.BinaryContentRepository;
import com.spirnt.mission.discodeit.repository.UserRepository;
import com.spirnt.mission.discodeit.repository.UserStatusRepository;
import com.spirnt.mission.discodeit.service.BinaryContentService;
import com.spirnt.mission.discodeit.service.UserService;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicUserService implements UserService {

  private final UserMapper userMapper;

  private final UserRepository userRepository;

  private final BinaryContentService binaryContentService;
  private final BinaryContentRepository binaryContentRepository;
  private final UserStatusRepository userStatusRepository;

  @Transactional
  @Override
  public UserDto create(UserCreateRequest userCreateRequest,
      BinaryContentCreateRequest binaryContentCreateRequest) {
    String email = userCreateRequest.email();
    String username = userCreateRequest.username();
    String password = userCreateRequest.password();

    if (userRepository.existsByEmail(email)) {
      log.warn("[Creating User Failed: Email {} already exists]", email);
      throw new UserAlreadyExistException(Instant.now(), Map.of("email", email));
    }
    if (userRepository.existsByUsername(username)) {
      log.warn("[Creating User Failed: Username {} already exists]", username);
      throw new UserAlreadyExistException(Instant.now(), Map.of("username", username));
    }
    // User 생성, 저장
    User user = userRepository.save(new User(username, email, password));
    // 관련 도메인
    UserStatus userStatus = userStatusRepository.save(
        new UserStatus(user, UserStatusType.ONLINE, Instant.now()));
    // 프로필 이미지 저장
    BinaryContent binaryContent = null;
    if (binaryContentCreateRequest != null) {
      BinaryContentDto binaryContentDto = binaryContentService.create(binaryContentCreateRequest);
      binaryContent = binaryContentRepository.findById(binaryContentDto.getId())
          .orElseThrow(
              () -> new BinaryContentNotFoundException(Instant.now(),
                  Map.of("binaryContentId", binaryContentDto.getId())));
    }
    user.setProfileAndStatus(binaryContent, userStatus);
    return userMapper.toDto(user);
  }

  @Override
  public UserDto find(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(Instant.now(), Map.of("userId", userId)));
    return userMapper.toDto(user);
  }

  @Transactional(readOnly = true)
  @Override
  public List<UserDto> findAll() {
    List<User> users = userRepository.findAllFetchJoin();
    return users.stream()
        .sorted(Comparator.comparing(user -> user.getCreatedAt()))
        .map(userMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
      BinaryContentCreateRequest binaryContentCreateRequest) {
    User user = userRepository.findById(userId).orElseThrow(() -> {
      log.warn("[Updating User Failed: User with id {} not found]", userId);
      return new UserNotFoundException(Instant.now(), Map.of("userId", userId));
    });

    String email = userUpdateRequest.newEmail();
    String username = userUpdateRequest.newUsername();
    String password = userUpdateRequest.newPassword();

    if (userRepository.existsByEmail(email)) {
      log.warn("[Updating User Failed: Email {} already exists]", email);
      throw new UserAlreadyExistException(Instant.now(), Map.of("email", email));
    }
    if (userRepository.existsByUsername(username)) {
      log.warn("[Updating User Failed: Username {} already exists]", username);
      throw new UserAlreadyExistException(Instant.now(), Map.of("username", username));
    }
    // 프로필 이미지 저장
    // 기존 프로필은 cascade로 자동 삭제
    BinaryContentDto binaryContentDto =
        (binaryContentCreateRequest != null) ? binaryContentService.create(
            binaryContentCreateRequest) : null;
    BinaryContent binaryContent =
        (binaryContentDto == null) ? null
            : binaryContentRepository.findById(binaryContentDto.getId())
                .orElse(null);
    user.update(username, email, password, binaryContent);
    return userMapper.toDto(user);
  }

  @Override
  public void delete(UUID userId) {
    User user = userRepository.findById(userId).orElseThrow(() -> {
      log.warn("[Updating User Failed: User with id {} not found]", userId);
      return new UserNotFoundException(Instant.now(), Map.of("userId", userId));
    });
    userRepository.delete(user);
  }

}
