package com.spirnt.mission.discodeit.service.basic;

import com.spirnt.mission.discodeit.async.notification.NotificationCreateEvent;
import com.spirnt.mission.discodeit.cache.event.NotificationEvictEvent;
import com.spirnt.mission.discodeit.dto.notification.NotificationDto;
import com.spirnt.mission.discodeit.entity.Notification;
import com.spirnt.mission.discodeit.entity.User;
import com.spirnt.mission.discodeit.exception.DiscodeitException;
import com.spirnt.mission.discodeit.exception.ErrorCode;
import com.spirnt.mission.discodeit.mapper.NotificationMapper;
import com.spirnt.mission.discodeit.repository.NotificationRepository;
import com.spirnt.mission.discodeit.service.NotificationService;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicNotificationService implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final ApplicationEventPublisher eventPublisher;
    // 사용자별 SSE 연결(SseEmitter) 관리하는 맵 
    // ConcurrentHashMap으로 스레드 세이프한 메모리 구조에서 관리
    // N개의 연결 허용하기 위해 SseEmitter를 List로 관리
    private final Map<UUID, List<SseEmitter>> userConnections = new ConcurrentHashMap<>();

    @Transactional
    public List<NotificationDto> create(NotificationCreateEvent event) {
        List<Notification> notifications = event.receivers().stream()
            .map(user -> new Notification(user, event.title(), event.content(), event.type(),
                event.targetId()))
            .collect(Collectors.toList());
        notificationRepository.saveAll(notifications);

        // 캐시 무효화를 위한 이벤트 발행
        eventPublisher.publishEvent(
            new NotificationEvictEvent(event.receivers().stream()
                .map(User::getId)
                .collect(Collectors.toList()))
        );

        return notificationMapper.toDto(notifications);
    }

    @Cacheable(
        cacheNames = "notifications",
        key = "#userId"
    )
    @Transactional(readOnly = true)
    @Override
    public List<NotificationDto> findAll(UUID userId) {
        List<Notification> notifications = notificationRepository.findAllByReceiverId(userId);
        return notificationMapper.toDto(notifications);
    }

    @Transactional
    @Override
    public void delete(UUID notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new DiscodeitException(ErrorCode.NOTIFICATION_NOT_FOUND, Map.of()));

        // 캐시 무효화를 위한 이벤트 발행
        eventPublisher.publishEvent(
            new NotificationEvictEvent(List.of(notification.getReceiver().getId()))
        );

        notificationRepository.delete(notification);
    }

    /**
     * SSE 연결 = 알림 구독 사용자의 ID를 기준으로 그 사용자를 위한 SSE 연결 통로(SseEmitter) 생성
     */
    @Override
    public SseEmitter subscribe(UUID userId, String lastEventId) {

        // SSE 생성 - 타임아웃 30분
        SseEmitter newEmitter = new SseEmitter(30 * 60 * 1000L);
        // 맵 업데이트
        /**
         * 새롭게 알게 된 점.
         * sse는 멀티스레드 환경에서 작동
         * -> 일반 new ArrayList를 생성하면 ConcurrentModificationException 발생
         * -> CopyWriteArrayList로 읽기 작업은 락 없이 빠르게, 쓰기는 복사 해서 안전하게 처리
         */
        List<SseEmitter> sseEmitters = userConnections.computeIfAbsent(userId,
            key -> new CopyOnWriteArrayList<>());   // 맵에 userId 키가 존재하면 반환하고, 없으면 새로운 빈 리스트 반환
        sseEmitters.add(newEmitter);

        // 메모리 누수 방지
        newEmitter.onCompletion(() -> removeEmitter(userId, newEmitter)); // 클라이언트가 정상적으로 연결을 종료했을 때
        newEmitter.onTimeout(
            () -> removeEmitter(userId, newEmitter));    // SseEmitter가 타임아웃되어 연결이 자동 종료될 때
        newEmitter.onError((e) -> removeEmitter(userId, newEmitter)); // 에러

        // 연결 확인용 메시지 전송
        try {
            newEmitter.send(SseEmitter.event()
                .name("Connected")
                .id(UUID.randomUUID().toString())   // 고유 ID 할당
                .data("Notification Service Connection Success"));
        } catch (IOException e) {
            newEmitter.completeWithError(e);
        }

        // TODO: 이벤트 유실 복원
        // 클라이언트가 마지막으로 받은 이벤트의 ID를 기억하고,
        // 서버와 다시 연결하면 서버는 그 이후 이벤트들을 다시 보내줌

        return newEmitter;
    }

    private void removeEmitter(UUID userId, SseEmitter emitter) {
        List<SseEmitter> sseEmitters = userConnections.get(userId);
        if (sseEmitters == null) {
            return;
        }

        sseEmitters.remove(emitter);    // 연결 리스트에서 삭제
        if (sseEmitters.isEmpty()) {
            userConnections.remove(userId); // 연결 리스트가 비어있으면 맵에서 삭제
        }
    }

    // 주기적으로 ping 보내서 응답이 없는 연결 정리
    @Scheduled(fixedRate = 30000)   // 30초마다 ping 전송
    @Override
    public void sendPingToAllConnections() {
        userConnections.keySet().stream()
            .forEach(userId -> {
                List<SseEmitter> emitters = userConnections.get(userId);
                if (emitters == null) {
                    return;
                }

                for (SseEmitter emitter : emitters) {
                    try {
                        emitter.send(SseEmitter.event()
                            .name("ping")
                            .data("ping"));
                    } catch (IOException e) {
                        emitter.completeWithError(e);
                        // 실패 = 응답 없는 emitter 정리
                        removeEmitter(userId, emitter);
                    }
                }
            });
    }
}
