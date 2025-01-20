package discodeit.enity;

import java.util.ArrayList;
import java.util.List;

public class Channel extends Common {
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

    //Update
    public void updateName(String name) {
        if(name == null) return;
        this.name = name;
        updateClass();
    }

    public void updateDescription(String description) {
        if(description.equals(this.description)) return;
        this.description = description;
        updateClass();
    }

}
