package discodeit.service.basic;

import discodeit.enity.Channel;
import discodeit.enity.ChannelType;
import discodeit.enity.User;
import discodeit.repository.ChannelRepository;
import discodeit.service.ChannelService;

import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

public class BasicChannelService implements ChannelService {
    private ChannelRepository channelRepository;
    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }
    @Override
    public UUID createChannel(String name, String description, ChannelType type) {
        System.out.print("채널 생성 요청: ");

        Channel channel = new Channel(name, description, type);
        channelRepository.save(channel);
        System.out.println("채널: " + name + "(" + type + ")" + ", 생성 시간: " + channel.getCreatedAt());
        return channel.getId();
    }

    @Override
    public void viewAllChannels() {
        System.out.println("--- 전체 채널 조회 ---");
        Map<UUID, Channel> channels = channelRepository.findAll();
        channels.values().stream()
                .sorted(Comparator.comparing(channel -> channel.getCreatedAt()))
                .forEach(channel -> {
                    System.out.println("채널: " + channel.getName() +
                            " / 설명: " + channel.getDescription());
                });
    }

    @Override
    public void viewChannelInfo(UUID channelId) {
        System.out.println("--- 채널 조회 ---");
        try {
            Channel channel = findById(channelId);
            System.out.println("채널: " + channel.getName() +" / 설명: " +channel.getDescription());

        } catch (NoSuchElementException e) {
            System.out.println("존재하지 않는 채널입니다. " + e.getMessage());
        }
    }

    @Override
    public void updateChannelName(UUID channelId, String name) {
        System.out.print("채널 수정 요청: ");
        try {
            Channel channel = findById(channelId);
            String prevName = channel.getName();
            channel.updateName(name);
            channelRepository.save(channel);
            System.out.println("채널 '" + prevName + "'이 '" + name + "'으로 변경되었습니다.");
        } catch (NoSuchElementException e) {
            System.out.println("존재하지 않는 채널입니다. " + e.getMessage());
        }
    }

    @Override
    public void updateChannelDescription(UUID channelId, String description) {
        System.out.print("채널 수정 요청: ");
        try {
            Channel channel = findById(channelId);
            channel.updateDescription(description);
            channelRepository.save(channel);
            System.out.println("채널 설명이 변경되었습니다.");
        } catch (NoSuchElementException e) {
            System.out.println("존재하지 않는 채널입니다. " + e.getMessage());
        }
    }

    @Override
    public void deleteChannel(UUID channelId) {
        System.out.print("채널 삭제 요청: ");
        try {
            channelRepository.delete(channelId);
            System.out.println("채널이 삭제되었습니다.");
        } catch (NoSuchElementException e) {
            System.out.println("존재하지 않는 채널입니다. " + e.getMessage());
        }
    }

    //skip
    @Override
    public void addUserIntoChannel(UUID channelId, UUID userId) {

    }

    @Override
    public void deleteUserInChannel(UUID channelId, UUID userId) {

    }

    @Override
    public Channel findById(UUID channelId) {
        return channelRepository.findById(channelId);
    }
}
