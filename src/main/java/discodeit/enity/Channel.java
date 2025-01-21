package discodeit.enity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Channel extends Common implements Serializable {
    private static final long serialVersionUID = 1L;
    String name;
    String description;

    ChannelType type;
    List<User> users;
    List<Message> messages;

    public Channel() {
        super();
    }

    public Channel(String name, String description, ChannelType type) {
        super();
        this.name = name;
        this.description = description;
        this.type = type;
        users = new ArrayList<>();
        messages = new ArrayList<>();
    }


    //Getter
    public String getName() {
        return name;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public String getDescription() { return description; }

    //Update
    public void updateName(String name) {
        if (name == null) return;
        this.name = name;
        updateClass();
    }

    public void updateDescription(String description) {
        if (description.equals(this.description)) return;
        this.description = description;
        updateClass();
    }

    // 객체를 UUID로 비교하기 위해
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Channel channel = (Channel) obj;
        return Objects.equals(this.getId(), channel.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

}
