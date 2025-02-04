package com.spirnt.mission.discodeit.enity;

import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Getter
public abstract class Common implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    public Common() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
    }



    //Update
    public void updateClass(Long updateTime) {
        this.updatedAt = updateTime;
    }
}
