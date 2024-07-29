package com.example.absensireact.dto;

import javax.persistence.Column;

public class SuperAdminDTO {
    private Long id;

    private String email;

    private String password;

    private  String username;

    private  String imageSuperAdmin;

    private String role;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageSuperAdmin() {
        return imageSuperAdmin;
    }

    public void setImageSuperAdmin(String imageSuperAdmin) {
        this.imageSuperAdmin = imageSuperAdmin;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
