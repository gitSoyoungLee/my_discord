package discodeit.service.jcf;

import discodeit.enity.Channel;
import discodeit.enity.Message;
import discodeit.enity.User;
import discodeit.repository.jcf.JCFChannelRepository;
import discodeit.repository.jcf.JCFMessageRepository;
import discodeit.repository.jcf.JCFUserRepository;
import discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {

    private static volatile JCFMessageService instance;
    private final JCFMessageRepository jcfMessageRepository;
    private JCFUserService jcfUserService;
    private JCFChannelService jcfChannelService;

    private JCFMessageService(JCFMessageRepository jcfMessageRepository) {
        this.jcfMessageRepository = jcfMessageRepository;
    }

    public static JCFMessageService getInstance(JCFMessageRepository jcfMessageRepository) {
        if (instance == null) {
            synchronized (JCFUserService.class) {
                if (instance == null) {
                    instance = new JCFMessageService(jcfMessageRepository);
                }
            }
        }
        return instance;
    }

    public void setService(JCFUserService jcfUserService, JCFChannelService jcfChannelService) {
        this.jcfUserService = jcfUserService;
        this.jcfChannelService = jcfChannelService;
    }

    @Override
    public UUID createMessage(UUID userId, UUID channelId, String content) {
        try {
            Channel channel = jcfChannelService.findById(channelId);
            User user = jcfUserService.findById(userId);

            if (!channel.getUsers().contains(user)) {
                System.out.println("메세지를 보낼 수 없습니다: " +
                        user.getName() + "은 아직 '" + channel.getName() + "' 채널에 입장하지 않았습니다.");
                return null;
            }

            Message message = new Message(userId, channelId, content);
            jcfMessageRepository.save(message);
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
            Channel channel = jcfChannelService.findById(message.getChannelId());
            User user = jcfUserService.findById(message.getSenderId());
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
        Map<UUID, Message> data = jcfMessageRepository.getData();
        data.entrySet().stream()
                .forEach(entry -> {
                    Message message = entry.getValue();
                    Channel channel = jcfChannelService.findById(message.getChannelId());
                    User user = jcfUserService.findById(message.getSenderId());

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
            if (message.getSenderId() != userId) {
                System.out.println("메세지 작성자만 수정 가능합니다.");
                return;
            }
            message.updateContent(newContent);
            System.out.println("메세지가 수정되었습니다.");
        } catch (NoSuchElementException e) {
            System.out.println("메세지 또는 사용자 데이터가 올바르지 않습니다. " + e.getMessage());
        }
    }

    @Override
    public void deleteMessage(UUID messageId) {
        System.out.print("메세지 삭제 요청: ");
        try {
            Message message = findById(messageId);
            Channel channel = jcfChannelService.findById(message.getChannelId());
            int idx = channel.getMessages().indexOf(message);
            channel.getMessages().remove(idx);
            jcfMessageRepository.delete(messageId);
            System.out.println("메세지가 삭제되었습니다.");
        } catch (NoSuchElementException e) {
            System.out.println("존재하지 않는 메세지입니다. " + e.getMessage());
        }
    }

    @Override
    public Message findById(UUID messageId) {
        return jcfMessageRepository.findById(messageId);
    }
}
