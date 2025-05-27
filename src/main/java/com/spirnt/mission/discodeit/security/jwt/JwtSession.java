package com.spirnt.mission.discodeit.security.jwt;

import com.spirnt.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "jwt_session")
@NoArgsConstructor
@Getter
public class JwtSession extends BaseEntity {

    // 사용자 정보
    private UUID userId;
    private String accessToken; // 액세스 토큰
    private String refreshToken;    // 리프레시 토큰

    public JwtSession(UUID userId, String accessToken, String refreshToken) {
        super();
        this.userId = userId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
