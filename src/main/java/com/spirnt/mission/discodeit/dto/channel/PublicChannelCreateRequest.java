package com.spirnt.mission.discodeit.dto.channel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Public Channel 생성 정보")
public record PublicChannelCreateRequest(
    @NotNull @Size(max = 20) String name,
    @Size(max = 20) String description
) {

}
