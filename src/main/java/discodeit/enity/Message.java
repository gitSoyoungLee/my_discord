package discodeit.enity;

import java.util.UUID;

public class Message extends Common {
    private String content;
    private UUID senderId;
    private UUID channelId;

    public Message() {
        super();
    }

    public Message(UUID senderId, UUID channelId, String content) {
        super();
        this.content = content;
        this.senderId = senderId;
        this.channelId = channelId;
    }

    //Getter
    public String getContent() {
        return content;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public UUID getChannelId() {
        return channelId;
    }

    // Update
    public void updateContent(String content) {
        if(content == null || content.equals(this.content)) return;
        this.content = content;
        updateClass();
    }
}
