package com.example.absensireact.model;


import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity
@Table
public class User {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "email")
   private String email;

   @Column(name = "password" , unique = true)
   private String password;

   @Column(name = "username")
   private  String username;

   @Column(name = "fotoUser")
   private String fotoUser;

   @Column(name = "startKerja")
   private String startKerja;



   @Column(name = "statusKerja")
   private String statusKerja;

   @ManyToOne
   @JoinColumn(name = "idOrganisasi")
   private Organisasi organisasi;

   @ManyToOne
   @JoinColumn(name = "idJabatan")
   private Jabatan jabatan;

   @ManyToOne
   @JoinColumn(name = "idShift")
   private Shift shift;

   @ManyToOne
   @JoinColumn(name = "idAdmin")
   private Admin admin;


   @Column(name = "role")
   private String role;

public User(){

}

    public User(Long id, String email, String password, String username, String fotoUser, String startKerja,   String statusKerja, Organisasi organisasi, Jabatan jabatan, Shift shift, Admin admin, String role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.fotoUser = fotoUser;
        this.startKerja = startKerja;
        this.statusKerja = statusKerja;
        this.organisasi = organisasi;
        this.jabatan = jabatan;
        this.shift = shift;
        this.admin = admin;
        this.role = role;
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

    public String getFotoUser() {
        return fotoUser;
    }

    public void setFotoUser(String fotoUser) {
        this.fotoUser = fotoUser;
    }

    public String getStartKerja() {
        return startKerja;
    }

    public void setStartKerja(String startKerja) {
        this.startKerja = startKerja;
    }



    public String getStatusKerja() {
        return statusKerja;
    }

    public void setStatusKerja(String statusKerja) {
        this.statusKerja = statusKerja;
    }

    public Organisasi getOrganisasi() {
        return organisasi;
    }

    public void setOrganisasi(Organisasi organisasi) {
        this.organisasi = organisasi;
    }

    public Jabatan getJabatan() {
        return jabatan;
    }

    public void setJabatan(Jabatan jabatan) {
        this.jabatan = jabatan;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


}
