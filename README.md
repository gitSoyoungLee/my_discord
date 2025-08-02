
[![codecov](https://codecov.io/gh/gitSoyoungLee/1-sprint-mission/branch/part3-%EC%9D%B4%EC%86%8C%EC%98%81-sprint8/graph/badge.svg?token=KP1S8SQKNL)](https://codecov.io/gh/gitSoyoungLee/1-sprint-mission)
[![CI workflow](https://github.com/gitSoyoungLee/1-sprint-mission/actions/workflows/test.yml/badge.svg?branch=part3-%EC%9D%B4%EC%86%8C%EC%98%81-sprint8)](https://github.com/gitSoyoungLee/1-sprint-mission/actions/workflows/test.yml)

# Spring 백엔드 트랙 1기 스프린트 미션 제출 리포지토리
# 스프린트 미션 11


1. [목표](#목표)
2. [요구사항💟](#요구사항)
3. [나의 설계⭐](#나의-설계)

## 목표
- 비동기를 통해 응답 속도 향상하기
- 캐시를 통해 DB I/O 줄이기
- Kafka 도입하기
- Redis 도입하기


## 요구사항💟
### 기본

**비동기 적용하기**
- [x] BinaryContentStorage의 파일 업로드 로직을 비동기적으로 처리하세요.
  - 비동기 처리 시 MDC의 Request ID, SecurityContext의 인증 정보가 유지되도록 하세요.
- [x] 업로드 중 예외가 발생한 경우, 자동 재시도 메커니즘을 구현하세요.
  - Spring Retry의 ```@Retryable``` 어노테이션을 사용해 재시도 정책(횟수, 대기 시간 등)을 설정하세요.
- [x] 재시도 횟수를 초과한 경우, 실패 정보(AsyncTaskFailure)를 기록하는 복구 로직을 실행하세요.
  - ```@Recover``` 어노테이션을 사용해 재시도 실패 처리 메서드를 구현하세요.
  - 실패 정보(AsyncTaskFailure)에는 MDC의 Request ID를 포함하여 추적이 가능하도록 하세요.
 
![image](https://github.com/user-attachments/assets/60dd6d64-5c82-438c-963c-f23b31608a2d)

- [x] 파일 업로드 메소드를 호출한 곳(BasicMessageService, BasicUserService 등)에서  비동기 처리 성공/실패에 따라 BinaryContent의 상태를 업데이트 하세요.
  - BinaryContent 엔티티에 업로드 상태를 나타내는 속성(uploadStatus)을 추가하세요.
![image](https://github.com/user-attachments/assets/484aab12-e71b-4a8e-91a6-c7515a43fde2)
    - ```WAITING```: 업로드 대기 중 (파일 저장 요청만 완료된 상태)
    - ```SUCCESS```: 업로드 완료
    - ```FAILED```: 업로드 실패 (모든 재시도 실패)
  - ```WAITING```으로 초기화하세요.
  - 업로드 성공 시, ```SUCCESS```로 업데이트하세요.
  - 업로드 실패 시, ```FAILED```로 업데이트하세요.
    - 의도적인 예외를 발생시켜 테스트해보세요.

> 경우에 따라 비동기 업로드 로직이 메인 스레드의 트랜잭션보다 일찍 종료되는 경우 업데이트할 BinaryContent 데이터가 DB에 반영되지 않아 업데이트가 정상적으로 이루어지지 않을 수 있습니다.
따라서 메인 스레드의 트랜잭션이 끝난 시점까지 대기해야 합니다.
```TransactionSynchronizationManager.registerSynchronization```를 활용해 트랜잭션이 성공적으로 커밋되었을 때 실행할 작업을 정의할 수 있습니다.
```
@Transactional
@Override
public MessageDto create(MessageCreateRequest messageCreateRequest,
    List<BinaryContentCreateRequest> binaryContentCreateRequests) {
                ... 
        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
              @Override
              public void afterCommit() {
                // 비동기 로직...
              }
            }
        );
        ...
}
```
> ```@TransactionalEventListener```를 활용할 수도 있습니다.

- [x] 동기 처리와 비동기 처리 간의 성능 차이를 비교해보세요.
- 파일 업로드 로직에 의도적인 지연(Thread.sleep(...))을 발생시키세요.
- @Timed 어노테이션을 활용하여 API의 실행 시간을 측정하고, 동기/비동기 처리 방식 간 응답 속도 차이를 분석하고 PR에 해당 내용을 포함하세요.

**알림 기능 추가하기**
- [x]  ReadStatus 엔티티를 리팩토링하세요.
  - 새로운 메시지에 대한 알림 활성화 여부를 관리하는 속성(notificationEnabled)을 추가하세요.
  - ReadStatus 엔티티에 ```boolean notificationEnabled``` 추가
  - 사용자가 해당 알림 설정을 변경할 수 있게 API를 수정하세요.
    - ReadStatusUpdateRequest DTO에 ```boolean newNotificationEnabled``` 추가
  - PRIVATE 채널은 알림을 활성화하는 것을 기본으로 합니다.
  - PUBLIC 채널은 알림을 비활성화하는 것을 기본으로 합니다.
- [x]  알림 관련 API를 구현하세요.

| 기능                | 엔드포인트                              | 요청                                 | 응답                        |
|---------------------|----------------------------------------|--------------------------------------|-----------------------------|
| 알림 조회           | GET /api/notifications                 | Header Authorization: …              | 200 List<NotificationDto>   |
| 알림 삭제 (알림 확인) | DELETE /api/notifications/{notificationId} | Header Authorization: …              | 204 Void                    |

  - 알림 조회, 확인(삭제)은 요청자 본인의 알림에 대해서만 수행할 수 있습니다.
  - 알림 조회 및 삭제 시 현재 요청의 인증 정보로 요청자를 식별합니다.
  - 알림 확인은 삭제를 통해 수행합니다.
![image](https://github.com/user-attachments/assets/5ae72a6f-831f-4109-901c-11df059d079f)

  - ```receiverId```: 알림을 수신할 User의 id입니다.
  - ```targetId```는 optional 입니다.
- [x]  다음 상황 발생 시, 해당 사용자에게 알림을 발행하세요.
  - 사용자가 알림을 활성화한 채팅방(```ReadStatus.notificationEnabled == true```)에 메시지가 등록된 경우
    - ```type: NEW_MESSAGE, targetId: channelId```
  - 사용자의 권한(Role)이 변경된 경우
    - ```type: ROLE_CHANGED, targetId: userId```
  - 해당 사용자의 요청에서 발생한 비동기 작업이 실패하여 실패 로그가 기록된 경우
    - ```type: ASYNC_FAILED, targetId: null```
- [x]  알림은 Spring Event 기반의 비동기 방식으로 발행되도록 구현하세요.
  - ```@Async``` + ```@EventListener``` 구조로 비동기 처리하세요.
  - 이벤트 처리 중 실패가 발생할 경우, 자동 재시도 메커니즘을 도입하세요.
  - 이벤트 발행 및 리스닝은 트랜잭션 경계(transaction boundary)를 고려하여 수행하세요.
    - ```@TransactionalEventListener```

**캐시 적용하기**

- [x] ```Caffeine``` 캐시를 적용하세요.
- [x] 캐시가 필요하다고 판단되는 로직에 캐시를 적용하세요.
  - 예시:
    - 사용자별 채널 목록 조회
    - 사용자별 알림 목록 조회
    - 사용자 목록 조회
- [x] 데이터 변경 시, 캐시를 갱신 또는 무효화하는 로직을 구현하세요.
  - ```@CacheEvict```, ```@CachePut```, ```Spring Event``` 등을 활용하세요.
  - 예시:
    - 새로운 채널 추가 → 채널 목록 캐시 무효화
    - 알림 추가 → 알림 목록 캐시 무효화
    - 채널에 사용자 추가 → 사용자 목록 캐시 무효화
- [x] 캐시 적용 전후의 차이를 비교해보세요.
  - SQL 실행 횟수
  - 응답 시간


### 심화
**유의 사항**
- 이번 실습은 분산 환경을 대비한 기술 도입을 목표로 합니다. 특히, 여러 서버나 인스턴스로 구성되는 시스템에서 발생할 수 있는 문제를 해결하는 데 필요한 기술을 학습합니다.
  - Kafka를 통한 이벤트 발행/구독
  - Redis를 활용한 전역 캐시 저장소 구성
- 이번 미션에서는 Kafka, Redis의 세부 설정이나 고급 기능보다는, 기술을 간단하게 적용하고, 분산 환경의 필요성과 기존 시스템의 한계를 이해하는 데 집중해주세요.

**Spring Kafka 도입하기**

- 알림 기능이 별도의 애플리케이션으로 분리된다고 가정해봅시다.
- Spring Event를 통해 발행/소비했던 이벤트는 더 이상 활용할 수 없습니다.
- 대신, Kafka와 같은 메시지 브로커를 통해 이벤트를 애플케이션 간 발행/구독할 수 있습니다.
- [x]  Kafka 환경을 구성하세요.
  - Docker Compose를 활용해 Kafka를 구동하세요.
```
services:
  broker:
    image: apache/kafka:latest
    hostname: broker
    container_name: broker
    ports:
      - 9092:9092
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT,CONTROLLER:PLAINTEXT
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://broker:29092,PLAINTEXT_HOST://localhost:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
      KAFKA_GROUP_INITIAL_REBALANCE_DELAY_MS: 0
      KAFKA_TRANSACTION_STATE_LOG_MIN_ISR: 1
      KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR: 1
      KAFKA_PROCESS_ROLES: broker,controller
      KAFKA_NODE_ID: 1
      KAFKA_CONTROLLER_QUORUM_VOTERS: 1@broker:29093
      KAFKA_LISTENERS: PLAINTEXT://broker:29092,CONTROLLER://broker:29093,PLAINTEXT_HOST://0.0.0.0:9092
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
      KAFKA_CONTROLLER_LISTENER_NAMES: CONTROLLER
      KAFKA_LOG_DIRS: /tmp/kraft-combined-logs
      CLUSTER_ID: MkU3OEVBNTcwNTJENDM2Qk
```
- Spring Kafka 의존성을 추가하고, application.yml에 Kafka 설정을 추가하세요.
```
implementation 'org.springframework.kafka:spring-kafka'
```
```
spring:
    ...
  kafka:
    bootstrap-servers: localhost:9092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
    consumer:
      group-id: discodeit-group
      auto-offset-reset: earliest
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
```
- [x]  Spring Event → Kafka 중계 핸들러(KafkaHandler)를 구현하세요.
  - 앞서 구현한 Spring Event 발행 코드는 유지하세요.
  - ```@EventListener``` / ```@TransactionalEventListener```와 KafkaTemplate을 활용해 발행된 Spring Event를 Kafka 메시지로 변환해 전송하는 중계 구조를 구성하세요.
  - 기존에 사용하던 Spring Event 발행 코드를 유지하면서, 내부적으로 Kafka로 메시지를 전송하는 중계 구조를 구성하세요.
```
sequenceDiagram

    ApplicationEventPublisher->>+KafkaHandler: handle(MyEvent)
    KafkaHandler->>+Kafka Server: KafkaTemplate.send(topic, payload)
```
- [x]  Kafka Console을 통해 Kafka 이벤트가 잘 발행되는지 확인해보세요.
```
broker 컨테이너에 접속
➜ docker exec -it -w /opt/kafka/bin broker sh

토픽 리스트 확인 
/opt/kafka/bin $ ./kafka-topics.sh --list --bootstrap-server broker:29092

__consumer_offsets
discodeit.async-task-failed
discodeit.new-message
discodeit.role-changed

특정 토픽(discodeit.role-changed) 이벤트 수신 대기
/opt/kafka/bin $ ./kafka-console-consumer.sh --topic discodeit.role-changed --from-beginning --bootstrap-server broker:29092

{"userId":"31d44730-baba-40c0-95b4-b224f1e2cb10","oldRole":"USER","newRole":"CHANNEL_MANAGER","topic":"discodeit.role-changed"}
```
- [x]  NotificationHandler를 Kafka Consumer 기반으로 리팩토링하세요.
  - 기존 ```@EventListener``` 기반 로직을 제거하고 ```@KafkaListener```로 대체하세요.

**Redis 도입하기**
- 대용량 트래픽을 감당하기 위해 서버의 인스턴스를 여러 개로 늘렸다고 가정해봅시다.
- Caffeine과 같은 로컬 캐시는 서로 다른 서버에서 더 이상 활용할 수 없습니다.  따라서 Redis를 통해 전역 캐시 저장소를 구성합니다.
- [x]  Redis 환경을 구성하세요.
  - Docker Compose를 활용해 Redis를 구동하세요.
```
services:
  redis:
    image: redis:7.2-alpine
    container_name: redis
    ports:
      - "6379:6379"
    volumes:
      - redis-data:/data
    command: redis-server --appendonly yes
```
  - Caffeine 의존성은 삭제 / Redis 의존성을 추가하고, application.yml에 Kafka 설정을 추가하세요.

```
implementation 'com.github.ben-manes.caffeine:caffeine'
implementation 'org.springframework.boot:spring-boot-starter-data-redis'
```
```
spring:
  data:
    redis:
      host: localhost
      port: 6379

```
- [x]  DataGrip을 통해 Redis에 저장된 캐시 정보를 조회해보세요.
## 나의 설계⭐



