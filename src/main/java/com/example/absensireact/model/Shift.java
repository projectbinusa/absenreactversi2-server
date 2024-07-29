package com.example.absensireact.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "shift")
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "namaShift")
    private String namaShift;

    @Column(name = "waktuMasuk")
    private String waktuMasuk;

    @Column(name = "waktuPulang")
    private String waktuPulang;



    @OneToOne
    @JoinColumn(name = "idAdmin")
    private Admin admin;


    public Shift(){

    }

    public Shift(Long id, String namaShift, String waktuMasuk, String waktuPulang, Admin admin) {
        this.id = id;
        this.namaShift = namaShift;
        this.waktuMasuk = waktuMasuk;
        this.waktuPulang = waktuPulang;
        this.admin = admin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNamaShift() {
        return namaShift;
    }

    public void setNamaShift(String namaShift) {
        this.namaShift = namaShift;
    }

    public String getWaktuMasuk() {
        return waktuMasuk;
    }

    public void setWaktuMasuk(String waktuMasuk) {
        this.waktuMasuk = waktuMasuk;
    }

    public String getWaktuPulang() {
        return waktuPulang;
    }

    public void setWaktuPulang(String waktuPulang) {
        this.waktuPulang = waktuPulang;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
}
