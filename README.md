
[![codecov](https://codecov.io/gh/gitSoyoungLee/1-sprint-mission/branch/part3-%EC%9D%B4%EC%86%8C%EC%98%81-sprint8/graph/badge.svg?token=KP1S8SQKNL)](https://codecov.io/gh/gitSoyoungLee/1-sprint-mission)
[![CI workflow](https://github.com/gitSoyoungLee/1-sprint-mission/actions/workflows/test.yml/badge.svg?branch=part3-%EC%9D%B4%EC%86%8C%EC%98%81-sprint8)](https://github.com/gitSoyoungLee/1-sprint-mission/actions/workflows/test.yml)

# 🌨️ 디스코드잇

디스코드잇은 디스코드(Discord)에서 영감을 받아 제작한 실시간 채팅 서비스입니다.
완전한 복제본을 지향하지는 않았고, 스프링 백엔드 개발자로서의 역량 강화를 목표로, 각 미션별 핵심 개념과 기술을 직접 구현하며 확장시켜 나간 프로젝트입니다.

이 프로젝트는 코드잇 스프링 백엔드 1기 부트캠프에서 총 12개의 미션을 통해 단계적으로 개발되었습니다.
각 미션은 실무에서 자주 접하는 백엔드 개발자의 과제를 기반으로 설계되었으며, 미션별 브랜치에서 구체적인 구현 내용을 확인할 수 있습니다.

## 📚 미션 별 목표 및 학습 내용

| 파트 | 학습 내용 |
|------|-----------|
|미션 1-2|Java 기초 및 객체지향 설계|
|미션 3-5|Spring 기반 웹 애플리케이션|
|미션 6| JPA와 데이터베이스 연동|
|미션 7-8|운영 환경 및 CI/CD|
|미션 9-10|인증/인가|
|미션 11-12|고도화|

- 미션 1
  - Git과 GitHub을 통해 프로젝트를 관리할 수 있다.
  - 채팅 서비스의 도메인 모델을 설계하고, Java로 구현할 수 있다.
  - 인터페이스를 설계하고 구현체를 구현할 수 있다.
  - 싱글톤 패턴을 구현할 수 있다.
  - Java Collections Framework에 데이터를 생성/수정/삭제할 수 있다.
  - Stream API를 통해 JCF의 데이터를 조회할 수 있다.
- 미션 2
  - 객체 직렬화를 이용해 데이터를 다룰 수 있다.
  - 비즈니스 로직과 데이터 저장 로직에 대한 이해를 쌓는다. 
- 미션 3
  - Java 프로젝트를 Spring 프로젝트로 마이그레이션
  - 의존성 관리를 IoC Container에 위임하도록 리팩토링
  - 비즈니스 로직 고도화
- 미션 4
  - 컨트롤러 레이어 추가 및 웹 API 구현
  - Postman 테스트
- 미션 5
  - RESTful API로 재설계 및 리팩토링
  - Swagger를 활용한 API 문서 자동화
  - 프론트엔드 연동
  - PaaS(Platform as a Service)를 활용한 배포 - Railway.io 사용
  - Google Java Style Guide 적용 
- 미션 6
  - 데이터베이스 환경 설정 및 모델링
  - Spring Data JPA 환경 적용
  - Entity 연관 관계 매핑
  - 레포지토리와 서비스 계층에 JPA 적용
  - Transaction 처리
  - 페이지네이션과 정렬
  - DTO의 적극적인 도입과 MapStruct의 활용
  - BinaryContent 저장 로직 고도화
  - 메타정보와 바이너리 정보 분리
  - N+1 문제 해결
- 미션 7
  - 로그 관리
  - 커스텀 예외 설계
  - 유효성 검사
  - Actuator를 활용한 모니터링
  - 단위 테스트
  - 슬라이스 테스트
  - 통합 테스트
- 미션 8
  - 애플리케이션 컨테이너화
  - BinaryContentStorage 고도화 (AWS S3)
  - AWS를 활용한 배포 (AWS ECS, RDS)
  - CI/CD 파이프라인 구축 (GitHub Actions)
- 미션 9
  - Spring Security 환경 설정
  - 세션 기반 인증/인가
- 미션 10
  - JWT 기반 인증 / 인가
- 미션 11
  - 비동기를 통해 응답 속도 향상하기
  - 캐시를 통해 DB I/O 줄이기
  - Kafka 도입하기
  - Redis 도입하기
- 미션 12
  - 웹소켓과 SSE를 활용한 실시간 통신
