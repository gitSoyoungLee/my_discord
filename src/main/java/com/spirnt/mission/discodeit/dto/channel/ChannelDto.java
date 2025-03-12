package com.spirnt.mission.discodeit.dto.channel;

import com.spirnt.mission.discodeit.enity.Channel;
import com.spirnt.mission.discodeit.enity.ChannelType;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChannelDto {

  private UUID id;
  private String name;
  private String description;
  private ChannelType type;
  private List<UUID> participantIds;
  private Instant lastMessageAt;

  @Builder
  public ChannelDto(UUID id, String name, String description, ChannelType type,
      List<UUID> participantIds, Instant lastMessageAt) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.type = type;
    this.participantIds = (participantIds == null) ? new ArrayList<>() : participantIds;
    this.lastMessageAt = lastMessageAt;
  }

  public static ChannelDto of(Channel channel, List<UUID> participantIds, Instant lastMessageAt) {
    return ChannelDto.builder()
        .id(channel.getId())
        .name(channel.getName())
        .description(channel.getDescription())
        .type(channel.getType())
        .participantIds(participantIds)
        .lastMessageAt(lastMessageAt)
        .build();
  }
}
