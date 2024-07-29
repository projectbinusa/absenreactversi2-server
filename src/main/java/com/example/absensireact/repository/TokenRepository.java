package com.example.absensireact.repository;


import com.example.absensireact.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query(value = "SELECT * FROM tokens WHERE user_id = :userId" , nativeQuery = true)
    Optional<Token> findByUserId (Long userId);

    Optional<Token> findByToken(String token);

}
