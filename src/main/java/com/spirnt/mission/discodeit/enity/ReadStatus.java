package com.spirnt.mission.discodeit.enity;

import com.spirnt.mission.discodeit.enity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 사용자가 채널 별 마지막으로 메시지를 읽은 시간을 표현하는 도메인
// '유저'가 '채널'에서 마지막으로 읽은 '메세지' 기록
@Entity
@Table(name = "read_statuses")
@NoArgsConstructor
@Getter
public class ReadStatus extends BaseUpdatableEntity {

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;
  @ManyToOne
  @JoinColumn(name = "channel_id")
  private Channel channel;
  @Column(name = "last_read_at")
  private Instant lastReadAt;


  public ReadStatus(User user, Channel channel, Instant lastReadAt) {
    super();
    this.user = user;
    this.channel = channel;
    this.lastReadAt = lastReadAt;
  }

  // 마지막으로 읽은 메세지 업데이트
  public void update(Instant lastReadAt) {
    this.lastReadAt = lastReadAt;
  }
}
