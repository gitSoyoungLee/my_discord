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
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
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
                // CSRF 토큰 발급, 회원가입, 로그인 API 요청 허용
                .requestMatchers("/api/auth/csrf-token").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                // 그 외 API는 최소 ROLE_USER 권한 필요
                .requestMatchers("/api/**").hasRole("USER")
                // 그 외 인증 필요
                .anyRequest().authenticated())
            .csrf(csrf ->
                // csrf 검증 제외
                csrf.ignoringRequestMatchers("/api/users", "/api/auth/login",
                    "/api/auth/logout"))
            // 커스텀 인증 필터 추가: UserAuthenticationFilter 대신 CustomAuthenticationFilter 사용
            .addFilterAt(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//            .rememberMe(rememberMe -> rememberMe
//                .rememberMeParameter("remember-me") // 프론트에서 보내는 속성
//                .tokenValiditySeconds(60 * 60 * 24 * 21)    // 쿠키 유효 시간 21일
//                .key("mySecretKey123!") // 서명에 사용할 키
//            )
            .sessionManagement(session -> session
                .sessionFixation(sf -> sf.changeSessionId())  // 세션 고정 공격 방어: 세션 ID만 변경
                .maximumSessions(1)    // 동시 세션 제어: 동시 세션 최대 1개 허용
                .maxSessionsPreventsLogin(false)    // 새로운 로그인 시 기존 세션을 무효화
                .sessionRegistry(sessionRegistry())  // 세션 관리 기능
            )
            .logout(logout -> logout.logoutRequestMatcher(
                    new AntPathRequestMatcher("/api/auth/logout"))  // POST /api/auth/logout으로 로그아웃
                .logoutSuccessUrl("/") // 세션 무효화 후 홈으로
                .deleteCookies("JSESSIONID")    // 쿠키 삭제
                .invalidateHttpSession(true));  // 로그아웃 시 세션 삭제
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
    public CustomAuthenticationSuccessHandler successHandler(SessionRegistry sessionRegistry,
        ObjectMapper objectMapper, UserMapper userMapper) {
        return new CustomAuthenticationSuccessHandler(objectMapper, sessionRegistry, userMapper);
    }

    @Bean
    public CustomAuthenticationFailureHandler failureHandler(ObjectMapper objectMapper) {
        return new CustomAuthenticationFailureHandler(objectMapper);
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.fromHierarchy(
            "ROLE_ADMIN > ROLE_CHANNEL_MANAGER\n" +
                "ROLE_CHANNEL_MANAGER > ROLE_USER"
        );
    }

    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler(
        RoleHierarchy roleHierarchy) {
        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
        handler.setRoleHierarchy(roleHierarchy);
        return handler;
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

}
