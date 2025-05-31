package com.spirnt.mission.discodeit.security.jwt;

import com.spirnt.mission.discodeit.dto.user.UserDto;
import com.spirnt.mission.discodeit.entity.User;
import com.spirnt.mission.discodeit.exception.User.UserNotFoundException;
import com.spirnt.mission.discodeit.exception.auth.InvalidJwtTokenException;
import com.spirnt.mission.discodeit.exception.auth.JwtSessionNotFoundException;
import com.spirnt.mission.discodeit.repository.UserRepository;
import java.util.Map;
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
    private final UserRepository userRepository;

    @Transactional
    public JwtSession createJwtSession(UserDto userDto) {
        User user = userRepository.findById(userDto.getId())
            .orElseThrow(() -> new UserNotFoundException(Map.of("userId", userDto.getId())));
        JwtSession jwtSession = new JwtSession(user,
            jwtTokenProvider.generateAccessToken(userDto),
            jwtTokenProvider.generateRefreshToken(userDto));
        jwtSessionRepository.save(jwtSession);

        return jwtSession;
    }

    public boolean isValid(String token) {
        return jwtTokenProvider.validateToken(token);
    }

    @Transactional
    public JwtSession rotateToken(String refreshToken) {
        // 토큰 유효성 검증
        if (refreshToken == null || !isValid(refreshToken)) {
            throw new InvalidJwtTokenException(Map.of());
        }

        JwtSession jwtSession = jwtSessionRepository.findByRefreshToken(refreshToken)
            .orElseThrow(() -> new JwtSessionNotFoundException(Map.of()));

        // 토큰 재발급
        User user = jwtSession.getUser();
        UserDto userDto = UserDto.from(user, true);
        String newAccessToken = jwtTokenProvider.generateAccessToken(userDto);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(userDto);
        // Rotation 전략
        jwtSession.updateToken(newAccessToken, newRefreshToken);

        return jwtSession;
    }

    @Transactional
    public void invalidateToken(String refreshToken) {
        JwtSession jwtSession = jwtSessionRepository.findByRefreshToken(refreshToken)
            .orElseThrow(() -> new JwtSessionNotFoundException(Map.of()));
        jwtSessionRepository.delete(jwtSession);
    }

}
