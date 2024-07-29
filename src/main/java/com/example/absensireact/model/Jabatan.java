package com.example.absensireact.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "jabatan")
public class Jabatan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idJabatan;

    @Column(name = "nama_jabatan")
    private String namaJabatan;

    @Column(name = "jumlahKaryawan")
    private String jumlahKaryawan;

     @ManyToOne
    @JoinColumn(name = "adminId")
    private Admin admin;



    public Jabatan(){

    }
    public Jabatan(Long idJabatan, String namaJabatan, String jumlahKaryawan, Admin admin  ) {
        this.idJabatan = idJabatan;
        this.namaJabatan = namaJabatan;
        this.jumlahKaryawan = jumlahKaryawan;
        this.admin = admin;
     }

    public Long getIdJabatan() {
        return idJabatan;
    }

    public void setIdJabatan(Long idJabatan) {
        this.idJabatan = idJabatan;
    }

    public String getNamaJabatan() {
        return namaJabatan;
    }

    public void setNamaJabatan(String namaJabatan) {
        this.namaJabatan = namaJabatan;
    }

    public Admin getAdmin() {
        return admin;
    }

    public String getJumlahKaryawan() {
        return jumlahKaryawan;
    }

    public void setJumlahKaryawan(String jumlahKaryawan) {
        this.jumlahKaryawan = jumlahKaryawan;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }


}
