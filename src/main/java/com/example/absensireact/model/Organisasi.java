package com.example.absensireact.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Table(name = "organisasi")
public class Organisasi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "namaOrganisasi")
    private String namaOrganisasi;

    @Column(name = "alamat")
    private String alamat;

    @Column(name = "nomerTelepon")
    private String nomerTelepon;

    @Column(name = "emailOrganisasi")
    private String emailOrganisasi;

    @Column(name = "kecamatan")
    private String kecamatan;

    @Column(name = "kabupaten")
    private String kabupaten;

    @Column(name = "provinsi")
    private String provinsi;

    @Column(name = "status")
    private String status;

    @Column(name = "fotoOrganisasi")
    private String fotoOrganisasi;


    @OneToOne
    @JoinColumn(name = "idAdmin")
    private Admin admin;

    public Organisasi(){


    }

    public Organisasi(Long id, String namaOrganisasi, String alamat, String nomerTelepon, String emailOrganisasi, String kecamatan, String kabupaten, String provinsi, String status, String fotoOrganisasi, Admin admin) {
        this.id = id;
        this.namaOrganisasi = namaOrganisasi;
        this.alamat = alamat;
        this.nomerTelepon = nomerTelepon;
        this.emailOrganisasi = emailOrganisasi;
        this.kecamatan = kecamatan;
        this.kabupaten = kabupaten;
        this.provinsi = provinsi;
        this.status = status;
        this.fotoOrganisasi = fotoOrganisasi;
        this.admin = admin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNamaOrganisasi() {
        return namaOrganisasi;
    }

    public void setNamaOrganisasi(String namaOrganisasi) {
        this.namaOrganisasi = namaOrganisasi;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNomerTelepon() {
        return nomerTelepon;
    }

    public void setNomerTelepon(String nomerTelepon) {
        this.nomerTelepon = nomerTelepon;
    }

    public String getEmailOrganisasi() {
        return emailOrganisasi;
    }

    public void setEmailOrganisasi(String emailOrganisasi) {
        this.emailOrganisasi = emailOrganisasi;
    }

    public String getKecamatan() {
        return kecamatan;
    }

    public void setKecamatan(String kecamatan) {
        this.kecamatan = kecamatan;
    }

    public String getKabupaten() {
        return kabupaten;
    }

    public void setKabupaten(String kabupaten) {
        this.kabupaten = kabupaten;
    }

    public String getProvinsi() {
        return provinsi;
    }

    public void setProvinsi(String provinsi) {
        this.provinsi = provinsi;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFotoOrganisasi() {
        return fotoOrganisasi;
    }

    public void setFotoOrganisasi(String fotoOrganisasi) {
        this.fotoOrganisasi = fotoOrganisasi;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }


}
