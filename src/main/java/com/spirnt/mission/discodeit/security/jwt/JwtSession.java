package com.spirnt.mission.discodeit.security.jwt;

import com.spirnt.mission.discodeit.entity.User;
import com.spirnt.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "jwt_session")
@NoArgsConstructor
@Getter
public class JwtSession extends BaseEntity {

    // 사용자 정보
    @OneToOne
    private User user;
    private String accessToken; // 액세스 토큰
    private String refreshToken;    // 리프레시 토큰

    public JwtSession(User user, String accessToken, String refreshToken) {
        super();
        this.user = user;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public void updateToken(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
