package com.spirnt.mission.discodeit.repositoryTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.spirnt.mission.discodeit.entity.Channel;
import com.spirnt.mission.discodeit.entity.ChannelType;
import com.spirnt.mission.discodeit.repository.ChannelRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataJpaTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class ChannelRepositoryTest {

  @Autowired
  private ChannelRepository channelRepository;

  @Autowired
  private EntityManager em;

  @Test
  @DisplayName("공개 채널 가져오기 테스트")
  void testFindAllPublic() {
    // given
    // 레포지토리에 public 2개, private 1개 저장
    Channel public1 = new Channel("Public 1", "Description 1", ChannelType.PUBLIC);
    Channel public2 = new Channel("Public 2", "Description 2", ChannelType.PUBLIC);
    Channel private1 = new Channel("Private 1", "Secret Description", ChannelType.PRIVATE);
    em.persist(public1);
    em.persist(public2);
    em.persist(private1);
    em.flush();
    em.clear();
    // when
    List<Channel> publicChannels = channelRepository.findAllPublic();

    // then
    assertEquals(2, publicChannels.size());
    // 공개 채널만 가져왔어야 함
    for (Channel ch : publicChannels) {
      assertEquals(ChannelType.PUBLIC, ch.getType());
    }
  }
}
