package discodeit.service.file;

import discodeit.dto.ChannelDto;
import discodeit.enity.Channel;
import discodeit.enity.ChannelType;
import discodeit.enity.User;
import discodeit.repository.file.FileChannelRepository;
import discodeit.service.ChannelService;

import java.util.*;
import java.util.stream.Collectors;

public class FileChannelService implements ChannelService {
    //싱글톤
    private static volatile FileChannelService instance;
    private final FileChannelRepository fileChannelRepository;
    private FileUserService fileUserService;
    private FileMessageService fileMessageService;

    public FileChannelService() {
        this.fileChannelRepository = new FileChannelRepository();
    }

    public static FileChannelService getInstance() {
        if (instance == null) {
            synchronized (FileChannelService.class) {
                if (instance == null) {
                    instance = new FileChannelService();
                }
            }
        }
        return instance;
    }

    protected void setService() {
        this.fileUserService = fileUserService.getInstance();
        this.fileMessageService = fileMessageService.getInstance();
    }

    @Override
    public UUID createChannel(String name, String description, ChannelType type) {
        Channel channel = new Channel(name, description, type);
        fileChannelRepository.save(channel);
        System.out.println("채널: " + name + "(" + type + ")" + ", 생성 시간: " + channel.getCreatedAt());
        return channel.getId();
    }

    // 채널 이름, 설명, 채널 내 사용자, 채널 내 메세지 조회
    @Override
    public ChannelDto getChannelInfoById(UUID channelId) {
        Channel channel = findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel ID: " + channelId + " Not Found"));
        return new ChannelDto(channel);
    }

    // 채널별 이름, 설명, 채널 내 사용자 조회
    @Override
    public List<ChannelDto> getAllChannelsInfo() {
        Map<UUID, Channel> data = fileChannelRepository.findAll();
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
        fileChannelRepository.save(channel);
    }

    @Override
    public void updateChannelDescription(UUID channelId, String description) {
        Channel channel = findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel ID: " + channelId + " Not Found"));

        channel.updateDescription(description);
        fileChannelRepository.save(channel);
    }

    @Override
    public void deleteChannel(UUID channelId) {
        // 존재하는지 검증
        Channel channel= findById(channelId)
                .orElseThrow(()->new NoSuchElementException("Channel ID: " + channelId + " Not Found"));
        // 채널에 속해있던 메세지도 삭제
        fileMessageService.deleteMessagesInChannel(channelId);
        fileChannelRepository.delete(channelId);
    }

    @Override
    public void addUserIntoChannel(UUID channelId, UUID userId) {
        // 존재하는지 검증
        Channel channel = findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel ID: " + channelId + " Not Found"));
        User user = fileUserService.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User ID: " + userId + " Not Found"));

        if (channel.containsUser(userId)) {
            return;
        }
        channel.getUsers().add(userId);
        fileChannelRepository.save(channel);
    }

    @Override
    public void deleteUserInChannel(UUID channelId, UUID userId) {
        // 존재하는지 검증
        Channel channel = findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel ID: " + channelId + " Not Found"));
        User user = fileUserService.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User ID: " + userId + " Not Found"));

        if (!channel.containsUser(userId)) {
            return;
        }
        channel.getUsers().remove(userId);
        fileChannelRepository.save(channel);
    }

    @Override
    public void deleteUserInAllChannels(UUID userId) {
        Map<UUID, Channel> data = fileChannelRepository.findAll();
        if (data.isEmpty()) return;
        data.values().stream()
                .forEach(channel -> {
                    channel.getUsers().remove(userId);
                    // 채널 객체 수정 후 ser 파일에 반영
                    fileChannelRepository.save(channel);
                });
    }

    @Override
    public Optional<Channel> findById(UUID channelId) {
        return fileChannelRepository.findById(channelId);
    }

}
