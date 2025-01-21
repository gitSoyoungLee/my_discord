package discodeit.service.file;

import discodeit.enity.Channel;
import discodeit.enity.ChannelType;
import discodeit.enity.User;
import discodeit.repository.file.FileChannelRepository;
import discodeit.service.ChannelService;

import java.io.File;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

public class FileChannelService implements ChannelService {
    private final FileChannelRepository fileChannelRepository;
    private FileUserService fileUserService;
    private FileMessageService fileMessageService;
    public FileChannelService() {
        this.fileChannelRepository = new FileChannelRepository();
    }

    public void setService(FileUserService fileUserService, FileMessageService fileMessageService) {
        this.fileUserService = fileUserService;
        this.fileMessageService = fileMessageService;
    }

    @Override
    public UUID createChannel(String name, String description, ChannelType type) {
        System.out.print("채널 생성 요청: ");

        Channel channel = new Channel(name, description, type);
        fileChannelRepository.save(channel);
        System.out.println("채널: " + name + "(" + type + ")" + ", 생성 시간: " + channel.getCreatedAt());
        return channel.getId();
    }

    @Override
    public void viewAllChannels() {
        System.out.println("--- 전체 채널 조회 ---");
        Map<UUID, Channel> channels = fileChannelRepository.findAll();
        channels.values().stream()
                .sorted(Comparator.comparing(channel -> channel.getCreatedAt()))
                .forEach(channel -> {
                    System.out.println("채널: " + channel.getName() +
                            " / 설명: " + channel.getDescription());
                    System.out.print("참여 중인 사용자: ");
                    channel.getUsers().stream()
                            .forEach(user -> {
                                System.out.print(user.getName() + " ");
                            });
                    System.out.println();
                });

    }

    @Override
    public void viewChannelInfo(UUID channelId) {
        System.out.println("--- 채널 조회 ---");
        try {
            Channel channel = findById(channelId);
            System.out.println("채널: " + channel.getName() +" / 설명: " +channel.getDescription());
            System.out.print("참여 중인 사용자: ");
            channel.getUsers().stream()
                    .forEach(user -> {
                        System.out.print(user.getName() + " ");
                    });
            System.out.println();

            //채널 내 메세지 출력
            if (channel.getMessages().isEmpty()) {
                System.out.println("현재 채널에 작성된 메세지가 없습니다. 채팅을 시작하세요.");
            } else {
                channel.getMessages().stream()
                        .forEach(message -> {
                            try {
                                User user = fileUserService.findById(message.getSenderId());
                                if (channel.getUsers().contains(user)) {
                                    System.out.println(user.getName() + ": " + message.getContent());
                                } else {
                                    System.out.println("(이 채널에 더 이상 없는 유저입니다.) " + user.getName() + ": " + message.getContent());
                                }
                            } catch (NoSuchElementException e) {
                                System.out.println("(알 수 없는 유저): " + message.getContent()); //탈퇴한 유저
                            }
                        });
            }
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
            fileChannelRepository.save(channel);
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
            fileChannelRepository.save(channel);
            System.out.println("채널 설명이 변경되었습니다.");
        } catch (NoSuchElementException e) {
            System.out.println("존재하지 않는 채널입니다. " + e.getMessage());
        }
    }
    @Override
    public void deleteChannel(UUID channelId) {
        System.out.print("채널 삭제 요청: ");
        try {
            fileChannelRepository.delete(channelId);
            System.out.println("채널이 삭제되었습니다.");
        } catch (NoSuchElementException e) {
            System.out.println("존재하지 않는 채널입니다. " + e.getMessage());
        }
    }

    @Override
    public void addUserIntoChannel(UUID channelId, UUID userId) {
        try {
            Channel channel = findById(channelId);
            User user = fileUserService.findById(userId);

            if (channel.getUsers().contains(user)) {
                System.out.println(channel.getName() + "은 채널에 이미 입장한 사용자입니다.");
            } else {
                channel.getUsers().add(user);
                fileUserService.addUserIntoChannel(channelId, userId);
                fileChannelRepository.save(channel);
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
            User user =fileUserService.findById(userId);

            System.out.println("유저: " +channel.getUsers());
            System.out.println("입력: "+user);
            if (!channel.getUsers().contains(user)) {
                System.out.println(user.getName() + "은 채널에 없는 사용자입니다.");
            } else {
                channel.getUsers().remove(user);
                fileUserService.deleteUserInChannel(channelId,userId);
                fileChannelRepository.save(channel);
                System.out.println(user.getName() + "님이 채널 '"
                        + channel.getName() + "'에서 퇴장합니다.");
            }
        } catch (NoSuchElementException e) {
            System.out.println("채널 또는 사용자 데이터가 올바르지 못합니다. " + e.getMessage());
        }
    }

    @Override
    public Channel findById(UUID channelId) {
        return fileChannelRepository.findById(channelId);
    }
}
