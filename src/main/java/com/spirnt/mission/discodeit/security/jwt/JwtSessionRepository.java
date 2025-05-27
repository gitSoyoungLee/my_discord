package com.spirnt.mission.discodeit.security.jwt;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtSessionRepository extends JpaRepository<JwtSession, UUID> {

    @Query("SELECT j FROM JwtSession j WHERE j.refreshToken = :refreshToken")
    Optional<JwtSession> findByRefreshToken(String refreshToken);
}
