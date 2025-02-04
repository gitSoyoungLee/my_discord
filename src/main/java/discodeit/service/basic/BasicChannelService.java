package discodeit.service.basic;

import discodeit.dto.ChannelDto;
import discodeit.enity.Channel;
import discodeit.enity.ChannelType;
import discodeit.enity.User;
import discodeit.repository.ChannelRepository;
import discodeit.service.ChannelService;
import discodeit.service.MessageService;
import discodeit.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

public class BasicChannelService implements ChannelService {
    private ChannelRepository channelRepository;
    private UserService userService;
    private MessageService messageService;


    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    public void setService(UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }

    @Override
    public UUID createChannel(String name, String description, ChannelType type) {
        Channel channel = new Channel(name, description, type);
        channelRepository.save(channel);
        System.out.println("채널: " + name + "(" + type + ")" + ", 생성 시간: " + channel.getCreatedAt());
        return channel.getId();
    }

    @Override
    public ChannelDto getChannelInfoById(UUID channelId) {
        Channel channel = findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel ID: " + channelId + " Not Found"));
        return new ChannelDto(channel);
    }

    @Override
    public List<ChannelDto> getAllChannelsInfo() {
        Map<UUID, Channel> data = channelRepository.findAll();
        List<ChannelDto> list = new ArrayList<>();
        if(data.isEmpty()) return list;
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
        channelRepository.save(channel);
    }

    @Override
    public void updateChannelDescription(UUID channelId, String description) {
        Channel channel = findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel ID: " + channelId + " Not Found"));

        channel.updateDescription(description);
        channelRepository.save(channel);
    }

    @Override
    public void deleteChannel(UUID channelId) {
        // 존재하는지 검증
        Channel channel= findById(channelId)
                .orElseThrow(()->new NoSuchElementException("Channel ID: " + channelId + " Not Found"));
        // 채널에 속해있던 메세지도 삭제
        messageService.deleteMessagesInChannel(channelId);
        channelRepository.delete(channelId);
    }

    @Override
    public void addUserIntoChannel(UUID channelId, UUID userId) {
        // 존재하는지 검증
        Channel channel = findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel ID: " + channelId + " Not Found"));
        User user = userService.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User ID: " + userId + " Not Found"));
        // 유저가 채널에 이미 입장한 경우 조건 불만족으로 실패
        if (channel.containsUser(userId)) {
            return;
        }
        channel.getUsers().add(userId);
        channelRepository.save(channel);
    }

    @Override
    public void deleteUserInChannel(UUID channelId, UUID userId) {
        // 존재하는지 검증
        Channel channel = findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel ID: " + channelId + " Not Found"));
        User user = userService.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User ID: " + userId + " Not Found"));
        // 유저가 채널에 없는 경우 조건 불만족으로 실패
        if (!channel.containsUser(userId)) {
            return;
        }
        channel.getUsers().remove(userId);
        channelRepository.save(channel);
    }

    @Override
    public void deleteUserInAllChannels(UUID userId) {
        Map<UUID, Channel> data = channelRepository.findAll();
        if (data.isEmpty()) return;
        data.values().stream()
                .forEach(channel -> {
                    channel.getUsers().remove(userId);
                    // 채널 객체 수정 후 ser 파일에 반영
                    channelRepository.save(channel);
                });
    }

    @Override
    public Optional<Channel> findById(UUID channelId) {
        return channelRepository.findById(channelId);
    }


}
