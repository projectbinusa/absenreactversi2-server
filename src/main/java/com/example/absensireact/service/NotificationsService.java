package com.example.absensireact.service;

import com.example.absensireact.model.Notifications;

import java.util.List;

public interface NotificationsService {
    List<Notifications> getAllNotif();

    List<Notifications>getNotfiUser(Long userId);

    Notifications tambahNotif(Long userId, String message);

    Notifications editNotifById(Long id, Notifications notifications);

    Notifications editNotifByUserId(Long userId, Notifications notifications);

    boolean deleteNotif(Long id);
}
