package com.spirnt.mission.discodeit.service;

import com.spirnt.mission.discodeit.dto.userStatus.UserStatusCreate;
import com.spirnt.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.spirnt.mission.discodeit.enity.UserStatus;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface UserStatusService {

  UserStatus create(UserStatusCreate userStatusCreate);

  UserStatus find(UUID userStatusId);

  List<UserStatus> findAll();

  UserStatus update(UUID userStatusId, UserStatusUpdateRequest userStatusUpdateRequest,
      Instant serverTime);

  UserStatus updateByUserId(UUID userId, UserStatusUpdateRequest userStatusUpdateRequest,
      Instant serverTime);

  void delete(UUID userStatusId);

  UserStatus findByUserId(UUID userId);

  void deleteByUserId(UUID userId);
}
