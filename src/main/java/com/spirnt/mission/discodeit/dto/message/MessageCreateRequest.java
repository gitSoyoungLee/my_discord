package com.spirnt.mission.discodeit.dto.message;

import java.util.UUID;
import lombok.Getter;

@Getter
public class MessageCreateRequest {

  private String content;
  private UUID userId;
  private UUID channelId;

  public MessageCreateRequest(UUID userId, UUID channelId,
      String content) {
    this.content = content;
    this.userId = userId;
    this.channelId = channelId;
  }
}
