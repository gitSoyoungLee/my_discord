package discodeit.service.file;

import discodeit.enity.Channel;
import discodeit.enity.User;
import discodeit.repository.file.FileUserRepository;
import discodeit.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

public class FileUserService implements UserService {

    private final FileUserRepository fileUserRepository;
    private FileChannelService fileChannelService;
    private FileMessageService fileMessageService;
    public FileUserService() {
        this.fileUserRepository = new FileUserRepository();
    }
    public void setService(FileChannelService fileChannelService, FileMessageService fileMessageService) {
        this.fileChannelService = fileChannelService;
        this.fileMessageService = fileMessageService;
    }
    @Override
    public UUID createUser(String email, String name, String password) {
        System.out.print("사용자 생성 요청: ");
        if (fileUserRepository.checkEmailDuplicate(email)) {
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
    public void viewUserInfo(UUID userId) {
        System.out.println("--- 사용자 조회 ---");
        try {
            User user = findById(userId);
            System.out.println("Email: " + user.getEmail()
                    + " / name: " + user.getName()
                    + " / created at: " + user.getCreatedAt());
            System.out.print("소속 채널: ");
            user.getChannels().stream()
                    .forEach(channel -> {
                        System.out.print(channel.getName() + " ");
                    });
            System.out.println();
        } catch (NoSuchElementException e) {
            System.out.println("존재하지 않는 사용자입니다. " + e.getMessage());
        }
    }

    @Override
    public void viewAllUser() {
        Map<UUID, User> users = fileUserRepository.findAll();
        System.out.println("--- 전체 사용자 목록---");
        users.values().stream()
                .sorted(Comparator.comparing(user -> user.getCreatedAt()))
                .forEach(user -> {
                    System.out.println("Email: " + user.getEmail()
                            + " / name: " + user.getName()
                            + " / created at: " + user.getCreatedAt());
                    System.out.println("소속 채널: " + user.getChannels());
                });

    }

    @Override
    public void updateUserName(UUID userId, String name) {
        System.out.print("사용자 수정 요청: ");
        try {
            User user = findById(userId);
            String prevName = user.getName();
            user.updateName(name);
            fileUserRepository.save(user);
            System.out.println(prevName + "님의 이름이 " + name + "으로 변경되었습니다.");
        } catch (NoSuchElementException e) {
            System.out.println("존재하지 않는 사용자입니다. " + e.getMessage());
        }
    }

    @Override
    public void updateUserEmail(UUID userId, String email) {
        System.out.print("사용자 수정 요청: ");
        try {
            if (fileUserRepository.checkEmailDuplicate(email)) {
                System.out.println(email + "은 이미 존재하는 이메일입니다.");
                return;
            }
            User user = findById(userId);
            user.updateEmail(email);
            fileUserRepository.save(user);
            System.out.println(user.getName() + "의 이메일이 " + email + "로 변경되었습니다.");
        } catch (NoSuchElementException e) {
            System.out.println("존재하지 않는 사용자입니다. " + e.getMessage());
        }
    }

    @Override
    public void updateUserPassword(UUID userId, String password) {
        System.out.print("사용자 수정 요청: ");
        try {
            if (password.length() < 5) {
                System.out.println("비밀번호는 최소 5자 이상이어야 합니다.");
                return;
            }
            User user = findById(userId);
            String prevName = user.getName();
            user.updatePassword(password);
            System.out.println(prevName + "님의 비밀번호가 변경되었습니다.");
        } catch (NoSuchElementException e) {
            System.out.println("존재하지 않는 사용자입니다. " + e.getMessage());
        }
    }

    @Override
    public void deleteUser(UUID userId) {
        System.out.print("사용자 삭제 요청: ");
        try {
            // 해당 유저가 속한 모든 채널에서 삭제
            User user = findById(userId);
            user.getChannels().stream()
                    .forEach(channel -> {
                        channel.getUsers().remove(user);
                    });
            fileUserRepository.delete(userId);
            System.out.println(user.getName() + " 사용자가 삭제되었습니다.");
        } catch (NoSuchElementException e) {
            System.out.println("존재하지 않는 사용자입니다. " + e.getMessage());
        }
    }

    @Override
    public User findById(UUID userId) {
        return fileUserRepository.findById(userId);
    }

    //FileChannelService에서 채널에 유저 입장/퇴장 시 user.ser를 업데이트하기 위해 호출
    public void addUserIntoChannel(UUID channelId, UUID userId) {
        try {
            User user = findById(userId);
            Channel channel = fileChannelService.findById(channelId);
            user.getChannels().add(channel);
            fileUserRepository.save(user);
        } catch (NoSuchElementException e) {
            throw e;
        }
    }

    public void deleteUserInChannel(UUID channelId, UUID userId) {
        try {
            User user = findById(userId);
            Channel channel = fileChannelService.findById(channelId);
            user.getChannels().remove(channel);
            fileUserRepository.save(user);
        } catch (NoSuchElementException e) {
            throw e;
        }
    }

}
