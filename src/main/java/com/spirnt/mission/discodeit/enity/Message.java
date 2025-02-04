package com.spirnt.mission.discodeit.enity;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Message extends Common implements Serializable {
    private static final long serialVersionUID = 1L;
    private String content;
    private UUID senderId;
    private UUID channelId;


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
        if (content == null || content.equals(this.content)) return;
        this.content = content;
        updateClass(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + this.getId() + '\'' +
                ", content='" + this.content + '\'' +
                '}';
    }

    // 객체를 UUID로 비교하기 위해
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Message message = (Message) obj;
        return Objects.equals(this.getId(), message.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

}
