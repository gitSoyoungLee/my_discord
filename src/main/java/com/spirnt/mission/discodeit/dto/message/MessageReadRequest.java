package com.spirnt.mission.discodeit.dto.message;

import java.util.UUID;

public record MessageReadRequest(
        UUID userId
) {
}
