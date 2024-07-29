package com.example.absensireact.dto;
import java.io.Serializable;

public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwttoken;
    private final Object userData;

    public JwtResponse(String jwttoken, Object userData) {
        this.jwttoken = jwttoken;
        this.userData = userData;
    }

    public Object getUserData() {
        return userData;
    }

    public String getToken() {
        return this.jwttoken;
    }
}