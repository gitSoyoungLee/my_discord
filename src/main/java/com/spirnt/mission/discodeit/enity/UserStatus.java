package com.spirnt.mission.discodeit.enity;

import com.spirnt.mission.discodeit.enity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_statuses")
@NoArgsConstructor
@Getter
public class UserStatus extends BaseUpdatableEntity {

  @OneToOne(mappedBy = "status")
  @JoinColumn(name = "user_id")
  private User user;
  @Column(name = "last_active_at")
  private Instant lastActiveAt; // 마지막으로 확인된 접속 시간

  public UserStatus(User user, UserStatusType type, Instant lastActiveAt) {
    super();
    this.user = user;
    this.lastActiveAt = lastActiveAt;
  }


  public boolean isOnline() {
    if (lastActiveAt == null) {
      return false; // 접속한 적이 없으면 오프라인으로 간주
    }
    Instant fiveMinutesAgo = Instant.now().minus(5, ChronoUnit.MINUTES);
    return lastActiveAt.isAfter(fiveMinutesAgo); // 5분 이내 접속이면 온라인
  }

  public void update(Instant lastActiveAt) {
    if (lastActiveAt != null && !lastActiveAt.equals(this.lastActiveAt)
        && this.lastActiveAt.isBefore(lastActiveAt)) {
      this.lastActiveAt = lastActiveAt;
    }
  }

}
