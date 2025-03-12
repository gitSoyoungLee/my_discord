package com.spirnt.mission.discodeit;

import com.spirnt.mission.discodeit.dto.message.MessageCreateRequest;
import com.spirnt.mission.discodeit.service.MessageService;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DiscodeitApplicationTests {

  @Autowired
  private MessageService messageService;

  @Test
  void contextLoads() {
  }

  @Test
  void pagingTest() {
    for (int i = 0; i < 300; i++) {
      this.messageService.create(
          new MessageCreateRequest("Hello", UUID.fromString("701aa0cf-5e86-4396-a5e9-2b0c332f83c8"),
              UUID.fromString("3b2fe432-13fc-4da6-badd-4c9604fb8e1b")), null);
    }
  }

}
