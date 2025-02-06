package com.spirnt.mission.discodeit.service.basic;

import com.spirnt.mission.discodeit.dto.userStatus.UserStatusCreate;
import com.spirnt.mission.discodeit.dto.userStatus.UserStatusUpdate;
import com.spirnt.mission.discodeit.enity.ReadStatus;
import com.spirnt.mission.discodeit.enity.User;
import com.spirnt.mission.discodeit.enity.UserStatus;
import com.spirnt.mission.discodeit.repository.UserStatusRepository;
import com.spirnt.mission.discodeit.service.UserService;
import com.spirnt.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository repository;
    private final UserService userService;

    @Override
    public UUID create(UserStatusCreate userStatusCreate) {
        // User가 존재하지 않으면 예외 발생
        User user = userService.findById(userStatusCreate.userId())
                .orElseThrow(()->new NoSuchElementException("User ID: "+userStatusCreate.userId()+
                        " Not Found"));
        // 이미 User와 관련된 객체가 존재하면 예외 발생
        if(repository.findAll().values().stream()
                .anyMatch(userStatus -> userStatus.getUserId().equals(userStatusCreate.userId()))) {
            throw new IllegalStateException("The ReadStatus with UserId and ChannelId Already Exists");
        }
        UserStatus userStatus = new UserStatus(userStatusCreate);
        repository.save(userStatus);
        return userStatus.getId();
    }

    @Override
    public UserStatus find(UUID userStatusId) {
        return repository.findById(userStatusId)
                .orElseThrow(()->new NoSuchElementException("UserStatus ID Not Found"));
    }

    @Override
    public List<UserStatus> findAll() {
        Map<UUID, UserStatus> map = repository.findAll();
        List<UserStatus> list = map.values().stream()
                .collect(Collectors.toList());
        return list;
    }

    @Override
    public void update(UUID userStatusId, UserStatusUpdate userStatusUpdate) {
        UserStatus userStatus = repository.findById(userStatusId)
                .orElseThrow(()->new NoSuchElementException("UserStatus ID Not Found"));
        userStatus.update(userStatusUpdate);
        repository.save(userStatus);
    }

    @Override
    public void updateByUserId(UUID userId, UserStatusUpdate userStatusUpdate) {
        Map<UUID, UserStatus> map = repository.findAll();
        UserStatus userStatus = map.values().stream()
                .filter(value->value.getUserId().equals(userId))
                .findAny()
                .orElseThrow(()->new NoSuchElementException("UserStatus ID Not Found"));
        userStatus.update(userStatusUpdate);
        repository.save(userStatus);
    }

    @Override
    public void delete(UUID userStatusId) {
        repository.delete(userStatusId);
    }
}
