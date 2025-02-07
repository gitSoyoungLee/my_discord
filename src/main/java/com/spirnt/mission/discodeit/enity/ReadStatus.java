package com.spirnt.mission.discodeit.enity;

import com.spirnt.mission.discodeit.dto.readStatus.ReadStatusDto;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

// 사용자가 채널 별 마지막으로 메시지를 읽은 시간을 표현하는 도메인
@Getter
public class ReadStatus extends Common {
    private final UUID userId;
    private final UUID channelId;
    private Instant lastReadAt;

    public ReadStatus(ReadStatusDto dto) {
        super();
        this.userId = dto.userId();
        this.channelId = dto.channelId();
        this.lastReadAt = dto.lastReadAt();
    }

    // 마지막으로 메세지 읽은 시간 업데이트
    public void update(Instant time) {
        this.lastReadAt = time;
        updateClass(time);
    }

    @Override
    public String toString() {
        return "ReadStatus[UserID: " + this.userId +
                " ChannelID: " + this.channelId +
                " LastReadAt: " + this.lastReadAt + "]";
    }
}
