package com.example.absensireact.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "lembur")
public class Lembur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nama")
    private String nama ;

    @Column(name = "tanggalLembur")
    private String tanggalLembur;

    @Column(name = "jamMulai")
    private String jamMulai;

    @Column(name = "jamSelesai")
    private String jamSelesai;

    @Column(name = "keteranganLembur")
    private String keteranganLembur;

     @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    public Lembur (){

    }

    public Lembur(Long id, String tanggalLembur, String jamMulai, String jamSelesai, String keteranganLembur, User user , String nama) {
        this.id = id;
        this.tanggalLembur = tanggalLembur;
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
        this.keteranganLembur = keteranganLembur;
        this.user = user;
        this.nama = nama;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTanggalLembur() {
        return tanggalLembur;
    }

    public void setTanggalLembur(String tanggalLembur) {
        this.tanggalLembur = tanggalLembur;
    }

    public String getJamMulai() {
        return jamMulai;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setJamMulai(String jamMulai) {
        this.jamMulai = jamMulai;
    }

    public String getJamSelesai() {
        return jamSelesai;
    }

    public void setJamSelesai(String jamSelesai) {
        this.jamSelesai = jamSelesai;
    }

    public String getKeteranganLembur() {
        return keteranganLembur;
    }

    public void setKeteranganLembur(String keteranganLembur) {
        this.keteranganLembur = keteranganLembur;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
