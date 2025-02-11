package com.spirnt.mission.discodeit.enity;

import com.spirnt.mission.discodeit.dto.userStatus.UserStatusCreate;
import com.spirnt.mission.discodeit.dto.userStatus.UserStatusUpdate;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Getter
public class UserStatus extends Common implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID userId;
    private UserStatusType userStatusType;
    private Instant lastSeenAt; // 마지막으로 확인된 접속 시간

    public UserStatus(UserStatusCreate dto) {
        super();
        this.userId = dto.userId();
        this.userStatusType = dto.type();
        this.lastSeenAt = dto.lastSeenAt();
    }

    public boolean isOnline() {
        if (lastSeenAt == null) {
            return false; // 접속한 적이 없으면 오프라인으로 간주
        }
        Instant fiveMinutesAgo = Instant.now().minus(5, ChronoUnit.MINUTES);
        return lastSeenAt.isAfter(fiveMinutesAgo); // 5분 이내 접속이면 온라인
    }

    public void update(UserStatusUpdate dto) {
        // 마지막 접속 시간 업데이트
        this.lastSeenAt = dto.lastSennAt();
        // 타입이 지정되어 있지 않은 경우 온라인 여부 확인 후 상태 지정
        if (dto.type() == null && isOnline()) {
            this.userStatusType = UserStatusType.ONLINE;
        } else if (dto.type() == null && !isOnline()) {
            this.userStatusType = UserStatusType.OFFLINE;
        } else this.userStatusType = dto.type();
        this.updateClass(dto.lastSennAt());
    }

    @Override
    public String toString() {
        return "UserStatus[User: " + this.userId +
                "(" + userStatusType + ")" +
                " LastSeenAt: " + lastSeenAt + "]";
    }

}
