package com.spirnt.mission.discodeit.enity;

import com.spirnt.mission.discodeit.dto.user.UserCreateRequest;
import com.spirnt.mission.discodeit.dto.user.UserUpdateRequest;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Getter
public class User extends Common implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String email;

    private transient String password;

    public User(UserCreateRequest dto) {
        super();
        this.name = dto.getName();
        this.email = dto.getEmail();
        this.password = dto.getPassword();
    }


    // Update
    public void updateName(String name) {
        if (name == null || name.equals(this.name)) return;
        this.name = name;
        updateClass(Instant.now());
    }

    public void updateEmail(String email) {
        if (email == null || email.equals(this.email)) return;
        this.email = email;
        updateClass(Instant.now());
    }

    public void updatePassword(String password) {
        if (password == null || password.equals(this.password)) return;
        this.password = password;
        updateClass(Instant.now());
    }

    public void update(UserUpdateRequest dto) {
        boolean anyValueUpdated = false;
        if (dto.getName() != null && !dto.getName().equals(this.name)) {
            this.name = dto.getName();
            anyValueUpdated = true;
        }
        if (dto.getEmail() != null && !dto.getEmail().equals(this.email)) {
            this.email = dto.getEmail();
            anyValueUpdated = true;
        }
        if (dto.getPassword() != null && !dto.getPassword().equals(this.password)) {
            this.password = dto.getPassword();
            anyValueUpdated = true;
        }
        if (anyValueUpdated) {
            this.updateClass(Instant.now());
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + this.getId() + '\'' +
                ", name='" + this.name + '\'' +
                ", email='" + this.email + '\'' +
                '}';
    }

    // UUID만으로 객체를 비교하기 위해 추가
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return Objects.equals(this.getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

}
