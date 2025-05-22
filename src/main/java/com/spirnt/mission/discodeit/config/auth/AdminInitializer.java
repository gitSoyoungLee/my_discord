package com.spirnt.mission.discodeit.config.auth;

import com.spirnt.mission.discodeit.entity.Role;
import com.spirnt.mission.discodeit.entity.User;
import com.spirnt.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String username = "admin";
        String email = "admin@mail.com";

        if (!userRepository.existsByUsername(username) && !userRepository.existsByEmail(email)) {
            User admin = new User(username, email, passwordEncoder.encode("password"));
            admin.updateRole(Role.ROLE_ADMIN);
            userRepository.save(admin);
            log.info("Admin user created while application starting");
        } else {
            log.info("Admin user with usernmae: {} aleardy exists", username);
        }
    }
}
