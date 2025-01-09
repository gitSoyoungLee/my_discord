package discodeit.service.jcf;

import discodeit.enity.Channel;
import discodeit.enity.Message;
import discodeit.enity.User;
import discodeit.service.ChannelService;
import discodeit.service.ServiceFactory;

import java.util.ArrayList;
import java.util.List;

public class JCFChannelService implements ChannelService {

    private static volatile JCFChannelService instance;
    private final List<Channel> data;

    private JCFChannelService() {
        this.data = new ArrayList<>();
    }

    public static JCFChannelService getInstance() {
        if(instance == null) {
            synchronized (JCFUserService.class) {
                if(instance == null) {
                    instance = new JCFChannelService();
                }
            }
        }
        return instance;
    }

    @Override
    public Channel createChannel(String name) {
        System.out.println(name + "채널을 생성합니다.");
        Channel channel = new Channel(name);
        System.out.println("채널: " + name + "생성 시간: " + channel.getCreatedAt());
        data.add(channel);
        return channel;
    }

    @Override
    public void viewAllChannels() {
        System.out.println("--- 전체 채널 조회 ---");
        data.stream()
                .forEach(channel -> {
                    System.out.println("채널: " + channel.getName());
                    System.out.print("참여 중인 사용자: " );
                    channel.getUsers().stream()
                            .forEach(user -> {
                                System.out.print(user.getName() + " ");
                            });
                    System.out.println();
                });
    }

    @Override
    public void viewChannelInfo(Channel channel) {
        if(data.contains(channel)){
            System.out.println("--- 채널 조회 ---");
            System.out.println("채널: " + channel.getName());
            System.out.print("참여 중인 사용자: " );
            channel.getUsers().stream()
                            .forEach(user -> {
                                System.out.print(user.getName() + " ");
                            });
            System.out.println();
            channel.getMessages().stream()
                    .forEach(message -> {
                        System.out.println(message.getSender().getName() + ": "+message.getContent());
                    });
        } else {
            System.out.println("존재하지 않는 채널입니다.");
        }
    }

    @Override
    public void updateChannelName(Channel channel, String name) {
        channel.updateName(name);
    }

    @Override
    public void deleteChannel(Channel channel) {
        if(data.contains(channel)){
            data.remove(channel);
            System.out.println(channel.getName()+" 채널이 삭제되었습니다.");
        } else {
            System.out.println("존재하지 않는 채널입니다.");
        }
    }

    @Override
    public void addUserIntoChannel(Channel channel, User user) {
        if(channel.getUsers().contains(user)){
            System.out.println(channel.getName() + " 채널에 이미 입장한 사용자입니다.");
        } else {
            channel.getUsers().add(user);
            user.getChannels().add(channel);
            System.out.println(user.getName() + "님이 채널 '"
                    + channel.getName() + "'에 입장합니다.");
        }
    }

    @Override
    public void deleteUserInChannel(Channel channel, User user) {
        if (channel.getUsers() == null || user.getChannels() == null) {
            System.out.println("올바르지 않은 데이터입니다.");
            return;
        }
        if(!channel.getUsers().contains(user) || !user.getChannels().contains(channel)){
            System.out.println(channel.getName() + " 채널에 없는 사용자입니다.");
        } else {
            channel.getUsers().remove(user);
            user.getChannels().remove(channel);
            System.out.println(user.getName() + "님이 채널 '"
                    + channel.getName() + "'에 퇴장합니다.");
        }
    }

    @Override
    public void deleteMessageInChannel(Message message) {
        Channel channel = message.getChannel();
        if(channel.getMessages().contains(message)) {
            channel.getMessages().remove(message);
        } else {
            System.out.println("존재하지 않는 메세지입니다.");
        }
    }

}
