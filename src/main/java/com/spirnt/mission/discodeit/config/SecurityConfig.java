package com.spirnt.mission.discodeit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain chain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .authorizeHttpRequests(auth -> auth
                // 정적 리소스 요청 허용
                .requestMatchers("/assets/**", "/favicon.ico", "/index.html").permitAll()
                // Swagger 요청 허용
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html",
                    "/webjars/**").permitAll()
                // Actuator 요청 허용
                .requestMatchers("/actuator/**").permitAll()
                // CSRF 토큰을 발급하는 API 요청 허용
                .requestMatchers("/api/auth/csrf-token").permitAll()
                // 그 외 인증 필요
                .anyRequest().authenticated())
            .csrf(csrf -> csrf
                // JavaScript에서 쿠키를 읽을 수 있게 함
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
            // LogoutFilter 제외
            .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer.disable())
            .httpBasic(Customizer.withDefaults());
        return httpSecurity.build();
    }
}
