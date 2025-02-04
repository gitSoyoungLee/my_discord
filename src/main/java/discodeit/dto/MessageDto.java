package discodeit.dto;

import discodeit.enity.Message;

public class MessageDto {
    private Message message;

    public MessageDto(Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message.getContent() +
                (message.getCreatedAt() == message.getUpdatedAt()
                        ? " (time: " + message.getCreatedAt() + ")"
                        : " (time: " + message.getUpdatedAt() + " (수정됨))");
    }
}
