package com.example.absensireact.impl;

import com.example.absensireact.dto.LokasiDTO;
import com.example.absensireact.dto.AdminDTO;
import com.example.absensireact.dto.OrganisasiDTO;
import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.Admin;
import com.example.absensireact.model.Lokasi;
import com.example.absensireact.model.Organisasi;
import com.example.absensireact.model.SuperAdmin;
import com.example.absensireact.repository.AdminRepository;
import com.example.absensireact.repository.LokasiRepository;
import com.example.absensireact.repository.OrganisasiRepository;
import com.example.absensireact.repository.SuperAdminRepository;
import com.example.absensireact.service.LokasiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LokasiImpl implements LokasiService {

    @Autowired
    private LokasiRepository lokasiRepository;

    @Autowired
    private OrganisasiRepository organisasiRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private SuperAdminRepository superAdminRepository;


    @Override
    public  List<Lokasi>getAllBySuperAdmin(Long idSuperAdmin){
        Optional<SuperAdmin> superAdminOptional = superAdminRepository.findById(idSuperAdmin);
        if (superAdminOptional.isEmpty()) {
            throw new NotFoundException("ID Super Admin tidak ditemukan: " + idSuperAdmin);
        }
        SuperAdmin superAdmin = superAdminOptional.get();

        List<Admin> adminList = adminRepository.findBySuperAdmin(superAdmin);

        List<Lokasi> lokasiList = new ArrayList<>();

        for (Admin admin : adminList) {
            List<Lokasi> adminLokasiList = lokasiRepository.findByAdmin(admin);
            lokasiList.addAll(adminLokasiList);
        }

        return lokasiList;
    }
//    @Override
//    public LokasiDTO saveLokasi(LokasiDTO lokasiDTO) {
//        Lokasi lokasi = convertToEntity(lokasiDTO);
//
//        // Ambil entitas Organisasi dari basis data berdasarkan ID yang diberikan
//        Optional<Organisasi> organisasiOptional = organisasiRepository.findById(lokasiDTO.getIdOrganisasi());
//        organisasiOptional.ifPresent(lokasi::setOrganisasi);
//
//        // Jika ada adminId yang diberikan, ambil entitas Admin dari basis data
//        if (lokasiDTO.getAdminId() != null) {
//            Optional<Admin> adminOptional = adminRepository.findById(lokasiDTO.getAdminId());
//            adminOptional.ifPresent(lokasi::setAdmin);
//        }
//
//        lokasi = lokasiRepository.save(lokasi);
//        return convertToDto(lokasi);
//    }


    @Override
    public List<Lokasi>getAllByAdmin(Long idAdmin){
        return lokasiRepository.findbyAdmin(idAdmin);
    }
    @Override
    public Lokasi tambahLokasi(Long idAdmin, Lokasi lokasi, Long idOrganisasi) {
        Optional<Admin> adminOptional = adminRepository.findById(idAdmin);
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            Optional<Organisasi> organisasiOptional = organisasiRepository.findById(idOrganisasi);
            if (organisasiOptional.isPresent()) {
                Organisasi organisasi = organisasiOptional.get();
                lokasi.setNamaLokasi(lokasi.getNamaLokasi());
                lokasi.setAlamat(lokasi.getAlamat());
                lokasi.setAdmin(admin);
                lokasi.setOrganisasi(organisasi);
                return lokasiRepository.save(lokasi);
            } else {
                throw new NotFoundException("Organisasi dengan ID " + idOrganisasi + " tidak ditemukan.");
            }
        } else {
            throw new NotFoundException("Admin dengan ID " + idAdmin + " tidak ditemukan.");
        }
    }

     @Override
     public Lokasi tambahLokasiBySuperAdmin(Long idSuperAdmin, Lokasi lokasi, Long idOrganisasi) {
         Optional<SuperAdmin> superadminOptional = superAdminRepository.findById(idSuperAdmin);
         if (superadminOptional.isPresent()) {
             SuperAdmin superAdmin = superadminOptional.get();
             Optional<Organisasi> organisasiOptional = organisasiRepository.findById(idOrganisasi);
             if (organisasiOptional.isPresent()) {
                 Organisasi organisasi = organisasiOptional.get();
                 lokasi.setNamaLokasi(lokasi.getNamaLokasi());
                 lokasi.setAlamat(lokasi.getAlamat());
                 lokasi.setAdmin(organisasi.getAdmin());
                 lokasi.setOrganisasi(organisasi);
                 return lokasiRepository.save(lokasi);
             } else {
                 throw new NotFoundException("Organisasi dengan ID " + idOrganisasi + " tidak ditemukan.");
             }
         } else {
             throw new NotFoundException("Super admi dengan ID " + idSuperAdmin + " tidak ditemukan.");
         }
     }

    @Override
    public List<LokasiDTO> getAllLokasi() {
        return lokasiRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public LokasiDTO getLokasiById(Long id) {
        Optional<Lokasi> lokasi = lokasiRepository.findById(id);
        return lokasi.map(this::convertToDto).orElse(null);
    }

    @Override
    public Optional<Lokasi> getByIdLokasi(Long idLokasi){
        return lokasiRepository.findById(idLokasi);
    }
    @Override
    public LokasiDTO updateLokasi(Long id, LokasiDTO lokasiDTO) {
        return lokasiRepository.findById(id).map(existingLokasi -> {
            updateEntity(existingLokasi, lokasiDTO);
            lokasiRepository.save(existingLokasi);
            return convertToDto(existingLokasi);
        }).orElse(null);
    }

    @Override
    public void deleteLokasi(Long idLokasi) {
        lokasiRepository.deleteById(idLokasi);
    }


    @Override
    public OrganisasiDTO getOrganisasiById(Long id) {
        Optional<Organisasi> lokasi = organisasiRepository.findById(id);
        return lokasi.map(this::convertOrganisasiToDto).orElse(null);
    }

    @Override
    public AdminDTO getAdminById(Long id) {
        Optional<Admin> adminOptional = adminRepository.findById(id);
        if (adminOptional.isPresent()) {
            return convertAdminToDto(adminOptional.get());
        } else {
            return null;
        }
    }

    @Override
    public Lokasi updateLokasiByIdlokasi(Long idLokasi, Lokasi lokasiDetails) {
        return lokasiRepository.findById(idLokasi).map(lokasi -> {
            lokasi.setNamaLokasi(lokasiDetails.getNamaLokasi());
            lokasi.setAlamat(lokasiDetails.getAlamat());
            return lokasiRepository.save(lokasi);
        }).orElseThrow(() -> new NotFoundException("Lokasi not found with id " + idLokasi));
    }
    private OrganisasiDTO convertOrganisasiToDto(Organisasi organisasi) {
        OrganisasiDTO organisasiDTO = new OrganisasiDTO();
        organisasiDTO.setId(organisasi.getId());
        organisasiDTO.setNamaOrganisasi(organisasi.getNamaOrganisasi());
        organisasiDTO.setAlamat(organisasi.getAlamat());
        organisasiDTO.setNomerTelepon(organisasi.getNomerTelepon());
        organisasiDTO.setEmailOrganisasi(organisasi.getEmailOrganisasi());
        organisasiDTO.setKecamatan(organisasi.getKecamatan());
        organisasiDTO.setKabupaten(organisasi.getKabupaten());
        organisasiDTO.setProvinsi(organisasi.getProvinsi());
        organisasiDTO.setStatus(organisasi.getStatus());
        organisasiDTO.setFotoOrganisasi(organisasi.getFotoOrganisasi());

        // Set Admin ID jika ada admin terkait
        Admin admin = organisasi.getAdmin();
        if (admin != null) {
            organisasiDTO.setAdmin(String.valueOf(admin.getId()));
        }

        return organisasiDTO;
    }




 
 
    private AdminDTO convertAdminToDto(Admin admin) {
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setId(admin.getId());
        adminDTO.setEmail(admin.getEmail());
        adminDTO.setPassword(admin.getPassword());
        adminDTO.setUsername(admin.getUsername());
        adminDTO.setImageAdmin(admin.getImageAdmin());


        return adminDTO;
    }



    private void updateEntity(Lokasi existingLokasi, LokasiDTO lokasiDTO) {
        existingLokasi.setNamaLokasi(lokasiDTO.getNamaLokasi());
        existingLokasi.setAlamat(lokasiDTO.getAlamat());

        Optional<Organisasi> organisasiOptional = organisasiRepository.findById(lokasiDTO.getIdOrganisasi());
        organisasiOptional.ifPresent(existingLokasi::setOrganisasi);
    }

    private LokasiDTO convertToDto(Lokasi lokasi) {
        LokasiDTO lokasiDTO = new LokasiDTO();
        lokasiDTO.setNamaLokasi(lokasi.getNamaLokasi());
        lokasiDTO.setAlamat(lokasi.getAlamat());

        // Set ID Organisasi
        if (lokasi.getOrganisasi() != null) {
            lokasiDTO.setIdOrganisasi(lokasi.getOrganisasi().getId());
        }

        // Set Admin
        if (lokasi.getAdmin() != null) {
            AdminDTO adminDTO = convertAdminToDto(lokasi.getAdmin());
            lokasiDTO.setAdmin(adminDTO);

            // Set Admin ID
            lokasiDTO.setAdminId(lokasi.getAdmin().getId());
        }

        return lokasiDTO;
    }



}
