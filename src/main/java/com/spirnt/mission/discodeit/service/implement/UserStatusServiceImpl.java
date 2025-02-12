package com.spirnt.mission.discodeit.service.implement;

import com.spirnt.mission.discodeit.dto.userStatus.UserStatusCreate;
import com.spirnt.mission.discodeit.dto.userStatus.UserStatusUpdate;
import com.spirnt.mission.discodeit.enity.UserStatus;
import com.spirnt.mission.discodeit.repository.UserStatusRepository;
import com.spirnt.mission.discodeit.service.UserService;
import com.spirnt.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserStatusServiceImpl implements UserStatusService {
    @Autowired
    private final UserStatusRepository repository;
    @Autowired
    private final UserService userService;

    @Override
    public UserStatus create(UserStatusCreate userStatusCreate) {
        // User가 존재하지 않으면 예외 발생
        if (!userService.existsById(userStatusCreate.userId()))
            throw new NoSuchElementException("User ID Not Found");
        // 이미 User와 관련된 객체가 존재하면 예외 발생
        if (repository.findAll().values().stream()
                .anyMatch(userStatus -> userStatus.getUserId().equals(userStatusCreate.userId()))) {
            throw new IllegalStateException("The ReadStatus with UserId and ChannelId Already Exists");
        }
        UserStatus userStatus = new UserStatus(userStatusCreate);
        repository.save(userStatus);
        return userStatus;
    }

    @Override
    public UserStatus find(UUID userStatusId) {
        return repository.findById(userStatusId)
                .orElseThrow(() -> new NoSuchElementException("UserStatus ID Not Found"));
    }

    @Override
    public List<UserStatus> findAll() {
        Map<UUID, UserStatus> map = repository.findAll();
        List<UserStatus> list = map.values().stream()
                .collect(Collectors.toList());
        return list;
    }

    @Override
    public UserStatus update(UUID userStatusId, UserStatusUpdate userStatusUpdate) {
        UserStatus userStatus = repository.findById(userStatusId)
                .orElseThrow(() -> new NoSuchElementException("UserStatus ID Not Found"));
        userStatus.update(userStatusUpdate);
        repository.save(userStatus);
        return userStatus;
    }

    @Override
    public UserStatus updateByUserId(UUID userId, UserStatusUpdate userStatusUpdate) {
        Map<UUID, UserStatus> map = repository.findAll();
        UserStatus userStatus = map.values().stream()
                .filter(value -> value.getUserId().equals(userId))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("UserStatus ID Not Found"));
        userStatus.update(userStatusUpdate);
        repository.save(userStatus);
        return userStatus;
    }

    @Override
    public void delete(UUID userStatusId) {
        repository.delete(userStatusId);
    }

    @Override
    public UserStatus findByUserId(UUID userId) {
        List<UserStatus> list = findAll();
        UUID userStatusId = list.stream()
                .filter(userStatus -> userStatus.getUserId().equals(userId))
                .map(userStatus -> userStatus.getId())
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("UserStatus ID Not Found"));
        return find(userStatusId);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        Map<UUID, UserStatus> map = repository.findAll();
        UserStatus userStatus = map.values().stream()
                .filter(value -> value.getUserId().equals(userId))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("UserStatus ID Not Found"));
        delete(userStatus.getId());
    }
}
