package discodeit.service.jcf;

import discodeit.dto.UserDto;
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
    public UserDto getUserInfoById(UUID userId) {
        User user = findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User ID: " + userId + " Not Found"));

        return new UserDto(user);
    }

    @Override
    public List<UserDto> getAllUsersInfo() {
        Map<UUID, User> data = jcfUserRepository.findAll();
        if (data == null || data.isEmpty()) {
            return null;
        }
        List<UserDto> list = new ArrayList<>();
        data.values().stream()
                .sorted(Comparator.comparing(user -> user.getCreatedAt()))
                .forEach(user -> {
                    list.add(new UserDto(user));
                });
        return list;
    }

    @Override
    public void updateUserName(UUID userId, String name) {
        User user = findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User ID: " + userId + " Not Found"));
        user.updateName(name);
    }

    @Override
    public void updateUserEmail(UUID userId, String email) {
        User user = findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User ID: " + userId + " Not Found"));

        // 중복 이메일인지 검증
        if (checkEmailDuplicate(email)) {
            System.out.println(email + "은 이미 존재하는 이메일입니다.");
        }
        user.updateEmail(email);
    }

    @Override
    public void updateUserPassword(UUID userId, String password) {
        User user = findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User ID: " + userId + " Not Found"));

        if (password.length() < 5) {
            System.out.println("비밀번호는 최소 5자 이상이어야 합니다.");
            return;// 비밀번호가 너무 짧으면 변경하지 않고 종료
        }

        user.updatePassword(password);
    }

    @Override
    public void deleteUser(UUID userId) {
        User user = findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User ID: " + userId + " Not Found"));
        // 해당 유저가 속한 모든 채널에서 삭제하기 위해 채널 서비스 호출
        jcfChannelService.deleteUserInAllChannels(userId);
        jcfUserRepository.delete(userId);
    }

    @Override
    public Optional<User> findById(UUID userId) {
        return jcfUserRepository.findById(userId);
    }

    @Override
    public boolean checkEmailDuplicate(String email) {
        Map<UUID, User> users = jcfUserRepository.findAll();
        if (users == null || users.isEmpty()) {
            return false;
        }
        return users.values().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

}
