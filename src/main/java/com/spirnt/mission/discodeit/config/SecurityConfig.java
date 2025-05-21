package com.spirnt.mission.discodeit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spirnt.mission.discodeit.config.auth.CustomAuthenticationFailureHandler;
import com.spirnt.mission.discodeit.config.auth.CustomAuthenticationFilter;
import com.spirnt.mission.discodeit.config.auth.CustomAuthenticationSuccessHandler;
import com.spirnt.mission.discodeit.config.auth.CustomUserDetailsService;
import com.spirnt.mission.discodeit.mapper.UserMapper;
import com.spirnt.mission.discodeit.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    SecurityFilterChain chain(HttpSecurity httpSecurity,
        CustomAuthenticationFilter customAuthenticationFilter) throws Exception {
        httpSecurity
            .authorizeHttpRequests(auth -> auth
                // 정적 리소스 요청 허용
                .requestMatchers("/assets/**", "/favicon.ico", "/index.html").permitAll()
                .requestMatchers("/", "/error/**").permitAll()
                // Swagger 요청 허용
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html",
                    "/webjars/**").permitAll()
                // Actuator 요청 허용
                .requestMatchers("/actuator/**").permitAll()
                // CSRF 토큰을 발급하는 API 요청 허용
                .requestMatchers("/api/auth/csrf-token").permitAll()
                // 회원가입, 로그인 API 요청 허용
                .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                // 그 외 인증 필요
                .anyRequest().authenticated())
            // 회원가입과 로그인은 csrf 검증 제외
            .csrf(csrf -> csrf.ignoringRequestMatchers("/api/users", "/api/auth/login"))
            // 커스텀 인증 필터 추가: UserAuthenticationFilter 대신 CustomAuthenticationFilter 사용
            .addFilterAt(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            // LogoutFilter 제외
            .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer.disable());
        return httpSecurity.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(UserRepository userRepository) {
        return new CustomUserDetailsService(userRepository);
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
        PasswordEncoder passwordEncoder) throws Exception {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }

    @Bean
    CustomAuthenticationFilter customAuthenticationFilter(
        AuthenticationManager authenticationManager, ObjectMapper objectMapper,
        CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler,
        CustomAuthenticationFailureHandler customAuthenticationFailureHandler) {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(
            authenticationManager, objectMapper);
        // /api/auth/login 경로에만 필터 적용
        customAuthenticationFilter.setFilterProcessesUrl("/api/auth/login");
        // 핸들러 적용
        customAuthenticationFilter.setAuthenticationSuccessHandler(
            customAuthenticationSuccessHandler);
        customAuthenticationFilter.setAuthenticationFailureHandler(
            customAuthenticationFailureHandler);
        return customAuthenticationFilter;
    }

    @Bean
    public CustomAuthenticationSuccessHandler successHandler(UserMapper userMapper,
        ObjectMapper objectMapper) {
        return new CustomAuthenticationSuccessHandler(objectMapper, userMapper);
    }

    @Bean
    public CustomAuthenticationFailureHandler failureHandler(ObjectMapper objectMapper) {
        return new CustomAuthenticationFailureHandler(objectMapper);
    }

}
