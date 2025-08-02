
[![codecov](https://codecov.io/gh/gitSoyoungLee/1-sprint-mission/branch/part3-%EC%9D%B4%EC%86%8C%EC%98%81-sprint8/graph/badge.svg?token=KP1S8SQKNL)](https://codecov.io/gh/gitSoyoungLee/1-sprint-mission)
[![CI workflow](https://github.com/gitSoyoungLee/1-sprint-mission/actions/workflows/test.yml/badge.svg?branch=part3-%EC%9D%B4%EC%86%8C%EC%98%81-sprint8)](https://github.com/gitSoyoungLee/1-sprint-mission/actions/workflows/test.yml)

# Spring 백엔드 트랙 1기 스프린트 미션 제출 리포지토리
# 스프린트 미션 12


1. [목표](#목표)
2. [요구사항💟](#요구사항)
3. [나의 설계⭐](#나의-설계)

## 목표
- 웹소켓과 SSE를 활용한 실시간 통신
- Nginx를 활용한 배포 아키텍처 구성


## 요구사항💟
### 기본

**데이터베이스**

- [x] 아래와 같이 데이터베이스 환경을 설정하세요.
**웹소켓 구현하기**
- [x] 웹소켓 환경 구성
  - spring-boot-starter-websocket 의존성을 추가하세요.
  - ```implementation 'org.springframework.boot:spring-boot-starter-websocket'```
  - 웹소켓 메시지 브로커 설정
    - 메모리 기반 SimpleBroker를 사용하세요.
    - STOMP 엔드포인트는 /ws로 설정하고, SockJS 연결을 지원해야 합니다.
- [x] 메시지 수신
  - 구독(Subscribe) 엔드포인트: /sub/channels.{channelId}.messages
  - 수신되는 메시지 타입은 MessageDto를 사용하세요.
- [x] 메시지 송신
  - 클라이언트가 메시지를 전송할 수 있도록 다음과 같이 엔드포인트를 구성하세요.
  - 첨부파일이 없는 텍스트 메시지
    - 송신 방식: 웹소켓
    - 전송 엔드포인트: ```/pub/messages```
    - 요청 페이로드 타입: ```MessageCreateRequest```
  - 첨부파일이 포함된 메시지
    - 기존 HTTP API를 유지하세요.

**SSE 구현하기**
- [x] SSE 환경을 구성하세요.
  - SSE 연결을 위한 엔드포인트를 구현하세요.
    - GET ```/api/sse```
  - 다음 요구사항을 만족해야 합니다.
    - 사용자당 N개의 연결을 허용할 수 있어야 합니다 (예: 다중 탭/기기).
    - SseEmitter 객체를 스레드 세이프한 메모리 구조에서 안전하게 관리해야 합니다.
    - 메모리 누수 방지를 위해 다음과 같은 처리를 해야 합니다:
      - ```onCompletion```, ```onTimeout```, ```onError``` 이벤트 핸들러에서 emitter를 제거합니다.
      - 주기적 스케줄링 작업을 통해 ping을 보내고, 응답이 없는 연결을 정리합니다.
    - 각 이벤트에는 고유한 ID를 부여하고, 클라이언트에서 Last-Event-ID를 전송해 이벤트 유실 복원이 가능하도록 해야 합니다.
- 기존에 클라이언트에서 폴링 방식으로 주기적으로 요청하던 데이터를 SSE를 이용해 서버에서 실시간으로 전달하는 방식으로 리팩토링하세요.
- [x] 새로운 알림 이벤트 전송
  - 새 알림이 생성되었을 때 클라이언트에 이벤트를 전송하세요.
  - 클라이언트는 이 이벤트를 수신하면 알림 목록에 알림을 추가합니다.
  - 이벤트 명세

| id | 이벤트 고유 ID |
| --- | --- |
| name | `notifications` |
| data | `NotificationDto` |

- [x] 파일 업로드 상태 변경 이벤트 전송
  - 파일 업로드 상태가 변경될 때 이벤트를 발송하세요.
  - 클라이언트는 해당 상태를 수신하여 UI를 다시 렌더링합니다.
  - 이벤트 명세

| id | 이벤트 고유 ID |
| --- | --- |**웹소켓 구현하기**
- [x] 웹소켓 환경 구성
  - spring-boot-starter-websocket 의존성을 추가하세요.
  - ```implementation 'org.springframework.boot:spring-boot-starter-websocket'```
  - 웹소켓 메시지 브로커 설정
    - 메모리 기반 SimpleBroker를 사용하세요.
    - STOMP 엔드포인트는 /ws로 설정하고, SockJS 연결을 지원해야 합니다.
- [x] 메시지 수신
  - 구독(Subscribe) 엔드포인트: /sub/channels.{channelId}.messages
  - 수신되는 메시지 타입은 MessageDto를 사용하세요.
- [x] 메시지 송신
  - 클라이언트가 메시지를 전송할 수 있도록 다음과 같이 엔드포인트를 구성하세요.
  - 첨부파일이 없는 텍스트 메시지
    - 송신 방식: 웹소켓
    - 전송 엔드포인트: ```/pub/messages```
    - 요청 페이로드 타입: ```MessageCreateRequest```
  - 첨부파일이 포함된 메시지
    - 기존 HTTP API를 유지하세요.

**SSE 구현하기**
- [x] SSE 환경을 구성하세요.
  - SSE 연결을 위한 엔드포인트를 구현하세요.
    - GET ```/api/sse```
  - 다음 요구사항을 만족해야 합니다.
    - 사용자당 N개의 연결을 허용할 수 있어야 합니다 (예: 다중 탭/기기).
    - SseEmitter 객체를 스레드 세이프한 메모리 구조에서 안전하게 관리해야 합니다.
    - 메모리 누수 방지를 위해 다음과 같은 처리를 해야 합니다:
      - ```onCompletion```, ```onTimeout```, ```onError``` 이벤트 핸들러에서 emitter를 제거합니다.
      - 주기적 스케줄링 작업을 통해 ping을 보내고, 응답이 없는 연결을 정리합니다.
    - 각 이벤트에는 고유한 ID를 부여하고, 클라이언트에서 Last-Event-ID를 전송해 이벤트 유실 복원이 가능하도록 해야 합니다.
- 기존에 클라이언트에서 폴링 방식으로 주기적으로 요청하던 데이터를 SSE를 이용해 서버에서 실시간으로 전달하는 방식으로 리팩토링하세요.
- [x] 새로운 알림 이벤트 전송
  - 새 알림이 생성되었을 때 클라이언트에 이벤트를 전송하세요.
  - 클라이언트는 이 이벤트를 수신하면 알림 목록에 알림을 추가합니다.
  - 이벤트 명세

| id | 이벤트 고유 ID |
| --- | --- |
| name | `notifications` |
| data | `NotificationDto` |

- [x] 파일 업로드 상태 변경 이벤트 전송
  - 파일 업로드 상태가 변경될 때 이벤트를 발송하세요.
  - 클라이언트는 해당 상태를 수신하여 UI를 다시 렌더링합니다.
  - 이벤트 명세

| id | 이벤트 고유 ID |
| --- | --- |
| name | `binaryContents.status` |
| data | `BinaryContentDto` |

- [x] 채널 목록 갱신 이벤트 전송
  - 채널 목록을 업데이트해야 할 경우, 이벤트를 발송하세요.
  - 클라이언트는 해당 이벤트를 수신하면 채널 목록을 재조회합니다.
  - 이벤트 명세

    | id | 이벤트 고유 ID |
    | --- | --- |
| name | `binaryContents.status` |
| data | `BinaryContentDto` |

- [x] 채널 목록 갱신 이벤트 전송
  - 채널 목록을 업데이트해야 할 경우, 이벤트를 발송하세요.
  - 클라이언트는 해당 이벤트를 수신하면 채널 목록을 재조회합니다.
  - 이벤트 명세

    | id | 이벤트 고유 ID |
    | --- | --- |
    | name | `channels.refresh` |
    | data | `{channelId: $channelId}` |

- [x] 사용자 목록 갱신 이벤트 전송
  - 사용자 목록을 업데이트해야 할 경우, 이벤트를 발송하세요.
  - 클라이언트는 해당 이벤트를 수신하면 사용자 목록을 재조회합니다.
  - 이벤트 명세

    | id | 이벤트 고유 ID |
    | --- | --- |
    | name | `users.refresh` |
    | data | `{userId: $userId}` |


**배포 아키텍처 구성하기**
- [ ]  다음의 다이어그램에 부합하는 배포 아키텍처를 Docker Compose를 통해 구현하세요.

<img width="1347" height="612" alt="image" src="https://github.com/user-attachments/assets/18fa4441-901c-493d-92b4-9d6c4c3ddb33" />

- `Reverse Proxy`
  - Nginx 기반의 리버스 프록시 컨테이너를 구성하세요.
  - 역할 및 설정은 다음과 같습니다:
    - `/api/*`, `/ws/*` 요청은 Backend 컨테이너로 프록시 처리합니다.
    - 이 외의 모든 요청은 정적 리소스(프론트엔드 빌드 결과)를 서빙합니다. 
      - 프론트엔드 정적 리소스는 Nginx 컨테이너 내부의 적절한 경로(/usr/share/nginx/html 등)에 복사하세요.
  - 외부에서 접근 가능한 유일한 컨테이너이며, 3000번 포트를 통해 접근할 수 있어야 합니다.
- `Backend`
  - Spring Boot 기반의 백엔드 서버를 Docker 컨테이너로 구성하세요.
  - Reverse Proxy를 통해 /api/*, /ws/* 요청이 이 서버로 전달됩니다.
- `DB`, `Memory DB`, `Message Broker`
  - `Backend` 컨테이너가 접근 가능한 다음의 인프라 컨테이너들을 구성하세요
    - DB: PostgreSQL
    - Memory DB: Redis
    - Message Broker: Kafka
  - 각 컨테이너는 Docker Compose 네트워크를 통해 백엔드에서 통신할 수 있어야 합니다.
  - 별도로 외부에 노출하지 않아도 됩니다.
  
### 심화

**웹소켓 인증/인가 처리하기**
- [ ] 인증 처리
  - 클라이언트는 CONNECT 프레임의 헤더에 다음과 같이 Authorization 토큰을 포함합니다.
```
CONNECT
Authorization:Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ3b29keSIsImV4cCI6MTc0OTM5MzA0OCwiaWF0IjoxNzQ5MzkyNDQ4LCJ1c2VyRHRvIjp7ImlkIjoiMDQwZTk2ZWMtMjdmNC00Y2MxLWI4MWQtNTMyM2ExZWQ5NTZhIiwidXNlcm5hbWUiOiJ3b29keSIsImVtYWlsIjoid29vZHlAZGlzY29kZWl0LmNvbSIsInByb2ZpbGUiOm51bGwsIm9ubGluZSI6bnVsbCwicm9sZSI6IlVTRVIifX0.JOkvCpnR0e0KMQYLh_hUWglgTvUIlfQOT58eD4Cym5o
accept-version:1.2,1.1,1.0
heart-beat:4000,4000
```
  - 서버 측에서는 ChannelInterceptor를 구현하여 연결 시 토큰을 검증하고, 인증된 사용자 정보를 SecurityContext에 설정해야 합니다.
  - 참고문서: [Spring 공식 문서](https://docs.spring.io/spring-framework/reference/web/websocket/stomp/authentication-token-based.html)
  - `SecurityContextChannelInterceptor`를 등록하여 이후 메시지 처리 흐름에서도 인증 정보를 활용할 수 있도록 구성하세요.
  - `ChannelInterceptor`와 `SecurityContextChannelInterceptor`를 활용하세요.
  - 디스코드잇 프론트엔드에서는 초기 핸드셰이크 단계에서 헤더(Authorization)에 토큰을 포함하도록 설계되었습니다.

- [ ] 인가 처리
  - 메시지 발행(PUB) 또는 구독(SUB) 요청에 대해 인가 정책을 적용하세요.
  - AuthorizationChannelInterceptor를 사용해 메시지 권한 검사를 수행합니다.
  - 아래의 의존성을 추가하고, AuthorizationManager는 MessageMatcherDelegatingAuthorizationManager를 활용하세요.
  - ```implementation 'org.springframework.security:spring-security-messaging'```
     
**분산 환경 배포 아키텍처 구성하기**
- [ ]  다음의 다이어그램에 부합하는 배포 아키텍처를 Docker Compose를 통해 구현하세요.

<img width="1411" height="612" alt="image" src="https://github.com/user-attachments/assets/3c548291-c77a-4106-8882-e2f3b7d1db47" />

- `Reverse Proxy`
  - 기존 Nginx Reverse Proxy 컨테이너에 로드밸런서 역할을 추가하세요.
  - 다음과 같은 로드밸런싱 전략 중 하나를 선택하여 Backend 서버들로 트래픽을 분산시키세요.
    - Round Robin
    - Least Connections
    - IP Hash
    - Weight
  - upstream 블록을 통해 여러 Backend 서버 인스턴스를 설정하고, 라우팅 전략을 적용하세요.
- [ ]  분산환경에 따른 웹소켓의 한계점을 식별하고 Kafka를 활용해 리팩토링하세요.
- [ ]  분산환경에 따른 SSE의 한계점을 식별하고 Kafka를 활용해 리팩토링하세요.

## 나의 설계⭐



