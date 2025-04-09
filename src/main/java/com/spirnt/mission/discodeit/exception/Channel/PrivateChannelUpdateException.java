package com.spirnt.mission.discodeit.exception.Channel;

import com.spirnt.mission.discodeit.exception.ErrorCode;
import java.time.Instant;
import java.util.Map;

/**
 * 수정할 수 없는 Private Channel을 수정하려고 시도한 경우 발생하는 예외
 */
public class PrivateChannelUpdateException extends ChannelException {

  public PrivateChannelUpdateException(Instant timestamp, Map<String, Object> details) {
    super(timestamp, ErrorCode.PRIVATE_CHANNEL_UPDATE, details);
  }
}
