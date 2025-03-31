package com.spirnt.mission.discodeit;

import com.spirnt.mission.discodeit.repository.ChannelRepository;
import com.spirnt.mission.discodeit.repository.UserRepository;
import com.spirnt.mission.discodeit.service.MessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DiscodeitApplicationTests {

  @Autowired
  private MessageService messageService;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private ChannelRepository channelRepository;

  @Test
  void contextLoads() {
  }

//  @Test
//  void pagingTest() {
//    for (int i = 0; i < 300; i++) {
//      this.messageService.create(
//          new MessageCreateRequest(String.valueOf(i),
//              UUID.fromString("e9a16991-69ae-4070-b2e6-ff85834da72c"),
//              UUID.fromString("c57342ac-6913-4c3c-933f-d5ef1035b5d6")), null);
//    }
//  }

}
