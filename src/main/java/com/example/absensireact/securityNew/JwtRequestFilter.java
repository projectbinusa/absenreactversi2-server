package com.example.absensireact.securityNew;

import com.example.absensireact.detail.CustomUserDetails;
import com.example.absensireact.repository.AdminRepository;
import com.example.absensireact.repository.SuperAdminRepository;
import com.example.absensireact.repository.UserRepository;
import com.example.absensireact.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private SuperAdminRepository superAdminRepository;

    @Autowired
    private CustomUserDetails customUserDetails;

    @Autowired
    JwtTokenUtil jwtTokenUtil;


    private String jwtSecret = "absensi";


    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);


//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        final String authorizationHeader = request.getHeader("Authorization");
//
//        String username = null;
//        String jwtToken = null;
//
//        if (authorizationHeader != null  ) {
//            jwtToken = authorizationHeader;
//            username = jwtTokenUtil.getUsernameFromToken(jwtToken);
//        }
//
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            if (jwtTokenUtil.validateToken(jwtToken)) {
//                UserDetails user = customUserDetails.loadUserByUsername(username);
//                 UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
//                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
////                List<GrantedAuthority> authorities = getRolesFromToken(jwtToken);
////                UserDetails userDetails = new org.springframework.security.core.userdetails.User(username, "", authorities);
////                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
//                SecurityContextHolder.getContext().setAuthentication(auth);
//            } else {
//                SecurityContextHolder.clearContext();
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7);
            username = jwtTokenUtil.getUsernameFromToken(jwtToken);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtTokenUtil.validateToken(jwtToken)) {
                UserDetails user = customUserDetails.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                List<GrantedAuthority> authorities = getRolesFromToken(jwtToken);
//                UserDetails userDetails = new org.springframework.security.core.userdetails.User(username, "", authorities);
//                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
//    private List<GrantedAuthority> getRolesFromToken(String role) {
//        List<GrantedAuthority> roles = new ArrayList<>();
//        if (role != null) {
//            String[] rolesArray = role.split(",");
//            for (String roleString : rolesArray) {
//                roles.add(new SimpleGrantedAuthority(roleString));
//            }
//        }
//        return roles;
//    }
//
//
//    private static final String AUTH_HEADER_NAME = "Authorization";
//    private static final String JWT_PREFIX = "Bearer ";
//
//    private String parseJwt(HttpServletRequest request) {
//        String headerAuth = request.getHeader(AUTH_HEADER_NAME);
//        if (headerAuth != null && StringUtils.hasText(headerAuth) && headerAuth.startsWith(JWT_PREFIX)) {
//            return headerAuth.substring(JWT_PREFIX.length());
//        }
//        return "Errororroror";
//    }

}