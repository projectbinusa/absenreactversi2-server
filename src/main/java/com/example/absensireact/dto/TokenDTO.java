package com.example.absensireact.dto;

import com.example.absensireact.model.User;

import javax.persistence.*;
import java.util.Date;

public class TokenDTO {
    private Long id;
    private String token;

    private User user;
    private Date created;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
