package com.example.absensireact.impl;

import com.example.absensireact.dto.PasswordDTO;
import com.example.absensireact.exception.BadRequestException;
import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.SuperAdmin;
import com.example.absensireact.repository.SuperAdminRepository;
import com.example.absensireact.service.SuperAdminService;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
public class SuperAdminImpl implements SuperAdminService {

    static final String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/absensireact.appspot.com/o/%s?alt=media";

    private final SuperAdminRepository superAdminRepository;

    @Autowired
    PasswordEncoder encoder;


    public SuperAdminImpl(SuperAdminRepository superAdminRepository) {
        this.superAdminRepository = superAdminRepository;
    }

    @Override
    public SuperAdmin getAllSuperAdmin(){
        return (SuperAdmin) superAdminRepository.findAll();
    }

    @Override
    public Optional<SuperAdmin> getSuperadminbyId(Long id){
       return superAdminRepository.findById(id);
    }

    @Override
    public SuperAdmin RegisterSuperAdmin(SuperAdmin superAdmin) {
        if (superAdminRepository.existsByEmail(superAdmin.getEmail())) {
            throw new NotFoundException("Email sudah digunakan  ");
        }
        superAdmin.setUsername(superAdmin.getUsername());
        superAdmin.setPassword(encoder.encode(superAdmin.getPassword()));
        superAdmin.setRole("SUPERADMIN");
        return superAdminRepository.save(superAdmin);
    }

    @Override
    public SuperAdmin tambahSuperAdmin(Long id, SuperAdmin superAdmin, MultipartFile image) throws IOException {
       Optional<SuperAdmin> ExistingSuperAdmin = Optional.ofNullable(superAdminRepository.findById(id).orElse(null));
        if (ExistingSuperAdmin == null) {
            superAdmin.setEmail(superAdmin.getEmail());
            superAdmin.setUsername(superAdmin.getUsername());
            superAdmin.setImageSuperAdmin(uploadFoto(image , "SuperAdmin"));

            return superAdminRepository.save(superAdmin);

        }

        throw new NotFoundException("Id Super Admin tidak ditemukan");
    }

    @Override
    public SuperAdmin EditSuperAdmin(Long id, MultipartFile image, SuperAdmin superAdmin) throws IOException {
        Optional<SuperAdmin> ifSuperAdmin = Optional.ofNullable(superAdminRepository.findById(id).orElse(null));
        if (ifSuperAdmin == null) {
            superAdmin.setEmail(superAdmin.getEmail());
            superAdmin.setUsername(superAdmin.getUsername());
            superAdmin.setImageSuperAdmin(uploadFoto(image , "SuperAdmin"));

            return superAdminRepository.save(superAdmin);
        }
       throw new NotFoundException("Id Super Admin tidak ditemukan");
    }

    @Override
    public SuperAdmin putPasswordSuperAdmin(PasswordDTO passwordDTO, Long id) {
        SuperAdmin update = superAdminRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Id Not Found"));

        boolean isOldPasswordCorrect = encoder.matches(passwordDTO.getOld_password(), update.getPassword());

        if (!isOldPasswordCorrect) {
            throw new NotFoundException("Password lama tidak sesuai");
        }

        if (passwordDTO.getNew_password().equals(passwordDTO.getConfirm_new_password())) {
            update.setPassword(encoder.encode(passwordDTO.getNew_password()));
            return superAdminRepository.save(update);
        } else {
            throw new BadRequestException("Password tidak sesuai");
        }
    }


    @Override
    public void deleteSuperAdmin(Long id) throws IOException {
        Optional<SuperAdmin> superAdminOptional = superAdminRepository.findById(id);
        if (superAdminOptional.isPresent()) {
            SuperAdmin superAdmin = superAdminOptional.get();
            if (superAdmin.getImageSuperAdmin().isEmpty()) {
            superAdminRepository.deleteById(id);

            }
            String fotoUrl = superAdmin.getImageSuperAdmin();
            String fileName = fotoUrl.substring(fotoUrl.indexOf("/o/") + 3, fotoUrl.indexOf("?alt=media"));
            deleteFoto(fileName);
            superAdminRepository.deleteById(id);
        } else {
            throw new NotFoundException("Organisasi not found with id: " + id);
        }
    }


    @Override
    public SuperAdmin ubahUsernamedanemail(Long id, SuperAdmin updateadmin){
        Optional<SuperAdmin> superoptional = superAdminRepository.findById(id);
        if (superoptional.isEmpty()) {
            throw new NotFoundException("Id super admin tidak ditemukan :" + id);
        }
        SuperAdmin superAdmin = superoptional.get();

        superAdmin.setEmail(updateadmin.getEmail());
        superAdmin.setUsername(updateadmin.getUsername());
        return  superAdminRepository.save(superAdmin);
    }

    @Override
    public SuperAdmin uploadImage(Long id, MultipartFile image) throws IOException {
        Optional<SuperAdmin> superOptional = superAdminRepository.findById(id);
        if (superOptional.isEmpty()) {
            throw new NotFoundException("Id admin tidak ditemukan");
        }
        String fileUrl = uploadFoto(image , "superadmin" + id);
        SuperAdmin superAdmin = superOptional.get();
        superAdmin.setImageSuperAdmin(fileUrl);
        return superAdminRepository.save(superAdmin);
    }


    private String uploadFoto(MultipartFile multipartFile, String fileName) throws IOException {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String folderPath = "superAdmin/";
        String fullPath = folderPath + timestamp + "_" + fileName;
        BlobId blobId = BlobId.of("absensireact.appspot.com", fullPath);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("./src/main/resources/FirebaseConfig.json"));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, multipartFile.getBytes());
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fullPath, StandardCharsets.UTF_8));
    }





    private void deleteFoto(String fileName) throws IOException {
        BlobId blobId = BlobId.of("absensireact.appspot.com", fileName);
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("./src/main/resources/FirebaseConfig.json"));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.delete(blobId);
    }

}
