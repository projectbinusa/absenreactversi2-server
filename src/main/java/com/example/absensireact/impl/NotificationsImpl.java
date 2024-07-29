package com.example.absensireact.impl;

import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.Notifications;
import com.example.absensireact.model.User;
import com.example.absensireact.repository.NotificationsRepository;
import com.example.absensireact.repository.UserRepository;
import com.example.absensireact.service.NotificationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationsImpl implements NotificationsService {

    @Autowired
    private NotificationsRepository  notificationsRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Notifications>getAllNotif(){
        return notificationsRepository.findAll();
    }

    @Override
    public List<Notifications>getNotfiUser(Long userId){
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("User id tiak ditemukan dengan id :" + userId);
        }
      return notificationsRepository.findnotifByUserId(userId);
    }

    @Override
    public Notifications tambahNotif(Long userId, String message){
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("User id tidak ditemukan dengan id :" + userId);
        }
        User user = userOptional.get();
        Date newDate = new Date();
        Notifications notifications = new Notifications();
        notifications.setCreatedAt(newDate);
        notifications.setUser(user);
        notifications.setMessage(message);
        return notificationsRepository.save(notifications);
    }

    @Override
    public Notifications editNotifById(Long id, Notifications notifications){
        Optional<Notifications> notifcationsOptional = notificationsRepository.findById(id);
        if (notifcationsOptional.isEmpty()) {
            throw new NotFoundException("Id Notif tidak ditemukan dengan id :" + id);
        }
        Date newDate = new Date();
        notifications.setMessage(notifications.getMessage());
        notifications.setCreatedAt(newDate);
        notifications.setUser(notifications.getUser());

        return notificationsRepository.save(notifications);
    }

    @Override
    public Notifications editNotifByUserId(Long userId, Notifications notifications){
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("Id User tidak ditemukan dengan id :" + userId);
        }
        Date newDate = new Date();
        User user = userOptional.get();
        notifications.setMessage(notifications.getMessage());
        notifications.setCreatedAt(newDate);
        notifications.setUser(user);

        return notificationsRepository.save(notifications);
    }

    @Override
    public boolean deleteNotif(Long id){
        Optional<Notifications> notificationsOptional = notificationsRepository.findById(id);
        if (notificationsOptional.isPresent()) {
            notificationsRepository.deleteById(id);
            return true;
        }
        return false;

    }

}
