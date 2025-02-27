package com.spirnt.mission.discodeit.dto.channel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class PrivateChannelCreateRequest {

  private List<UUID> usersId;

  public PrivateChannelCreateRequest(List<UUID> usersId) {
    this.usersId = (usersId == null) ? new ArrayList<>() : usersId;
  }
}
