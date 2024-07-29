package com.example.absensireact.detail;

import com.example.absensireact.model.Admin;
import com.example.absensireact.model.SuperAdmin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import java.io.Serial;
import java.util.*;

public class AdminDetail implements UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;

    private String email;

    private String password;
    private  String username;
    private  String imageAdmin;

    private SuperAdmin superAdmin;

    private String role;



    public AdminDetail(Long id, String email, String password, String username, String imageAdmin, SuperAdmin superAdmin, String role ) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.imageAdmin = imageAdmin;
        this.superAdmin = superAdmin;
        this.role = role;
     }



    public static AdminDetail buildAdmin(Admin admin) {
        return new AdminDetail(
                admin.getId(),
                admin.getEmail(),
                admin.getPassword(),
                admin.getUsername(),
                admin.getImageAdmin(),
                admin.getSuperAdmin(),
                "ADMIN"
         );
     }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageAdmin() {
        return imageAdmin;
    }

    public void setImageAdmin(String imageAdmin) {
        this.imageAdmin = imageAdmin;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public SuperAdmin getSuperAdmin() {
        return superAdmin;
    }

    public void setSuperAdmin(SuperAdmin superAdmin) {
        this.superAdmin = superAdmin;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}