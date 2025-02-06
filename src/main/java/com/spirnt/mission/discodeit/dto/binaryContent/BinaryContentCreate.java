package com.spirnt.mission.discodeit.dto.binaryContent;

import java.util.UUID;

public record BinaryContentCreate(
        UUID userId,
        UUID messageId,
        String filePath
) {
}
