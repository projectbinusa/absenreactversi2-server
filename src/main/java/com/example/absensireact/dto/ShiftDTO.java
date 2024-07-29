package com.example.absensireact.dto;

import com.example.absensireact.model.Organisasi;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;


public class ShiftDTO {
    private Long id;
    private String namaShift;

    private String waktuMasuk;

    private String waktuPulang;

    private String jumlahKaryawan;

    private Organisasi organisasi;

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

    public String getJumlahKaryawan() {
        return jumlahKaryawan;
    }

    public void setJumlahKaryawan(String jumlahKaryawan) {
        this.jumlahKaryawan = jumlahKaryawan;
    }

    public Organisasi getOrganisasi() {
        return organisasi;
    }

    public void setOrganisasi(Organisasi organisasi) {
        this.organisasi = organisasi;
    }
}
