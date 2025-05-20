package com.spirnt.mission.discodeit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain chain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .authorizeHttpRequests(auth -> auth
                // 정적 리소스 요청 허용
                .requestMatchers("/assets/**", "/favicon.ico", "/index.html").permitAll()
                .requestMatchers("/", "/error/*").permitAll()
                // Swagger 요청 허용
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html",
                    "/webjars/**").permitAll()
                // Actuator 요청 허용
                .requestMatchers("/actuator/**").permitAll()
                // CSRF 토큰을 발급하는 API 요청 허용
                .requestMatchers("/api/auth/csrf-token").permitAll()
                // 회원가입 API 요청 허용
                .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                // 그 외 인증 필요
                .anyRequest().authenticated())
            // LogoutFilter 제외
            .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer.disable())
            .httpBasic(Customizer.withDefaults())
            .formLogin(Customizer.withDefaults());
        return httpSecurity.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails userDetails = User.withUsername("user")
            .password(passwordEncoder.encode("password"))
            .build();
        return new InMemoryUserDetailsManager(userDetails);
    }


}
