package discodeit.service.jcf;

import discodeit.enity.Channel;
import discodeit.enity.ChannelType;
import discodeit.enity.Message;
import discodeit.enity.User;
import discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {

    private static volatile JCFMessageService instance;
    private final Map<UUID, Message> data;
    private JCFUserService jcfUserService;
    private JCFChannelService jcfChannelService;

    private JCFMessageService() {
        this.data = new HashMap<>();
    }

    public static JCFMessageService getInstance() {
        if (instance == null) {
            synchronized (JCFUserService.class) {
                if (instance == null) {
                    instance = new JCFMessageService();
                }
            }
        }
        return instance;
    }

    public void setService() {
        this.jcfUserService = jcfUserService.getInstance();
        this.jcfChannelService = jcfChannelService.getInstance();
    }

    public Map<UUID, Message> getData() {
        return new HashMap<>(data);
    }

    @Override
    public UUID createMessage(UUID userId, UUID channelId, String content) {
        try {
            Channel channel = jcfChannelService.findChannel(channelId);
            User user = jcfUserService.findUser(userId);

            if (!channel.getUsers().contains(user)) {
                System.out.println("메세지를 보낼 수 없습니다: " +
                        user.getName() + "은 아직 '" + channel.getName() + "' 채널에 입장하지 않았습니다.");
                return null;
            }

            Message message = new Message(userId, channelId, content);
            data.put(message.getId(), message);
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
            Message message = findMessage(messageId);
            Channel channel = jcfChannelService.getData().get(message.getChannelId());
            User user = jcfUserService.getData().get(message.getSenderId());
            System.out.println(channel.getName() + " > " +
                    user.getName() + ": " +
                    message.getContent()
                    + " (시간: " + message.getUpdatedAt() + ")");
        } catch (NoSuchElementException e) {
            System.out.println("존재하지 않는 메세지입니다. " +e.getMessage());
        }
    }

    @Override
    public void viewAllMessages() {
        System.out.println("--- 디스코드잇에서 생성된 모든 메세지 ---");
        data.entrySet().stream()
                .forEach(entry -> {
                    Message message = entry.getValue();
                    Channel channel = jcfChannelService.getData().get(entry.getValue().getChannelId());
                    User user = jcfUserService.getData().get(entry.getValue().getSenderId());

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
            Message message = findMessage(messageId);

            // 메시지 작성자인 경우에만 수정 가능
            if (message.getSenderId() != userId) {
                System.out.println("메세지 작성자만 수정 가능합니다.");
                return;
            }
            message.updateContent(newContent);
            System.out.println("메세지가 수정되었습니다.");
        } catch (NoSuchElementException e) {
            System.out.println("메세지 또는 사용자 데이터가 올바르지 않습니다. " +e.getMessage());
        }
    }

    @Override
    public void deleteMessage(UUID messageId) {
        System.out.print("메세지 삭제 요청: ");
        try {
            // 해당 채널의 메세지 리스트에서 삭제
            Message message = findMessage(messageId);
            Channel channel = jcfChannelService.findChannel(message.getChannelId());
            int idx = channel.getMessages().indexOf(message);
            channel.getMessages().remove(idx);
            data.remove(messageId);
            System.out.println("Message ID: " + message.getId() + "가 삭제됩니다.");
        } catch (NoSuchElementException e) {
            System.out.println("존재하지 않는 메세지입니다. " + e.getMessage());
        }
    }

    @Override
    public Message findMessage(UUID messageId) {
        Message message = this.data.get(messageId);
        return Optional.ofNullable(message)
                .orElseThrow(() -> new NoSuchElementException("Message ID: "+messageId+" not found"));
    }
}
