package com.spirnt.mission.discodeit.service.facade;

import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentCreate;
import com.spirnt.mission.discodeit.dto.user.UserCreateRequest;
import com.spirnt.mission.discodeit.dto.user.UserResponse;
import com.spirnt.mission.discodeit.dto.user.UserUpdateRequest;
import com.spirnt.mission.discodeit.dto.userStatus.UserStatusCreate;
import com.spirnt.mission.discodeit.enity.User;
import com.spirnt.mission.discodeit.enity.UserStatusType;
import com.spirnt.mission.discodeit.service.BinaryContentService;
import com.spirnt.mission.discodeit.service.ChannelService;
import com.spirnt.mission.discodeit.service.UserService;
import com.spirnt.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserFacade {
    private final UserService userService;
    private final ChannelService channelService;
    private final BinaryContentService binaryContentService;
    private final UserStatusService userStatusService;

    public User createUser(UserCreateRequest userCreateRequest) {
        User user = userService.create(userCreateRequest);
        //프로필 이미지 저장
        if (userCreateRequest.getProfileImage() != null)
            binaryContentService.create(new BinaryContentCreate(user.getId(), null, userCreateRequest.getProfileImage()));
        // UserStatus 생성
        UserStatusCreate userStatusCreate = new UserStatusCreate(user.getId(), UserStatusType.ONLINE, Instant.now());
        userStatusService.create(userStatusCreate);
        return user;
    }

    private void createUserStatus(UserStatusCreate userStatusCreate) {
        userStatusService.create(userStatusCreate);
    }

    public UserResponse findUser(UUID userId) {
        User user = userService.find(userId);
        return new UserResponse(user);
    }

    public List<UserResponse> findAll() {
        List<User> users = userService.findAll();
        return users.stream()
                .map(user -> new UserResponse(user))
                .collect(Collectors.toList());
    }

    public User updateUser(UUID userId, UserUpdateRequest userUpdateRequest) {
        //프로필 이미지 선택적 대체
        if(userUpdateRequest.getProfileImage()!=null){
            binaryContentService.deleteUserProfile(userId);
            binaryContentService.create(new BinaryContentCreate(userId, null, userUpdateRequest.getProfileImage()));
        }
        return userService.update(userId, userUpdateRequest);
    }


    public void deleteUser(UUID userId) {
        userService.delete(userId);
        // 해당 유저가 속한 모든 채널에서 삭제하기 위해 채널 서비스 호출
        channelService.deleteUserInAllChannels(userId);
        // 유저의 프로필 삭제
        binaryContentService.deleteUserProfile(userId);
        // UserStatus 삭제
        userStatusService.deleteByUserId(userId);
    }
}
