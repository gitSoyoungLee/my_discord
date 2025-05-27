package com.spirnt.mission.discodeit.security.jwt;

import com.spirnt.mission.discodeit.dto.user.UserDto;
import com.spirnt.mission.discodeit.repository.JwtSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    private final JwtSessionRepository jwtSessionRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public JwtSession createJwtSession(UserDto userDto) {
        JwtSession jwtSession = new JwtSession(userDto.getId(),
            jwtTokenProvider.generateAccessToken(userDto),
            jwtTokenProvider.generateRefreshToken(userDto));
        jwtSessionRepository.save(jwtSession);

        return jwtSession;
    }

    public boolean isValid(String token) {
        return jwtTokenProvider.validateToken(token);
    }


}
