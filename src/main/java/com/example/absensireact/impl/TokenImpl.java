package com.example.absensireact.impl;

import com.example.absensireact.detail.AdminDetail;
import com.example.absensireact.detail.SuperAdminDetail;
import com.example.absensireact.detail.UserDetail;
import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.Admin;
import com.example.absensireact.model.SuperAdmin;
import com.example.absensireact.model.Token;
import com.example.absensireact.model.User;
import com.example.absensireact.repository.AdminRepository;
import com.example.absensireact.repository.SuperAdminRepository;
import com.example.absensireact.repository.TokenRepository;
import com.example.absensireact.repository.UserRepository;
import com.example.absensireact.securityNew.JwtTokenUtil;
import com.example.absensireact.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenImpl implements TokenService {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private SuperAdminRepository superAdminRepository;

    @Value("${security.oauth2.authorization.jwt.expires-in}")
    private long tokenValidityInSeconds;

    @Override
    public long getTokenExpirationTime(String token) {
        String username = jwtTokenUtil.getUsernameFromToken(token);
        UserDetails userDetails = loadUserByUsername(username);
        long expiresIn = tokenValidityInSeconds;
        long issuedAt = jwtTokenUtil.extractIssuedAt(token);
        long now = System.currentTimeMillis() / 1000;
        long remainingValidity = issuedAt + expiresIn - now;
        return remainingValidity > 0 ? remainingValidity : 0;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(username);
        if (userOptional.isPresent()) {
            return UserDetail.buidUser(userOptional.get());
        }

        Optional<Admin> adminOptional = adminRepository.findByEmail(username);
        if (adminOptional.isPresent()) {
            return AdminDetail.buildAdmin(adminOptional.get());
        }

        Optional<SuperAdmin> superAdminOptional = superAdminRepository.findByEmail(username);
        if (superAdminOptional.isPresent()) {
            return SuperAdminDetail.buildSuperAdmin(superAdminOptional.get());
        }

        throw new UsernameNotFoundException("User not found with username: " + username);
    }


    @Override
    public boolean verifyTokenAndResetPassword(String tokenString, String newPassword) {
        Optional<Token> tokenOptional = tokenRepository.findByToken(tokenString);
        if (tokenOptional.isPresent()) {
            Token token = tokenOptional.get();
            User user = token.getUser();
            user.setPassword(newPassword);
            userRepository.save(user);
            tokenRepository.delete(token);
            return true;
        }
        return false;
    }

    @Override
    public void sendResetToken(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, use the following token: " + token);
        mailSender.send(message);
    }
   @Override
    public String createResetToken(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String tokenString = UUID.randomUUID().toString();
            Token token = new Token();
            token.setToken(tokenString);
            token.setUser(user);
            token.setCreated(new Date());
            tokenRepository.save(token);
            return tokenString;
        }
        return null;
    }

    @Override
    public List<Token> getAllToken(){
        return tokenRepository.findAll();
    }

    @Override
    public Optional<Token> getByUser(Long userId){
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("User tidak ditemukan dengan id : "+ userId);
        }
       return tokenRepository.findByUserId(userId);
    }


    @Override
    public boolean deleteToken(Long id) {
        Optional<Token> tokenOptional = tokenRepository.findById(id);
        if (tokenOptional.isPresent()) {
            tokenRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
