package discodeit.service.basic;

import discodeit.enity.Channel;
import discodeit.enity.Message;
import discodeit.enity.User;
import discodeit.repository.MessageRepository;
import discodeit.service.MessageService;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

public class BasicMessageService implements MessageService {
    private MessageRepository messageRepository;
    public BasicMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }
    @Override
    public UUID createMessage(UUID userId, UUID channelId, String content) {
        Message message = new Message(userId, channelId, content);
        messageRepository.save(message);
        System.out.println("메세지 생성 완료");
        return message.getId();
    }

    @Override
    public void viewMessage(UUID messageId) {
        try {
            Message message = findById(messageId);
            System.out.println(message.getContent()
                    + " (time: " + message.getUpdatedAt() + ")");
        } catch (NoSuchElementException e) {
            System.out.println("존재하지 않는 메세지입니다. " + e.getMessage());
        }
    }

    @Override
    public void viewAllMessages() {
        System.out.println("--- 디스코드잇에서 생성된 모든 메세지 ---");
        Map<UUID, Message> data = messageRepository.findAll();
        data.entrySet().stream()
                .forEach(entry -> {
                    Message message = entry.getValue();

                    String time = message.getCreatedAt().equals(message.getUpdatedAt())
                            ? String.valueOf(message.getCreatedAt())
                            : message.getUpdatedAt() + " 수정";

                    System.out.println(message.getContent()
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
            messageRepository.save(message);
            System.out.println("메세지가 수정되었습니다.");
        } catch (NoSuchElementException e) {
            System.out.println("메세지 또는 사용자 데이터가 올바르지 않습니다. " + e.getMessage());
        }
    }

    @Override
    public void deleteMessage(UUID messageId) {
        System.out.print("메세지 삭제 요청: ");
        try {
            messageRepository.delete(messageId);
            System.out.println("메세지가 삭제되었습니다.");
        } catch (NoSuchElementException e) {
            System.out.println("존재하지 않는 메세지입니다. " + e.getMessage());
        }
    }

    @Override
    public Message findById(UUID messageId) {
        return messageRepository.findById(messageId);
    }
}
