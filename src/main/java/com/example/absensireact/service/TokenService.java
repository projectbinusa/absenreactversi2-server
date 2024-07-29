package com.example.absensireact.service;

import com.example.absensireact.model.Token;


import java.util.List;
import java.util.Optional;

public interface TokenService {

    long getTokenExpirationTime(String token);

    boolean verifyTokenAndResetPassword(String tokenString, String newPassword);

    void sendResetToken(String email, String token);

    String createResetToken(String email);

    List<Token> getAllToken();

    Optional<Token> getByUser(Long userId);

    boolean deleteToken(Long id);
}
