package com.spirnt.mission.discodeit.exception.Channel;

import com.spirnt.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * ID로 조회했는데 Channel이 존재하지 않는 경우 발생하는 예외
 */
public class ChannelNotFoundException extends ChannelException {

  public ChannelNotFoundException(Instant timestamp,
      UUID channelId) {
    super(timestamp, ErrorCode.CHANNEL_NOT_FOUND, Map.of("id", channelId));
  }
}
