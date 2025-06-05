package com.spirnt.mission.discodeit.service.basic;

import com.spirnt.mission.discodeit.async.notification.NotificationCreateEvent;
import com.spirnt.mission.discodeit.dto.notification.NotificationDto;
import com.spirnt.mission.discodeit.entity.Notification;
import com.spirnt.mission.discodeit.mapper.NotificationMapper;
import com.spirnt.mission.discodeit.repository.NotificationRepository;
import com.spirnt.mission.discodeit.service.NotificationService;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicNotificationService implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Transactional
    public List<NotificationDto> create(NotificationCreateEvent event) {
        List<Notification> notifications = event.receivers().stream()
            .map(user -> new Notification(user, event.title(), event.content(), event.type(),
                event.targetId()))
            .collect(Collectors.toList());
        notificationRepository.saveAll(notifications);

        return notificationMapper.toDto(notifications);
    }

    @Override
    public List<NotificationDto> findAll(UUID userId) {
        List<Notification> notifications = notificationRepository.findAllByReceiverId(userId);
        return notificationMapper.toDto(notifications);
    }

    @Transactional
    @Override
    public void delete(UUID notificationId) {
        notificationRepository.deleteById(notificationId);
    }
}
