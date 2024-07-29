package com.example.absensireact.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProfileAdminDTO {
    private String email;
    private String username;

    @JsonCreator
    public ProfileAdminDTO(@JsonProperty("email") String email, @JsonProperty("username") String username) {
        this.email = email;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
