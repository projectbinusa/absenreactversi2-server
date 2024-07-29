package com.example.absensireact.controller;
import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.Notifications;
import com.example.absensireact.service.NotificationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notifications")
public class NotificationsController {

    @Autowired
    private NotificationsService notificationsService;

    @GetMapping
    public List<Notifications> getAllNotifications() {
        return notificationsService.getAllNotif();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getNotificationsByUser(@PathVariable Long userId) {
        try {
            List<Notifications> notifications = notificationsService.getNotfiUser(userId);
            return ResponseEntity.ok(notifications);
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<?> createNotification(@PathVariable Long userId, @RequestParam String message) {
        try {
            Notifications notification = notificationsService.tambahNotif(userId, message);
            return ResponseEntity.ok(notification);
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateNotification(@PathVariable Long id, @RequestBody Notifications notifications) {
        try {
            Notifications updatedNotification = notificationsService.editNotifById(id, notifications);
            return ResponseEntity.ok(updatedNotification);
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<?> updateNotificationByUserId(@PathVariable Long userId, @RequestBody Notifications notifications) {
        try {
            Notifications updatedNotification = notificationsService.editNotifByUserId(userId, notifications);
            return ResponseEntity.ok(updatedNotification);
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable Long id) {
        boolean success = notificationsService.deleteNotif(id);
        if (success) {
            return ResponseEntity.ok("Notification deleted successfully.");
        } else {
            return ResponseEntity.badRequest().body("Notification not found with ID: " + id);
        }
    }
}
