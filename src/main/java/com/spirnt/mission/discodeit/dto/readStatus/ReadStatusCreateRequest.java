package com.spirnt.mission.discodeit.dto.readStatus;

import java.util.UUID;

public record ReadStatusCreateRequest(
    UUID userId,
    UUID channelId
) {

}
