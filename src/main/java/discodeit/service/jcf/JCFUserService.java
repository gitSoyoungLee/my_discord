package discodeit.service.jcf;

import discodeit.dto.UserInfoDto;
import discodeit.enity.Channel;
import discodeit.enity.User;
import discodeit.repository.jcf.JCFUserRepository;
import discodeit.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

public class JCFUserService implements UserService {
    //싱글톤
    private static volatile JCFUserService instance;
    private final JCFUserRepository jcfUserRepository;

    private JCFChannelService jcfChannelService;
    private JCFMessageService jcfMessageService;

    private JCFUserService() {
        this.jcfUserRepository = new JCFUserRepository();

    }

    public static JCFUserService getInstance() {
        if (instance == null) {
            synchronized (JCFUserService.class) {
                if (instance == null) {
                    instance = new JCFUserService();
                }
            }
        }
        return instance;
    }

    protected void setService() {
        this.jcfMessageService = jcfMessageService.getInstance();
        this.jcfChannelService = jcfChannelService.getInstance();
    }

    @Override
    public UUID createUser(String email, String name, String password) {
        if (checkEmailDuplicate(email)) {
            System.out.println("이미 존재하는 계정입니다.");
            return null;
        }
        if (password.length() < 5) {
            System.out.println("비밀번호는 최소 5자 이상이어야 합니다.");
            return null;
        }
        User user = new User(name, email, password);
        jcfUserRepository.save(user);
        System.out.println(name + "(" + email + ") 사용자가 등록되었습니다.");
        return user.getId();
    }

    @Override
    public UserInfoDto getUserInfoById(UUID userId) {
        try {
            // UUID로 User 객체 찾기
            User user = findById(userId);
            // 해당 유저가 소속된 채널 목록 찾기
            List<Channel> channelsContainUser = jcfChannelService.getChannelsByUserId(userId);
            // 채널명만 가져옴
            List<String> channelNames = channelsContainUser.stream()
                    .sorted(Comparator.comparing(channel -> channel.getCreatedAt()))
                    .map(channel -> channel.getName())
                    .collect(Collectors.toList());

            return new UserInfoDto(user);
        } catch (NoSuchElementException e) {
            System.out.println("존재하지 않는 사용자입니다. " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<UserInfoDto> getAllUsersInfo() {
        Map<UUID, User> data = jcfUserRepository.findAll();
        if (data == null || data.isEmpty()) {
            System.out.println("사용자가 없습니다.");
            return null;
        }
        List<UserInfoDto> list = new ArrayList<>();
        data.values().stream()
                .sorted(Comparator.comparing(user -> user.getCreatedAt()))
                .forEach(user -> {
                    list.add(new UserInfoDto(user));    // 전체 사용자 목록 조회 시에는 소속 채널 출력 x
                });
        return list;
    }

    @Override
    public void updateUserName(UUID userId, String name) {
        try {
            User user = findById(userId);
            String prevName = user.getName();
            user.updateName(name);
            System.out.println(prevName + "님의 이름이 " + name + "으로 변경되었습니다.");
        } catch (NoSuchElementException e) {
            System.out.println("존재하지 않는 사용자입니다. " + e.getMessage());
        }
    }

    @Override
    public void updateUserEmail(UUID userId, String email) {
        try {
            if (checkEmailDuplicate(email)) {
                System.out.println(email + "은 이미 존재하는 이메일입니다.");
                return;
            }
            User user = findById(userId);
            user.updateEmail(email);
            System.out.println(user.getName() + "의 이메일이 " + email + "로 변경되었습니다.");
        } catch (NoSuchElementException e) {
            System.out.println("존재하지 않는 사용자입니다. " + e.getMessage());
        }
    }

    @Override
    public void updateUserPassword(UUID userId, String password) {
        try {
            User user = findById(userId);
            if (password.length() < 5) {
                System.out.println("비밀번호는 최소 5자 이상이어야 합니다.");
            }
            user.updatePassword(password);
            System.out.println(user.getEmail() + " 계정의 비밀번호가 변경되었습니다.");
        } catch (NoSuchElementException e) {
            System.out.println("존재하지 않는 사용자입니다. " + e.getMessage());
        }
    }

    @Override
    public void deleteUser(UUID userId) {
        try {
            User user = findById(userId);
            // 해당 유저가 속한 모든 채널에서 삭제하기 위해 채널 서비스 호출
            jcfChannelService.deleteUserInAllChannels(userId);
            jcfUserRepository.delete(userId);
            System.out.println(user.getName() + " 사용자가 삭제되었습니다.");
        } catch (NoSuchElementException e) {
            System.out.println("존재하지 않는 사용자입니다. " + e.getMessage());
        }
    }

    @Override
    public User findById(UUID userId) {
        return jcfUserRepository.findById(userId);
    }

    @Override
    public boolean checkEmailDuplicate(String email) {
        Collection<User> users = jcfUserRepository.findAll().values();
        if (users == null) return false;
        return users.stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

}
