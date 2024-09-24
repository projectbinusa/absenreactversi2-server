package com.example.absensireact.controller;
import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.Token;
import com.example.absensireact.securityNew.JwtTokenUtil;
import com.example.absensireact.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tokens")
@CrossOrigin(origins = "*")
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping("/expiration")
    public long getTokenExpirationTime(@RequestParam  String token) {
        return tokenService.getTokenExpirationTime(token);
    }

    @PostMapping("/forgot")
    public ResponseEntity<?> forgotPassword(@RequestParam Long userId, @RequestParam String email) {
        String token = tokenService.createResetToken(String.valueOf(userId));
        if (token != null) {
            tokenService.sendResetToken(email, token);
            return ResponseEntity.ok("Reset token sent to your email.");
        }
        return ResponseEntity.badRequest().body("User not found.");
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        boolean success = tokenService.verifyTokenAndResetPassword(token, newPassword);
        if (success) {
            return ResponseEntity.ok("Password has been reset.");
        }
        return ResponseEntity.badRequest().body("Invalid token.");
    }

    @GetMapping("all-token")
    public ResponseEntity<?>getAllTokens (){
        List<Token> tokenList = tokenService.getAllToken();
        return ResponseEntity.ok(tokenList);
    }

    @GetMapping("/get-token/{userId}")
    public ResponseEntity<?>getTokenByUser(@PathVariable Long userId){
      Optional<Token> tokenOptional = Optional.ofNullable(tokenService.getByUser(userId)
              .orElseThrow(() -> new NotFoundException("User not found with id : " + userId)));
        if (tokenOptional.isPresent()) {
            return ResponseEntity.ok(tokenOptional);
        }
        throw new NotFoundException("Token Notfound");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteToken(@PathVariable Long id) {
        boolean success = tokenService.deleteToken(id);
        if (success) {
            return ResponseEntity.ok("Token deleted successfully.");
        }
        return ResponseEntity.badRequest().body("Token not found with ID: " + id);
    }
}
