package com.example.absensireact.dto;

import com.example.absensireact.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

public class AbsensiDTO {


    private Long id;

    private Date tanggalAbsen;

    private String jamMasuk;

    private String jamPulang;

    private String lokasiMasuk;

    private String lokasiPulang;

    private String fotoMasuk;

    private String fotoPulang;

    private String keterangan;



    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTanggalAbsen() {
        return tanggalAbsen;
    }

    public void setTanggalAbsen(Date tanggalAbsen) {
        this.tanggalAbsen = tanggalAbsen;
    }

    public String getJamMasuk() {
        return jamMasuk;
    }

    public void setJamMasuk(String jamMasuk) {
        this.jamMasuk = jamMasuk;
    }

    public String getJamPulang() {
        return jamPulang;
    }

    public void setJamPulang(String jamPulang) {
        this.jamPulang = jamPulang;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLokasiMasuk() {
        return lokasiMasuk;
    }

    public void setLokasiMasuk(String lokasiMasuk) {
        this.lokasiMasuk = lokasiMasuk;
    }

    public String getLokasiPulang() {
        return lokasiPulang;
    }

    public void setLokasiPulang(String lokasiPulang) {
        this.lokasiPulang = lokasiPulang;
    }

    public String getFotoMasuk() {
        return fotoMasuk;
    }

    public void setFotoMasuk(String fotoMasuk) {
        this.fotoMasuk = fotoMasuk;
    }

    public String getFotoPulang() {
        return fotoPulang;
    }

    public void setFotoPulang(String fotoPulang) {
        this.fotoPulang = fotoPulang;
    }
}
