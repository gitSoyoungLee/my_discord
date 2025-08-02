
[![codecov](https://codecov.io/gh/gitSoyoungLee/1-sprint-mission/branch/part3-%EC%9D%B4%EC%86%8C%EC%98%81-sprint8/graph/badge.svg?token=KP1S8SQKNL)](https://codecov.io/gh/gitSoyoungLee/1-sprint-mission)
[![CI workflow](https://github.com/gitSoyoungLee/1-sprint-mission/actions/workflows/test.yml/badge.svg?branch=part3-%EC%9D%B4%EC%86%8C%EC%98%81-sprint8)](https://github.com/gitSoyoungLee/1-sprint-mission/actions/workflows/test.yml)

# Spring 백엔드 트랙 1기 스프린트 미션 제출 리포지토리
# 스프린트 미션 9


1. [목표](#목표)
2. [요구사항💟](#요구사항)
3. [나의 설계⭐](#나의-설계)

## 목표
- Spring Security 환경 설정
- 세션 기반 인증/인가



## 요구사항💟
### 기본
**Spring Security 환경 설정**
- [x] 프로젝트에 Spring Security 의존성을 추가하세요.
- [x] Security 설정 클래스를 생성하세요.
  - 패키지명: com.sprint.mission.discodeit.config
  - 클래스명: SecurityConfig
-  [x] SecurityFilterChain Bean을 선언하세요.
  - [x] 가장 기본적인 SecurityFilterChain을 등록하고, 이때 등록되는 필터 목록을 디버깅해보세요.
    ![기본필터체인 필터목록 조회](https://github.com/user-attachments/assets/c887a1df-e810-40f9-8b90-944460f5a25e)

  - [x] 모든 요청에 대해 인증이 수행되도록 하세요.
  - [x] /api/를 포함하지 않는 모든 url에 대한 요청(정적 리소스, swagger, actuator 등)은 인증을 수행하지 않도록 하세요.
- [x] LogoutFilter를 제외하세요.
  - 디스코드잇은 로그아웃 페이지를 CSR로 처리하기 때문에 LogoutFilter는 사용하지 않습니다.
- [x] 개발 환경에서 Spring Security 모듈의 로깅 레벨을 trace로 설정하세요.
  - 각 요청마다 통과하는 필터 목록을 확인할 수 있습니다.

**CSRF 보호 설정하기**
> 디스코드잇은 CSR 방식이기 때문에 CSRF 토큰을 프론트엔드에서 명시적으로 관리합니다.
> 1. 페이지가 로드될 때 서버로부터 CSRF 토큰 발급
> 2. CSRF 토큰을 쿠키(CSRF-TOKEN)에 저장
> 3. 매 요청마다 쿠키에 저장된 CSRF 토큰을 헤더(X-CSRF-TOKEN)에 포함

- [x] CSRF 토큰을 발급하는 API를 구현하세요
  - 엔드포인트: GET /api/auth/csrf-token
  - 요청: 없음
  - 응답: 200 CsrfToken
- [x] CSRF 토큰을 발급하는 API는 인증하지 않도록 SecurityFilterChain을 리팩토링하세요.

**회원가입 고도화**
- [x]  회원가입 API 스펙은 유지합니다.
  - 엔드포인트: POST /api/users
  - 요청: Body UserCreateRequest, MultipartFile
  - 응답: 200 UserDto
- [x] 회원가입 시 비밀번호는 PasswordEncoder를 통해 해시로 저장하세요.
  -  PasswordEncoder의 구현체는 BCryptPasswordEncoder를 활용하세요.
- [x]  회원가입 API는 인증하지 않도록 SecurityFilterChain을 리팩토링하세요.

**기본 인증 구현**
>디스코드잇은 CSR 방식이기 때문에 formLogin은 사용하지 않습니다.
- 다음의 조건을 만족하는 필터와 AuthenticationProvider를 구현하세요.
- [x]  로그인 API 스펙은 다음과 같습니다.
  - 엔드포인트: POST /api/auth/login
  - 요청: Body LoginRequest
  - 응답: 200 UserDto / 401 ErrorResponse
  - 기존에 구현했던 로그인 관련 코드는 제거하세요
- [x]  다음의 주요 컴포넌트를 활용해 Spring Security의 기본 인증 플로우를 최대한 유지합니다.
  - 인증 플로우 참고: UsernamePasswordAuthenticationFilter
  - AuthenticationProvider: DaoAuthenticationProvider
  - SecurityContextRespository: HttpSessionSecurityContextRepository

**세션을 활용한 현재 사용자 정보 조회**
> 브라우저의 세션 스토리지(user-storage)로 관리하던 사용자 정보는 보안 강화를 위해 브라우저 메모리에서만 관리하도록 변경합니다. 단, 메모리에서만 관리하는 경우 새로고침 시 로그인 정보가 날아가게 됩니다. 따라서 세션을 통해 현재 사용자 정보를 조회할 수 있도록 API를 구현 및 활용합니다.
- [x]  세션ID를 통해 사용자의 기본 정보(UserDto)를 가져올 수 있도록 API를 정의하세요.
  - 엔드포인트: GET /api/auth/me
  - 요청: Header(자동 포함) Cookie: JSESSIONID=…
  - 응답: 200 UserDto

**로그아웃**
- 다음의 조건을 만족하는 필터를 구현하세요.
- [x]  로그아웃 API 스펙은 다음과 같습니다.
  - 엔드포인트: POST /api/auth/logout
  - 요청: Header(자동 포함) Cookie: JSESSIONID=…
  - 응답: 200 Void
- [x]  CSRF 검사는 수행하지 않도록 합니다.
- [x]  해당 세션을 무효화 처리하세요.
- [x]  SecurityContext를 초기화하세요.

**권한 관리**
- [x]  다음과 같이 권한을 정의하세요.
  - 관리자: ROLE_ADMIN
  - 채널 매니저: ROLE_CHANNEL_MANAGER
  - 일반 사용자: ROLE_USER
  - 각 권한은 계층 구조를 가집니다.
    - 관리자 > 채널 매니저 > 일반 사용자
- [x]  회원 가입 시 모든 사용자는 ROLE_USER 권한을 가집니다.
- [x]  애플리케이션 실행 시 ROLE_ADMIN 권한을 가진 계정이 초기화되도록 구현하세요.
- [x]  UserDto에 권한 정보를 포함하세요.
- [x]  사용자 권한을 수정하는 API를 구현하세요.
  - 엔드포인트: PUT /api/auth/role
  - 요청: Body UserRoleUpdateRequest(UUID userId, Role newRole)
  - 응답: 200 UserDto
- [x]  권한이 수정된 사용자가 로그인 상태라면, 강제 로그아웃 되도록 합니다.
> 디스코드잇 프론트엔드에서는 401 응답인 경우에 강제 로그아웃 되도록 구현되었습니다.

**인가 처리**
- [x] 회원가입, 로그인, csrf 토큰 발급 등을 제외한 모든 API는 최소 ROLE_USER 권한을 가져야합니다.
- [x] 퍼블릭 채널 생성, 수정, 삭제는 최소 ROLE_CHANNEL_MANAGER 권한을 가져야합니다.
- [x] 사용자 권한 수정은 ROLE_ADMIN 권한을 가져야합니다.

### 심화

**Remember-Me**
- 다음의 조건을 만족하도록 로그인 유지 기능을 구현하세요.
- [x] 토큰은 데이터베이스에 저장하세요.
- [x] 쿠키에 저장되는 토큰의 유효기간은 3주로 지정하세요.
- [x] 로그아웃 시 데이터베이스에 저장된 토큰을 삭제하고, 클라이언트 쿠키도 삭제하세요.

**동시 로그인 제한**
- [x] 하나의 사용자 ID로 동시 로그인을 제한하세요. 새로운 로그인 발생 시 기존 세션을 무효화하세요.

**세션 고정 보호**
- [x] 세션 고정 보호를 위해 필요한 설정을 구현하세요.

**사용자 로그인 상태 고도화**
- [x] Session 정보를 활용해 사용자의 로그인 상태를 판단하도록 리팩토링하세요.
- [x] UserStatus 엔티티와 관련된 모든 코드는 삭제하세요.

**인가 고도화**
- [x] 사용자 정보 수정, 삭제는 본인 또는 ROLE_ADMIN 권한을 가진 사용자만 호출할 수 있습니다.
- [x] 메시지 수정은 메시지를 작성한 사람만 호출할 수 있습니다.
- [x] 메시지 삭제는 메시지를 작성한 사람 또는 ROLE_ADMIN 권한을 가진 사용자만 호출할 수 있습니다.
- [x] 읽음 상태 생성, 수정은 본인만 호출할 수 있습니다.

## 나의 설계⭐



