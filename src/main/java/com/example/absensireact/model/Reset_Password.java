package com.example.absensireact.model;
import javax.persistence.*;

@Entity
@Table(name = "reset_password")
public class Reset_Password {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private  String email = "";


    @Column(name = "status")
    private int status = 1;

    @Column(name = "code_verification")
    private String code;

    public Reset_Password() {
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }



    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
