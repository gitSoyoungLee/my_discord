package discodeit.enity;

import java.util.ArrayList;
import java.util.List;

public class User extends Common {

    private String name;
    private String email;

    private String password;
    private List<Channel> channels;

    public User() {
        super();
    }

    public User(String name, String email, String password) {
        super();
        this.name = name;
        this.email = email;
        this.password = password;
        channels = new ArrayList<>();
    }

    //Getter
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    // Update
    public void updateName(String name) {
        if(name == null || name.equals(this.name)) return;
        this.name = name;
        updateClass();
    }

    public void updateEmail(String email) {
        if(email == null || email.equals(this.email)) return;
        this.email = email;
        updateClass();
    }

    public void updatePassword(String password) {
        if(password == null || password.equals(this.password)) return;
        this.password = password;
        updateClass();
    }
}
