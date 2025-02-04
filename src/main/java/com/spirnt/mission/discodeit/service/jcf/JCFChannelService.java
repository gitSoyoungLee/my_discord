package com.spirnt.mission.discodeit.service.jcf;

import com.spirnt.mission.discodeit.dto.ChannelDto;
import com.spirnt.mission.discodeit.enity.Channel;
import com.spirnt.mission.discodeit.enity.ChannelType;
import com.spirnt.mission.discodeit.enity.User;
import com.spirnt.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.spirnt.mission.discodeit.service.ChannelService;

import java.util.*;

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

    @Override
    public ChannelDto getChannelInfoById(UUID channelId) {
        Channel channel = findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel ID: " + channelId + " Not Found"));
        return new ChannelDto(channel);
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
                    list.add(new ChannelDto(channel));
                });
        return list;
    }


    @Override
    public void updateChannelName(UUID channelId, String name) {
        Channel channel = findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel ID: " + channelId + " Not Found"));
        channel.updateName(name);
    }

    @Override
    public void updateChannelDescription(UUID channelId, String description) {
        Channel channel = findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel ID: " + channelId + " Not Found"));

        channel.updateDescription(description);
    }

    @Override
    public void deleteChannel(UUID channelId) {
        // 존재하는지 검증
        Channel channel = findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel ID: " + channelId + " Not Found"));
        // 채널에 속해있던 메세지도 삭제
        jcfMessageService.deleteMessagesInChannel(channelId);
        jcfChannelRepository.delete(channelId);
    }

    @Override
    public void addUserIntoChannel(UUID channelId, UUID userId) {
        // 존재하는지 검증
        Channel channel = findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel ID: " + channelId + " Not Found"));
        User user = jcfUserService.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User ID: " + userId + " Not Found"));

        if (channel.containsUser(userId)) {
            return;
        }
        channel.getUsers().add(userId);
        return;
    }

    @Override
    public void deleteUserInChannel(UUID channelId, UUID userId) {
        // 존재하는지 검증
        Channel channel = findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel ID: " + channelId + " Not Found"));
        User user = jcfUserService.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User ID: " + userId + " Not Found"));
        if (!channel.containsUser(userId)) {
            System.out.println(user.getName() + "은 채널에 없는 사용자입니다.");
            return;
        }
        channel.getUsers().remove(userId);
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
    public Optional<Channel> findById(UUID channelId) {
        return jcfChannelRepository.findById(channelId);
    }

}
