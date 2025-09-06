package com.interior.design.controller;

import com.interior.design.domain.Notification;
import com.interior.design.dto.NotificationDtos;
import com.interior.design.service.NotificationService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/me")
    public ResponseEntity<List<Notification>> myNotifications() {
        return ResponseEntity.ok(notificationService.listForUser());
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Notification>> forProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(notificationService.listForProject(projectId));
    }

    @PostMapping("/project/{projectId}")
    @PreAuthorize("hasAnyRole('VENDOR','ADMIN')")
    public ResponseEntity<Notification> send(@PathVariable Long projectId, @RequestBody NotificationDtos.SendNotificationRequest req) {
        return ResponseEntity.ok(notificationService.notifyCustomer(projectId, req.message()));
    }
}

