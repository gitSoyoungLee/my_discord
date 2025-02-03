package discodeit.service.jcf;

import discodeit.dto.ChannelDto;
import discodeit.enity.Channel;
import discodeit.enity.ChannelType;
import discodeit.enity.User;
import discodeit.repository.jcf.JCFChannelRepository;
import discodeit.service.ChannelService;

import java.util.*;
import java.util.stream.Collectors;

public class JCFChannelService implements ChannelService {

    private static volatile JCFChannelService instance;
    private final JCFChannelRepository jcfChannelRepository;

    private JCFUserService jcfUserService;
    private JCFMessageService jcfMessageService;

    private JCFChannelService() {
        this.jcfChannelRepository = new JCFChannelRepository();

    }

    public static JCFChannelService getInstance() {
        if (instance == null) {
            synchronized (JCFUserService.class) {
                if (instance == null) {
                    instance = new JCFChannelService();
                }
            }
        }
        return instance;
    }

    protected void setService() {
        this.jcfUserService = jcfUserService.getInstance();
        this.jcfMessageService = jcfMessageService.getInstance();
    }


    @Override
    public UUID createChannel(String name, String description, ChannelType type) {
        Channel channel = new Channel(name, description, type);
        jcfChannelRepository.save(channel);
        System.out.println("채널: " + name + "(" + type + ")" + ", 생성 시간: " + channel.getCreatedAt());
        return channel.getId();
    }

    // 채널 이름, 설명, 채널 내 사용자, 채널 내 메세지 조회
    @Override
    public ChannelDto getChannelInfoById(UUID channelId) {
        try {
            Channel channel = findById(channelId);
            // 채널에 참여한 사용자 이름
            List<String> userNames = new ArrayList<>();
            for (UUID userId : channel.getUsers()) {
                userNames.add((jcfUserService.findById(userId).getName()));
            }
            // 채널 내 메세지
            List<String> formattedMessages = jcfMessageService.getMessagesByChannelId(channelId);

            return new ChannelDto(channel);
        } catch (NoSuchElementException e) {
            System.out.println("존재하지 않는 채널입니다. " + e.getMessage());
            return null;
        }
    }

    // 채널별 이름, 설명, 채널 내 사용자 조회
    @Override
    public List<ChannelDto> getAllChannelsInfo() {
        Map<UUID, Channel> data = jcfChannelRepository.findAll();
        if (data.isEmpty()) {
            System.out.println("채널이 없습니다.");
            return null;
        }
        List<ChannelDto> list = new ArrayList<>();
        data.values().stream()
                .sorted(Comparator.comparing(channel -> channel.getCreatedAt()))
                .forEach(channel -> {
                    // 채널에 참여한 사용자 이름
                    List<String> userNames = new ArrayList<>();
                    for (UUID userId : channel.getUsers()) {
                        userNames.add((jcfUserService.findById(userId).getName()));
                    }
                    list.add(new ChannelDto(channel));  // 메세지는 출력 x
                });
        return list;
    }


    @Override
    public void updateChannelName(UUID channelId, String name) {
        try {
            Channel channel = findById(channelId);
            String prevName = channel.getName();
            channel.updateName(name);
            System.out.println("채널 '" + prevName + "'이 '" + name + "'으로 변경되었습니다.");
        } catch (NoSuchElementException e) {
            System.out.println("존재하지 않는 채널입니다. " + e.getMessage());
        }
    }

    @Override
    public void updateChannelDescription(UUID channelId, String description) {
        try {
            Channel channel = findById(channelId);
            channel.updateDescription(description);
            System.out.println("채널 설명이 변경되었습니다.");
        } catch (NoSuchElementException e) {
            System.out.println("존재하지 않는 채널입니다. " + e.getMessage());
        }
    }

    @Override
    public void deleteChannel(UUID channelId) {
        try {
            // 채널에 속해있던 메세지도 삭제
            jcfMessageService.deleteMessagesInChannel(channelId);
            jcfChannelRepository.delete(channelId);
            System.out.println("채널이 삭제되었습니다.");
        } catch (NoSuchElementException e) {
            System.out.println("존재하지 않는 채널입니다. " + e.getMessage());
        }
    }

    @Override
    public void addUserIntoChannel(UUID channelId, UUID userId) {
        try {
            Channel channel = findById(channelId);
            User user = jcfUserService.findById(userId);

            if (channel.containsUser(userId)) {
                System.out.println(channel.getName() + "은 채널에 이미 입장한 사용자입니다.");
            } else {
                channel.getUsers().add(userId);
                System.out.println(user.getName() + "님이 채널 '"
                        + channel.getName() + "'에 입장합니다.");
            }
        } catch (NoSuchElementException e) {
            System.out.println("채널 또는 사용자 데이터가 올바르지 못합니다. " + e.getMessage());
        }
    }

    @Override
    public void deleteUserInChannel(UUID channelId, UUID userId) {
        try {
            Channel channel = findById(channelId);
            User user = jcfUserService.findById(userId);

            if (!channel.containsUser(userId)) {
                System.out.println(user.getName() + "은 채널에 없는 사용자입니다.");
            } else {
                channel.getUsers().remove(userId);
                System.out.println(user.getName() + "님이 채널 '"
                        + channel.getName() + "'에서 퇴장합니다.");
            }
        } catch (NoSuchElementException e) {
            System.out.println("채널 또는 사용자 데이터가 올바르지 못합니다. " + e.getMessage());
        }
    }

    @Override
    public void deleteUserInAllChannels(UUID userId) {
        Map<UUID, Channel> data = jcfChannelRepository.findAll();
        data.values().stream()
                .forEach(channel -> {
                    channel.getUsers().remove(userId);
                });
    }

    @Override
    public Channel findById(UUID channelId) {
        return jcfChannelRepository.findById(channelId);
    }

    // 현재 userId가 소속된 채널 목록을 조회
    @Override
    public List<Channel> getChannelsByUserId(UUID userId) {
        Map<UUID, Channel> data = jcfChannelRepository.findAll();
        List<Channel> channels = data.values().stream()
                .filter(channel -> channel.getUsers().contains(userId))
                .collect(Collectors.toList());
        return channels;
    }



}
