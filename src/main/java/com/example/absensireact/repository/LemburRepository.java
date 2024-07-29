package com.example.absensireact.repository;

import com.example.absensireact.model.Lembur;
import com.example.absensireact.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LemburRepository extends JpaRepository<Lembur , Long> {

    @Query(value = "SELECT * FROM lembur WHERE user_id = :userId" , nativeQuery = true)
    List<Lembur> findByuserId(Long userId);

    List<Lembur>findByUser(User user);
}
