package com.example.absensireact.service;

import com.example.absensireact.dto.AdminDTO;
import com.example.absensireact.dto.LokasiDTO;
import com.example.absensireact.dto.OrganisasiDTO;
import com.example.absensireact.model.Lokasi;

import java.util.List;
import java.util.Optional;

public interface LokasiService {


//    LokasiDTO saveLokasi(LokasiDTO lokasiDTO);

    List<Lokasi>getAllBySuperAdmin(Long idSuperAdmin);

    List<Lokasi>getAllByAdmin(Long idAdmin);

    Lokasi tambahLokasi(Long idAdmin, Lokasi lokasi, Long idOrganisasi);

    Lokasi tambahLokasiBySuperAdmin(Long idSuperAdmin, Lokasi lokasi, Long idOrganisasi);

    List<LokasiDTO> getAllLokasi();

    LokasiDTO getLokasiById(Long id);


    Optional<Lokasi> getByIdLokasi(Long idLokasi);

    LokasiDTO updateLokasi(Long id, LokasiDTO lokasiDTO);


    void deleteLokasi(Long id);

    OrganisasiDTO getOrganisasiById(Long id);

    AdminDTO getAdminById(Long id);

    Lokasi updateLokasiByIdlokasi(Long idLokasi, Lokasi lokasiDetails);
}

