package com.spirnt.mission.discodeit.repository;

import com.spirnt.mission.discodeit.security.jwt.JwtSession;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtSessionRepository extends JpaRepository<JwtSession, UUID> {

}
