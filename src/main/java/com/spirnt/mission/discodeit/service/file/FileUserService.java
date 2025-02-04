package com.spirnt.mission.discodeit.service.file;

import com.spirnt.mission.discodeit.dto.UserDto;
import com.spirnt.mission.discodeit.enity.User;
import com.spirnt.mission.discodeit.repository.UserRepository;
import com.spirnt.mission.discodeit.repository.file.FileUserRepository;
import com.spirnt.mission.discodeit.repository.jcf.JCFUserRepository;
import com.spirnt.mission.discodeit.service.ChannelService;
import com.spirnt.mission.discodeit.service.MessageService;
import com.spirnt.mission.discodeit.service.UserService;
import com.spirnt.mission.discodeit.service.jcf.JCFChannelService;
import com.spirnt.mission.discodeit.service.jcf.JCFMessageService;

import java.util.*;

public class FileUserService implements UserService {
    //싱글톤
    private static volatile FileUserService instance;
    private FileUserRepository fileUserRepository;
    private FileChannelService fileChannelService;
    private FileMessageService fileMessageService;

    public FileUserService() {
        this.fileUserRepository = new FileUserRepository();
    }

    @Override
    public void setUserRepository(UserRepository userRepository) {
        this.fileUserRepository = (FileUserRepository) userRepository;
    }

    public static FileUserService getInstance() {
        if (instance == null) {
            synchronized (FileUserService.class) {
                if (instance == null) {
                    instance = new FileUserService();
                }
            }
        }
        return instance;
    }

    protected void setService() {
        this.fileMessageService = fileMessageService.getInstance();
        this.fileChannelService = fileChannelService.getInstance();
    }

    @Override
    public void setService(ChannelService channelService, MessageService messageService) {
        this.fileChannelService = (FileChannelService) channelService;
        this.fileMessageService= (FileMessageService) messageService;
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
        fileUserRepository.save(user);
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
        Map<UUID, User> data = fileUserRepository.findAll();
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
        fileUserRepository.save(user);
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
        fileUserRepository.save(user);
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
        User user = findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User ID: " + userId + " Not Found"));
        // 해당 유저가 속한 모든 채널에서 삭제하기 위해 채널 서비스 호출
        fileChannelService.deleteUserInAllChannels(userId);
        fileUserRepository.delete(userId);
    }


    public Optional<User> findById(UUID userID) {
        return fileUserRepository.findById(userID);
    }


    public boolean checkEmailDuplicate(String email) {
        Map<UUID, User> users = fileUserRepository.findAll();
        if (users == null || users.isEmpty()) {
            return false;
        }
        return users.values().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

}
