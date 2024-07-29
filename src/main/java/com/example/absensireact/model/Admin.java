package com.example.absensireact.model;


import javax.persistence.*;

@Entity
@Table(name = "admin")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password" , unique = true)
    private String password;

    @Column(name = "username")
    private  String username;

    @Column(name = "imageAdmin"  )
    private  String imageAdmin;


    @Column(name = "role")
    private String role;


    @ManyToOne
    @JoinColumn(name = "idSuperAdmin")
    private SuperAdmin superAdmin;

   public Admin(){

   }

    public Admin(Long id, String email, String password, String username, String imageAdmin, String role,  SuperAdmin superAdmin) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.imageAdmin = imageAdmin;
        this.role = role;
//        this.organisasi = organisasi;
        this.superAdmin = superAdmin;
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
}
