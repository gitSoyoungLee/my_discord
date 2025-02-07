package com.spirnt.mission.discodeit.enity;

import com.spirnt.mission.discodeit.dto.channel.ChannelCreateRequest;
import com.spirnt.mission.discodeit.dto.channel.ChannelUpdateRequest;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
public class Channel extends Common implements Serializable {
    private static final long serialVersionUID = 1L;
    String name;
    String description;
    ChannelType type;
    List<UUID> usersId;


    public Channel(ChannelCreateRequest dto, ChannelType type) {
        super();
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.type = type;
        if (dto.getUsersId() != null) this.usersId = dto.getUsersId();
        else this.usersId = new ArrayList<>();
    }


    public void update(ChannelUpdateRequest dto) {
        boolean anyValueUpdated = false;
        if (dto.getName() != null && !dto.getName().equals(this.name)) {
            this.name = dto.getName();
            anyValueUpdated = true;
        }
        if (dto.getDescription() != null && !dto.getDescription().equals(this.description)) {
            this.description = dto.getDescription();
            anyValueUpdated = true;
        }
        if (anyValueUpdated) {
            this.updateClass(Instant.now());
        }
    }

    public boolean containsUser(UUID userID) {
        return this.usersId.contains(userID);
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id='" + this.getId() + '\'' +
                ", name='" + this.name + '\'' +
                '}';
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
