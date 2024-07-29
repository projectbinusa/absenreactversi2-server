package com.example.absensireact.repository;


import com.example.absensireact.model.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationsRepository extends JpaRepository<Notifications, Long> {

    @Query(value = "SELECT * FROM notifications WHERE user_id = :userId" , nativeQuery = true)
    List<Notifications> findnotifByUserId (Long userId);

}
