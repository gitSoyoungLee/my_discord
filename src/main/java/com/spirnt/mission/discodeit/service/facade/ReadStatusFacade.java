package com.spirnt.mission.discodeit.service.facade;

import com.spirnt.mission.discodeit.service.ChannelService;
import com.spirnt.mission.discodeit.service.ReadStatusService;
import com.spirnt.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadStatusFacade {
    @Autowired
    private final ReadStatusService readStatusService;
    @Autowired
    private final UserService userService;
    @Autowired
    private final ChannelService channelService;
}
