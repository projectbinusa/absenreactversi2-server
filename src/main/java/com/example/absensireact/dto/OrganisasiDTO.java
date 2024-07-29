package com.example.absensireact.dto;

import com.example.absensireact.model.Admin;

public class OrganisasiDTO {
    private Long id;
    private String namaOrganisasi;
    private String alamat;
    private String nomerTelepon;
    private String emailOrganisasi;
    private String kecamatan;
    private String kabupaten;
    private String provinsi;
    private String status;
    private String fotoOrganisasi;
    private String admin;

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
    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

}
