package com.spirnt.mission.discodeit.dto.binaryContent;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record BinaryContentCreate(
        MultipartFile file
) {
}
