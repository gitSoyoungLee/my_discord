package com.spirnt.mission.discodeit.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;


@Schema(description = "수정할 User 정보")
public record UserUpdateRequest(
    @Size(min = 3, max = 20)
    String newUsername,
    @Email
    String newEmail,
    @Size(min = 5, max = 20)
    String newPassword
) {

}
