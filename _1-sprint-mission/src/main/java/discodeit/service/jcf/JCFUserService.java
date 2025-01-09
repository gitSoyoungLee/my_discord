package discodeit.service.jcf;

import discodeit.enity.Channel;
import discodeit.enity.User;
import discodeit.service.ServiceFactory;
import discodeit.service.UserService;

import java.security.Provider;
import java.util.*;

public class JCFUserService implements UserService {
    //싱글톤
    private static volatile JCFUserService instance;

    private final Map<UUID, User> data;   // 모든 유저 데이터, key=id

    private JCFUserService() {
        this.data = new HashMap<>();
    }

    public static JCFUserService getInstance() {
        if(instance == null) {
            synchronized (JCFUserService.class) {
                if(instance == null) {
                    instance = new JCFUserService();
                }
            }
        }
        return instance;
    }

    @Override
    public User createUser(String email, String name) {
        try{
            if(data.entrySet().stream()
                    .anyMatch( entry -> entry.getValue().getEmail().equals(email))){
                throw new IllegalArgumentException("이미 존재하는 이메일");
            }
            User user = new User(name, email);
            data.put(user.getId(), user);
            System.out.println(name + "(" + email + ") 사용자가 등록되었습니다.");
            return user;
        } catch (IllegalArgumentException e){
            System.out.println(email + "은 이미 존재하는 계정입니다.");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void viewUserInfo(User user) {
        if(data.containsKey(user.getId())){
            System.out.println("--- 사용자 조회 ---");
            System.out.println("Email: " + user.getEmail()
                    + " / name: " + user.getName()
                    + " / created at: " + user.getCreatedAt());
            System.out.print("소속 채널: ");
            user.getChannels().stream()
                    .forEach(channel -> {
                        System.out.print(channel.getName() + " ");
                    });
            System.out.println();
        }
        else System.out.println("존재하지 않는 사용자입니다.");
    }

    @Override
    public void viewAllUser() {
        System.out.println("--- 전체 사용자 목록---");
        data.entrySet().stream()
                .sorted(Comparator.comparingLong(entry -> entry.getValue().getCreatedAt())
                )
                .forEach( entry -> {
                    System.out.println("Email: " + entry.getValue().getEmail()
                            + " / name: " + entry.getValue().getName()
                            + " / created at: " + entry.getValue().getCreatedAt());
                    System.out.println("소속 채널: " + entry.getValue().getChannels());
                });
    }

    @Override
    public void updateUserName(User user, String name) {
        user.updateName(name);
    }

    @Override
    public void updateUserEmail(User user, String email) {
        if(data.entrySet().stream()
                .anyMatch( entry -> entry.getValue().getEmail().equals(email))) {
            System.out.println(email + "은 이미 존재하는 이메일입니다.");
            return;
        }
        user.updateEmail(email);
    }

    @Override
    public void deleteUser(User user) {
        if (data.containsKey(user.getId())) {
            // 해당 유저가 속한 모든 채널에서 삭제
            JCFChannelService jcfChannelService = ServiceFactory.getInstance().getJcfchannelService();
            List<Channel> channelsToRemove = new ArrayList<>(user.getChannels());
            channelsToRemove.stream()
                    .forEach(channel -> jcfChannelService.deleteUserInChannel(channel, user));
            data.remove(user.getId());
            System.out.println(user.getName() + " 사용자가 삭제되었습니다.");
        } else {
            System.out.println("존재하지 않는 사용자입니다.");
        }
    }
}
