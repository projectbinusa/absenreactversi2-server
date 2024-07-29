package com.example.absensireact.impl;

import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.Admin;
import com.example.absensireact.model.Organisasi;
import com.example.absensireact.model.SuperAdmin;
import com.example.absensireact.repository.AdminRepository;
import com.example.absensireact.repository.OrganisasiRepository;
import com.example.absensireact.repository.SuperAdminRepository;
import com.example.absensireact.repository.UserRepository;
import com.example.absensireact.service.OrganisasiService;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrganisasiImpl implements OrganisasiService {
    static final String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/absensireact.appspot.com/o/%s?alt=media";

    private final OrganisasiRepository organisasiRepository;
    private final UserRepository userRepository;
    private final SuperAdminRepository superAdminRepository;
    private final AdminRepository adminRepository;

    public OrganisasiImpl(OrganisasiRepository organisasiRepository, UserRepository userRepository, SuperAdminRepository superAdminRepository, AdminRepository adminRepository) throws IOException {
        this.organisasiRepository = organisasiRepository;
        this.userRepository = userRepository;
        this.superAdminRepository = superAdminRepository;
        this.adminRepository = adminRepository;

    }
    @Override
    public List<Organisasi> getAllOrganisasi(){
        return organisasiRepository.findAll();
    }

    @Override
    public List<Organisasi>getAllByAdmin(Long idAdmin){
        return organisasiRepository.getOrganisasiByIdAdmin(idAdmin);
    }
    @Override
    public Optional<Organisasi>GetOrganisasiById(Long id){
        return organisasiRepository.findById(id);
    }


    @Override
    public List<Organisasi> getAllBySuperAdmin(Long idSuperAdmin) {
        Optional<SuperAdmin> superAdminOptional = superAdminRepository.findById(idSuperAdmin);
        if (superAdminOptional.isPresent()) {
            SuperAdmin superAdmin = superAdminOptional.get();

            List<Admin> adminList = adminRepository.findBySuperAdmin(superAdmin);
            List<Organisasi> organisasiList = new ArrayList<>();

            for (Admin admin : adminList) {
                List<Organisasi> organisasiListByAdmin = organisasiRepository.findByAdmin(admin);
                organisasiList.addAll(organisasiListByAdmin);
            }

            return organisasiList;
        }
        throw new NotFoundException("Id Super admin tidak ditemukan");
    }


    @Override
    public Optional<Organisasi> GetByIdAdmin(Long idAdmin){
        return  organisasiRepository.findOrganisasiByIdAdmin(idAdmin);}
    @Override
    public Optional<Organisasi> GetAllBYId(Long id){
        return organisasiRepository.findById(id);
    }


    @Override
    public Organisasi TambahOrganisasiBySuperAdmin(Long idSuperAdmin, Long idAdmin, Organisasi organisasi) throws IOException {
        Optional<SuperAdmin> superAdminOptional = superAdminRepository.findById(idSuperAdmin);

        if (superAdminOptional.isEmpty()) {
            throw new NotFoundException("id super admin tidak ditemukan : " + idSuperAdmin);
        }

        Optional<Admin> adminOptional = adminRepository.findById(idAdmin);

        if (!adminOptional.isPresent()) {
            throw new NotFoundException("Id Admin tidak ditemukan");
        }
        Admin admin = adminOptional.get();

        Optional<Organisasi> existingOrganisasi = organisasiRepository.findOrganisasiByIdAdmin(idAdmin);
        if (existingOrganisasi.isPresent()) {
            throw new IllegalStateException("Admin sudah memiliki organisasi");
        }

        organisasi.setNamaOrganisasi(organisasi.getNamaOrganisasi());
        organisasi.setAlamat(organisasi.getAlamat());
        organisasi.setKecamatan(organisasi.getKecamatan());
        organisasi.setKabupaten(organisasi.getKabupaten());
        organisasi.setProvinsi(organisasi.getProvinsi());
        organisasi.setNomerTelepon(organisasi.getNomerTelepon());
        organisasi.setEmailOrganisasi(organisasi.getEmailOrganisasi());
        organisasi.setAdmin(admin);

        return organisasiRepository.save(organisasi);

    }

    @Override
    public Organisasi TambahOrganisasi(Long idAdmin, Organisasi organisasi) {
        Optional<Admin> adminOptional = Optional.ofNullable(adminRepository.findById(idAdmin).orElse(null));

        if (!adminOptional.isPresent()) {
            throw new NotFoundException("Id Admin tidak ditemukan");
        }
        Admin admin = adminOptional.get();

        Optional<Organisasi> existingOrganisasi = organisasiRepository.findOrganisasiByIdAdmin(idAdmin);
        if (existingOrganisasi.isPresent()) {
            throw new IllegalStateException("Admin sudah memiliki organisasi");
        }

        organisasi.setAdmin(admin);
        organisasi.setNamaOrganisasi(organisasi.getNamaOrganisasi());
        organisasi.setAlamat(organisasi.getAlamat());
        organisasi.setKecamatan(organisasi.getKecamatan());
        organisasi.setKabupaten(organisasi.getKabupaten());
        organisasi.setProvinsi(organisasi.getProvinsi());
        organisasi.setNomerTelepon(organisasi.getNomerTelepon());
        organisasi.setEmailOrganisasi(organisasi.getEmailOrganisasi());
        return organisasiRepository.save(organisasi);
    }

    @Override
    public void saveOrganisasiImage(Long idAdmin, Long organisasiId, MultipartFile image) throws IOException {
        Optional<Organisasi> organisasiOptional = organisasiRepository.findById(organisasiId);
        if (!organisasiOptional.isPresent()) {
            throw new NotFoundException("Organisasi tidak ditemukan");
        }

        Organisasi organisasi = organisasiOptional.get();
        String file = uploadFoto(image, "_organisasi_" + idAdmin);
        organisasi.setFotoOrganisasi(file);
        organisasiRepository.save(organisasi);
    }

    private String uploadFoto(MultipartFile multipartFile, String fileName) throws IOException {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String folderPath = "organisasi/";
        String fullPath = folderPath + timestamp + "_" + fileName;
        BlobId blobId = BlobId.of("absensireact.appspot.com", fullPath);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("./src/main/resources/FirebaseConfig.json"));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, multipartFile.getBytes());
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fullPath, StandardCharsets.UTF_8));
    }



//    private String uploadFoto(MultipartFile multipartFile, String fileName) throws IOException {
//        String timestamp = String.valueOf(System.currentTimeMillis());
//        String folderPath = "organisasi/";
//        String fullPath = folderPath + timestamp + "_" + fileName;
//        BlobId blobId = BlobId.of("absensireact.appspot.com", fullPath);
//        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
//        storage.create(blobInfo, multipartFile.getBytes());
//        return String.format(DOWNLOAD_URL, URLEncoder.encode(fullPath, StandardCharsets.UTF_8));
//    }

    private void deleteFoto(String fileName) throws IOException {
        BlobId blobId = BlobId.of("absensireact.appspot.com", fileName);
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("./src/main/resources/FirebaseConfig.json"));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.delete(blobId);
    }

    @Override
    public Organisasi UbahDataOrgannisasi(Long idAdmin, Organisasi organisasi, MultipartFile image) throws IOException {
        Admin admin = adminRepository.findById(idAdmin).orElse(null);
        if (admin == null) {
            throw new NotFoundException("Id admin tidak ditemukan");
        }

        Optional<Organisasi> existingOrganisasiOptional = organisasiRepository.findOrganisasiByIdAdmin(idAdmin);
        if (!existingOrganisasiOptional.isPresent()) {
            throw new NotFoundException("Organisasi dengan id admin " + idAdmin + " tidak ditemukan");
        }

        Organisasi existingOrganisasi = existingOrganisasiOptional.get();

        if (existingOrganisasi.getFotoOrganisasi() != null && !existingOrganisasi.getFotoOrganisasi().isEmpty()) {
            String currentFotoUrl = existingOrganisasi.getFotoOrganisasi();
            String fileName = currentFotoUrl.substring(currentFotoUrl.indexOf("/o/") + 3, currentFotoUrl.indexOf("?alt=media"));
            deleteFoto(fileName);
        }

        if (image != null && !image.isEmpty()) {
            String imageUrl = uploadFoto(image, "_organisasi_" + idAdmin);
            organisasi.setFotoOrganisasi(imageUrl);
        } else {
            organisasi.setFotoOrganisasi(existingOrganisasi.getFotoOrganisasi());
        }

        organisasi.setNamaOrganisasi(organisasi.getNamaOrganisasi());
        organisasi.setAlamat(organisasi.getAlamat());
        organisasi.setKecamatan(organisasi.getKecamatan());
        organisasi.setKabupaten(organisasi.getKabupaten());
        organisasi.setProvinsi(organisasi.getProvinsi());
        organisasi.setNomerTelepon(organisasi.getNomerTelepon());
        organisasi.setEmailOrganisasi(organisasi.getEmailOrganisasi());
        organisasi.setAdmin(admin);

        return organisasiRepository.save(organisasi);
    }

    @Override
    public Organisasi EditByid(Long id, Long idAdmin, Organisasi organisasi, MultipartFile image) throws IOException {
        Organisasi existingOrganisasi = organisasiRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Organisasi dengan id " + id + " tidak ditemukan"));

        Optional<Admin> adminOptional = adminRepository.findById(idAdmin);
        if (adminOptional.isEmpty()) {
            throw new NotFoundException("Id admin tidak ditemukan");
        }

        if (existingOrganisasi.getFotoOrganisasi() != null && !existingOrganisasi.getFotoOrganisasi().isEmpty()) {
            String currentFotoUrl = existingOrganisasi.getFotoOrganisasi();
            String fileName = currentFotoUrl.substring(currentFotoUrl.indexOf("/o/") + 3, currentFotoUrl.indexOf("?alt=media"));
            deleteFoto(fileName);
        }

        if (image != null && !image.isEmpty()) {
            String imageUrl = uploadFoto(image, "organisasi" + id);
            organisasi.setFotoOrganisasi(imageUrl);
        } else {
            organisasi.setFotoOrganisasi(existingOrganisasi.getFotoOrganisasi());
        }

        Admin admin = adminOptional.get();
        existingOrganisasi.setNamaOrganisasi(organisasi.getNamaOrganisasi());
        existingOrganisasi.setAlamat(organisasi.getAlamat());
        existingOrganisasi.setKecamatan(organisasi.getKecamatan());
        existingOrganisasi.setKabupaten(organisasi.getKabupaten());
        existingOrganisasi.setProvinsi(organisasi.getProvinsi());
        existingOrganisasi.setNomerTelepon(organisasi.getNomerTelepon());
        existingOrganisasi.setEmailOrganisasi(organisasi.getEmailOrganisasi());
        existingOrganisasi.setAdmin(admin);

        return organisasiRepository.save(existingOrganisasi);
    }

    @Override
    public void deleteOrganisasi(Long id) throws IOException {
        Optional<Organisasi> organisasiOptional = organisasiRepository.findById(id);
        if (organisasiOptional.isPresent()) {
            Organisasi organisasi = organisasiOptional.get();
            if (organisasi.getFotoOrganisasi() != null) {
                String fotoUrl = organisasi.getFotoOrganisasi();
                String fileName = fotoUrl.substring(fotoUrl.indexOf("/o/") + 3, fotoUrl.indexOf("?alt=media"));
                deleteFoto(fileName);
                organisasiRepository.deleteById(id);
            } else {

             organisasiRepository.deleteById(id);
            }
        } else {
            throw new NotFoundException("Organisasi not found with id: " + id);
        }
    }

}
