package com.example.absensireact.dto;

public class LokasiDTO {
    private String namaLokasi;
    private String alamat;
    private Long idOrganisasi;
    private AdminDTO admin;
    private Long adminId;

    public LokasiDTO(String namaLokasi, String alamat, Long idOrganisasi, AdminDTO admin, Long adminId) {
        this.namaLokasi = namaLokasi;
        this.alamat = alamat;
        this.idOrganisasi = idOrganisasi;
        this.admin = admin;
        this.adminId = adminId;
    }

    public LokasiDTO() {

    }

    public String getNamaLokasi() {
        return namaLokasi;
    }

    public void setNamaLokasi(String namaLokasi) {
        this.namaLokasi = namaLokasi;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public Long getIdOrganisasi() {
        return idOrganisasi;
    }

    public void setIdOrganisasi(Long idOrganisasi) {
        this.idOrganisasi = idOrganisasi;
    }

    public AdminDTO getAdmin() {
        return admin;
    }

    public void setAdmin(AdminDTO admin) {
        this.admin = admin;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }
}
