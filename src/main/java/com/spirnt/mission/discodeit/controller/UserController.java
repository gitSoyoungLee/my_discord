package com.spirnt.mission.discodeit.controller;

import com.spirnt.mission.discodeit.controller.swagger.UserApiDocs;
import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.spirnt.mission.discodeit.dto.user.UserCreateRequest;
import com.spirnt.mission.discodeit.dto.user.UserDto;
import com.spirnt.mission.discodeit.dto.user.UserUpdateRequest;
import com.spirnt.mission.discodeit.dto.userStatus.UserStatusDto;
import com.spirnt.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.spirnt.mission.discodeit.service.UserService;
import com.spirnt.mission.discodeit.service.UserStatusService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController implements UserApiDocs {


  private final UserService userService;
  private final UserStatusService userStatusService;

  // 모든 사용자 조회
  @GetMapping(value = "")
  public ResponseEntity<List<UserDto>> getAllUsers() {
    return ResponseEntity.ok(userService.findAll());
  }


  // 사용자 등록
  @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserDto> createUser(@Valid @RequestPart UserCreateRequest userCreateRequest,
      @RequestPart(required = false) MultipartFile profile) {
    log.info("[Creating User started]");
    BinaryContentCreateRequest binaryContentCreateRequest =
        (profile == null || profile.isEmpty()) ? null : new BinaryContentCreateRequest(profile);
    UserDto userDto = userService.create(userCreateRequest, binaryContentCreateRequest);
    log.info("[User Created / id: {}]", userDto.getId());
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(userDto);
  }

  // 사용자 정보 수정
  @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<UserDto> updateUser(@PathVariable UUID userId,
      @Valid @RequestPart UserUpdateRequest userUpdateRequest,
      @RequestPart(required = false) MultipartFile profile) {
    log.info("[Updating User started / id: {}]", userId);
    BinaryContentCreateRequest binaryContentCreateRequest =
        (profile == null || profile.isEmpty()) ? null : new BinaryContentCreateRequest(profile);
    UserDto userDto = userService.update(userId, userUpdateRequest, binaryContentCreateRequest);
    log.info("[User Updated / id: {}]", userId);
    return ResponseEntity.ok(userDto);
  }

  // 사용자 삭제
  @DeleteMapping("/{userId}")
  public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
    log.info("[Deleting User started / id: {}]", userId);
    userService.delete(userId);
    log.info("[User Deleted / id: {}]", userId);
    return ResponseEntity.noContent().build();
  }

  // 사용자의 온라인 상태 업데이트
  @PatchMapping(value = "/{userId}/userStatus", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserStatusDto> updateUserStatus(@PathVariable UUID userId,
      @Valid @RequestBody UserStatusUpdateRequest userStatusUpdateRequest) {
    UserStatusDto userStatusDto = userStatusService.updateByUserId(userId, userStatusUpdateRequest);
    return ResponseEntity.ok(userStatusDto);
  }

}

