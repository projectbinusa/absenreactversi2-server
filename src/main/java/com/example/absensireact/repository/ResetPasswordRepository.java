package com.example.absensireact.repository;

import com.example.absensireact.model.Reset_Password;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetPasswordRepository extends JpaRepository<Reset_Password ,Long> {

    @Query(value = "SELECT * FROM reset_password  WHERE email = :email AND code_verification = :code", nativeQuery = true)
    Optional<Reset_Password> findByEmailandCode(String email , String code);
}
