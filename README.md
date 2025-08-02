
[![codecov](https://codecov.io/gh/gitSoyoungLee/1-sprint-mission/branch/part3-%EC%9D%B4%EC%86%8C%EC%98%81-sprint8/graph/badge.svg?token=KP1S8SQKNL)](https://codecov.io/gh/gitSoyoungLee/1-sprint-mission)
[![CI workflow](https://github.com/gitSoyoungLee/1-sprint-mission/actions/workflows/test.yml/badge.svg?branch=part3-%EC%9D%B4%EC%86%8C%EC%98%81-sprint8)](https://github.com/gitSoyoungLee/1-sprint-mission/actions/workflows/test.yml)

# Spring 백엔드 트랙 1기 스프린트 미션 제출 리포지토리
# 스프린트 미션 10


1. [목표](#목표)
2. [요구사항💟](#요구사항)
3. [나의 설계⭐](#나의-설계)

## 목표
- JWT 기반 인증 / 인가 


## 요구사항💟
### 기본

**CSRF 보호 설정 리팩토링**
- [x] CSRF 토큰 저장소를 쿠키 기반 저장 방식으로 리팩토링하세요.
- [x] 디스코드잇 프론트엔드에서 토큰을 헤더에 포함할 수 있도록 쿠키의 HTTP Only 속성은 false로 설정하세요.
  - 쿠키 이름: XSRF-TOKEN
  - 헤더 이름: X-XSRF-TOKEN


**JWT 다루기**
- [x] 적절한 JWT 라이브러리를 추가하세요.
- [x] 다음의 요구사항을 만족하는 JwtSession을 구현하세요.
  - 패키지명: com.sprint.mission.discodeit.security.jwt
  - 다음의 정보를 포함하세요.
    - 사용자 정보
    - 발급된 엑세스 토큰
    - 발급된 리프레시 토큰
  - 데이터베이스를 통해 관리할 수 있습니다.
- [x] 다음의 요구사항을 만족하는 JwtService를 구현하세요.
  - 패키지명: com.sprint.mission.discodeit.security.jwt
  - UserDto 정보로 토큰을 생성할 수 있습니다.
    - JwtSession을 같이 저장하세요.
  - 토큰의 유효성을 검사할 수 있습니다.
  - 리프레시 토큰을 활용해 엑세스 토큰을 재발급할 수 있습니다.
    - 리프레시 토큰 Rotation 전략을 활용합니다.
    - JwtSession을 같이 수정하세요.
  - 리프레시 토큰을 무효화할 수 있습니다.
    - JwtSession을 같이 삭제하세요.
  - 페이로드에는 다음의 정보를 포함합니다.
    - iat: 토큰 발행 시간
    - exp: 토큰 만료 시간
    - userDto: UserDto 정보


**로그인 리팩토링**
- [x]  로그인 API 스펙을 다음과 같이 변경합니다.

| 엔드포인트          | 요청             | 응답                    |
|---------------------|------------------|-------------------------|
| POST /api/auth/login | Body LoginRequest | 200 String, 401 ErrorResponse |

  - 엔드포인트와 요청은 이전과 동일합니다.
  - 응답 Body에 문자열로 엑세스 토큰을 포함하세요.
  - Cookie에 리프레시 토큰을 저장하세요.
- [x]  인증이 성공한 경우 JwtService를 활용해 토큰을 발급하세요.

**엑세스 토큰 인증 필터 구현**
- 요청 헤더에 포함된 엑세스 토큰을 통해 인증하는 필터를 구현합니다.
- [x] 엑세스 토큰 인증이 필요한 요청에 대해서만 인증을 시도합니다.
- [x] JwtService를 통해 토큰의 유효성을 검사합니다.
- [x] 유효한 토큰인 경우 인증 완료 처리합니다.
- [x] 유효하지 않은 토큰인 경우 401 응답을 반환합니다.

**리프레시 토큰을 활용한 엑세스 토큰 정보 조회**
>디스코드잇 프론트엔드에서 Access Token 정보는 브라우저 메모리에서만 관리합니다. 따라서 새로 고침 시 Access Token 정보가 날아가게 됩니다.
- [x]  Me API 스펙을 다음과 같이 변경합니다.

| 엔드포인트          | 요청             | 응답                    |
|---------------------|------------------|-------------------------|
| GET /api/auth/me | Header(자동 포함) Cookie: refresh_token=… | 200 String |

  - 엔드포인트는 이전과 동일합니다.
  - 요청 헤더의 쿠키는 세션 대신 리프레시 토큰을 활용합니다.
  - 응답 Body에 문자열로 엑세스 토큰을 포함하세요.
> 디스코드잇 프론트엔드에서 엑세스 토큰을 파싱해 UserDto를 추출하여 사용합니다.
- [x] JwtSession 정보를 통해 해당 리프레스 토큰으로 발급된 엑세스 토큰을 반환합니다.
  - 엑세스 토큰을 새로 발급하지 않습니다.

**로그아웃 리팩토링**
- [x]  로그아웃 API 스펙은 다음과 같이 변경합니다.
  - 엔드포인트와 응답은 이전과 동일합니다.
  - 요청 헤더의 쿠키는 세션 대신 리프레시 토큰을 활용합니다.
- [x]  JwtService를 활용해 토큰을 무효화하세요.
- [x] 쿠키에 저장된 리프레시 토큰을 무효화하세요.

| 엔드포인트          | 요청             | 응답                    |
|---------------------|------------------|-------------------------|
| POST /api/auth/logout | Header(자동 포함) Cookie: refresh_token=… | 200 Void|

**리프레시 토큰을 활용한 액세스 토큰 재발급**
- [x]  토큰 재발급 API 스펙은 다음과 같습니다.
  - 응답 Body에 문자열로 엑세스 토큰을 포함하세요.
  - Cookie에 리프레시 토큰을 저장하세요.
-  [x] 요청 리프레시 토큰이 유효하고 JwtSession이 존재할 때 JwtService를 활용해 엑세스 토큰을 재발급합니다.
- [x] 리프레시 토큰이 유효하지 않거나, JwtSession이 존재하지 않으면 401 응답을 반환합니다.

| 엔드포인트          | 요청             | 응답                    |
|---------------------|------------------|-------------------------|
| POST /api/auth/refresh | Header(자동 포함) Cookie: refresh_token=… | 200 String, 401 ErrorResponse |


**권한 수정 리팩토링**
- 권한 수정 API 스펙인 이전과 동일합니다.
- [x] 권한이 수정된 사용자가 로그인 상태라면, 강제 로그아웃 되도록 합니다.
  - JwtSession을 활용하세요.

> 강제 로그아웃 하더라도 해당 유저가 새로고침하지 않는다면, 엑세스 토큰의 유효기간 동안은 이전의 권한으로 여전히 API 요청을 할 수 있습니다. 이 버그에 대해서는 심화 요구사항을 수행하면서 해결합니다.


### 심화


## 주요 변경사항
**블랙리스트를 활용한 강제 로그아웃 고도화**
- 엑세스 토큰 블랙리스트를 통해 사용할 수 없는 엑세스 토큰을 더 적극적으로 제어할 수 있습니다.
- [x] 다음의 요구사항을 만족하는 JwtBlacklist를 구현하세요.
  - 패키지명: com.sprint.mission.discodeit.security.jwt
  - 다음의 정보를 포함하세요.
    - 엑세스 토큰, 만료 시간 Map
      - 동시성 처리를 위해 ConcurrentHashMap을 활용하세요.
      - 동시성에 대해서는 다음 교과목에서 학습하니다.
  - 성능을 위해 데이터베이스는 사용하지 않고 메모리에서만 관리합니다.
- [x] JwtService에서 리프레시 토큰을 무효화할 때 해당 엑세스 토큰을 블랙리스트에 추가하세요.
- [x] JwtService에서 엑세스 토큰의 유효성을 검사할 때 블랙리스트에 포함되었는지 확인하세요.
- [x] 메모리 누수 방지를 위해 필요한 조치를 취하세요.

**사용자 로그인 상태 리팩토링**
- [x] SessionRegistry로 확인하던 사용자 로그인 상태를 JwtSession을 활용하도록 리팩토링하세요.

**동시 로그인 제한 리팩토링**
- [x] 지난 미션에서 구현한 동시 로그인 제한 기능을 JwtSession을 활용해 리팩토링하세요.

## 나의 설계⭐



