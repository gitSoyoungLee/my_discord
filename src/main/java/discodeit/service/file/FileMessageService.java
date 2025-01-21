package discodeit.service.file;

import discodeit.enity.Channel;
import discodeit.enity.Message;
import discodeit.enity.User;
import discodeit.repository.file.FileChannelRepository;
import discodeit.repository.file.FileMessageRepository;
import discodeit.service.MessageService;

import java.io.File;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

public class FileMessageService implements MessageService {
    private final FileMessageRepository fileMessageRepository;
    private FileUserService fileUserService;
    private FileChannelService fileChannelService;
    public FileMessageService() {
        this.fileMessageRepository = new FileMessageRepository();
    }
    public void setService(FileUserService fileUserService, FileChannelService fileChannelService) {
        this.fileUserService = fileUserService;
        this.fileChannelService = fileChannelService;
    }

    @Override
    public UUID createMessage(UUID userId, UUID channelId, String content) {
        try {
            Channel channel = fileChannelService.findById(channelId);
            User user = fileUserService.findById(userId);

            if (!channel.getUsers().contains(user)) {
                System.out.println("메세지를 보낼 수 없습니다: " +
                        user.getName() + "은 아직 '" + channel.getName() + "' 채널에 입장하지 않았습니다.");
                return null;
            }

            Message message = new Message(userId, channelId, content);
            fileMessageRepository.save(message);
            channel.getMessages().add(message);
            System.out.println("메세지 생성 완료");
            return message.getId();
        } catch (NoSuchElementException e) {
            System.out.println("채널 또는 사용자 데이터가 올바르지 않습니다. " + e.getMessage());
            return null;
        }
    }

    @Override
    public void viewMessage(UUID messageId) {
        try {
            Message message = findById(messageId);
            Channel channel = fileChannelService.findById(message.getChannelId());
            User user = fileUserService.findById(message.getSenderId());
            System.out.println(channel.getName() + " > " +
                    user.getName() + ": " +
                    message.getContent()
                    + " (시간: " + message.getUpdatedAt() + ")");
        } catch (NoSuchElementException e) {
            System.out.println("존재하지 않는 메세지입니다. " + e.getMessage());
        }
    }

    @Override
    public void viewAllMessages() {
        System.out.println("--- 디스코드잇에서 생성된 모든 메세지 ---");
        Map<UUID, Message> data = fileMessageRepository.findAll();
        data.entrySet().stream()
                .forEach(entry -> {
                    Message message = entry.getValue();
                    Channel channel = fileChannelService.findById(message.getChannelId());
                    User user = fileUserService.findById(message.getSenderId());

                    String time = message.getCreatedAt().equals(message.getUpdatedAt())
                            ? String.valueOf(message.getCreatedAt())
                            : message.getUpdatedAt() + " 수정";

                    System.out.println("채널 '" + channel.getName() + "' > "
                            + user.getName() + ": "
                            + message.getContent()
                            + " (time: " + time + ")");
                });
    }

    @Override
    public void updateMessage(UUID userId, UUID messageId, String newContent) {
        System.out.print("메세지 수정 요청: ");
        try {
            Message message = findById(messageId);

            // 메시지 작성자인 경우에만 수정 가능
            if (!message.getSenderId().equals(userId)) {
                System.out.println("메세지 작성자만 수정 가능합니다.");
                return;
            }
            message.updateContent(newContent);
            fileMessageRepository.save(message);
            System.out.println("메세지가 수정되었습니다.");
        } catch (NoSuchElementException e) {
            System.out.println("메세지 또는 사용자 데이터가 올바르지 않습니다. " + e.getMessage());
        }
    }

    @Override
    public void deleteMessage(UUID messageId) {
        System.out.print("메세지 삭제 요청: ");
        try {
            fileMessageRepository.delete(messageId);
            System.out.println("메세지가 삭제되었습니다.");
        } catch (NoSuchElementException e) {
            System.out.println("존재하지 않는 메세지입니다. " + e.getMessage());
        }
    }

    @Override
    public Message findById(UUID messageId) {
        return fileMessageRepository.findById(messageId);
    }
}
