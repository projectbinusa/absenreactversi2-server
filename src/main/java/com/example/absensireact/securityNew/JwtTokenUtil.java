package com.example.absensireact.securityNew;


import com.example.absensireact.detail.AdminDetail;
import com.example.absensireact.detail.SuperAdminDetail;
import com.example.absensireact.detail.UserDetail;
import com.example.absensireact.repository.AdminRepository;
import com.example.absensireact.repository.SuperAdminRepository;
import com.example.absensireact.repository.UserRepository;
import io.jsonwebtoken.*;
 import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenUtil implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);



    private String jwtSecret = "absensi";
    private int jwtExpirationMs = 604800000;

    private static final String SECRET_KEY = "absensi";


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private SuperAdminRepository superAdminRepository;


    public static Claims decodeJwt(String jwtToken) {
        Jws<Claims> jwsClaims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(jwtToken);

        return jwsClaims.getBody();
    }


    public static long extractIssuedAt(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        Date issuedAt = claims.getIssuedAt();
        return issuedAt.getTime();
    }

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("id", getUserIdFromUserDetails(userDetails))
                .claim("role", getRoleFromUserDetails(userDetails))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    private Long getUserIdFromUserDetails(UserDetails userDetails) {
        if (userDetails instanceof UserDetail) {
            return ((UserDetail) userDetails).getId();
        } else if (userDetails instanceof AdminDetail) {
            return ((AdminDetail) userDetails).getId();
        } else if (userDetails instanceof SuperAdminDetail) {
            return ((SuperAdminDetail) userDetails).getId();
        }
        return null;
    }

    private String getRoleFromUserDetails(UserDetails userDetails) {
        if (userDetails instanceof UserDetail) {
            return "USER";
        } else if (userDetails instanceof AdminDetail) {
            return "ADMIN";
        } else if (userDetails instanceof SuperAdminDetail) {
            return "SUPERADMIN";
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }
}