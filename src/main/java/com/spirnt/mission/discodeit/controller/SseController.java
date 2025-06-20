package com.spirnt.mission.discodeit.controller;

import com.spirnt.mission.discodeit.security.CustomUserDetails;
import com.spirnt.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/sse")
public class SseController {

    private final NotificationService notificationService;

    @GetMapping("")
    public SseEmitter sseEmitter(@AuthenticationPrincipal CustomUserDetails customUserDetails,
        @RequestHeader(value = "Last-Event-ID") String lastEventId) {
        SseEmitter sseEmitter = notificationService.subscribe(customUserDetails.getUser().getId(),
            lastEventId);
        return sseEmitter;
    }

}
