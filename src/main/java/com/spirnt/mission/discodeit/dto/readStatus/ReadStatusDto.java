package com.spirnt.mission.discodeit.dto.readStatus;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusDto(
        UUID userId,
        UUID channelId,
        Instant lastReadAt  // 유저가 채널에서 마지막으로 메세지를 읽은 시간
) {
}
