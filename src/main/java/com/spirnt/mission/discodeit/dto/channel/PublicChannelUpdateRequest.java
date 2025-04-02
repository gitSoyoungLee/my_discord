package com.spirnt.mission.discodeit.dto.channel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "수정할 Channel 정보")
public record PublicChannelUpdateRequest(
    @Size(max = 20) String newName,
    @Size(max = 20) String newDescription
) {


}
