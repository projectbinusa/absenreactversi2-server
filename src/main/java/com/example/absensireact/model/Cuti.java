package com.example.absensireact.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "cuti")
public class Cuti {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "awalCuti")
    private String awalCuti;

    @Column(name = "akhirCuti")
    private String akhirCuti;

    @Column(name = "masukKerja")
    private String masukKerja;

    @Column(name = "keperluan")
    private String keperluan;

    @Column(name = "status")
    private String status;


    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    @OneToOne
    @JoinColumn(name = "idOrganisasi")
    private Organisasi organisasi;

    public Cuti(){

    }

    public Cuti(Long id, String awalCuti, String akhirCuti, String masukKerja, String keperluan, String status, User user, Organisasi organisasi) {
        this.id = id;
        this.awalCuti = awalCuti;
        this.akhirCuti = akhirCuti;
        this.masukKerja = masukKerja;
        this.keperluan = keperluan;
        this.status = status;
        this.user = user;
        this.organisasi = organisasi;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAwalCuti() {
        return awalCuti;
    }

    public void setAwalCuti(String awalCuti) {
        this.awalCuti = awalCuti;
    }

    public String getAkhirCuti() {
        return akhirCuti;
    }

    public void setAkhirCuti(String akhirCuti) {
        this.akhirCuti = akhirCuti;
    }

    public String getMasukKerja() {
        return masukKerja;
    }

    public void setMasukKerja(String masukKerja) {
        this.masukKerja = masukKerja;
    }

    public String getKeperluan() {
        return keperluan;
    }

    public void setKeperluan(String keperluan) {
        this.keperluan = keperluan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public Organisasi getOrganisasi() {
        return organisasi;
    }

    public void setOrganisasi(Organisasi organisasi) {
        this.organisasi = organisasi;
    }
}
