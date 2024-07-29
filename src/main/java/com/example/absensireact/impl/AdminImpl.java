package com.example.absensireact.impl;


import com.example.absensireact.dto.PasswordDTO;
import com.example.absensireact.exception.BadRequestException;
import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.Admin;
import com.example.absensireact.model.SuperAdmin;
import com.example.absensireact.model.User;
import com.example.absensireact.repository.*;
import com.example.absensireact.service.AdminService;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AdminImpl implements AdminService {
    static final String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/absensireact.appspot.com/o/%s?alt=media";

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final SuperAdminRepository superAdminRepository;
    private final JabatanRepository jabatanRepository;
    private final ShiftRepository shiftRepository;
    private final OrganisasiRepository organisasiRepository;
    private final LokasiRepository lokasiRepository;
    private final LemburRepository lemburRepository;
    private final CutiRepository cutiRepository;
    private final AbsensiRepository absensiRepository;


    public AdminImpl(AdminRepository adminRepository, UserRepository userRepository, SuperAdminRepository superAdminRepository, JabatanRepository jabatanRepository, ShiftRepository shiftRepository, OrganisasiRepository organisasiRepository, LokasiRepository lokasiRepository, LemburRepository lemburRepository, CutiRepository cutiRepository, AbsensiRepository absensiRepository) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
        this.superAdminRepository = superAdminRepository;
        this.jabatanRepository = jabatanRepository;
        this.shiftRepository = shiftRepository;
        this.organisasiRepository = organisasiRepository;
        this.lokasiRepository = lokasiRepository;
        this.lemburRepository = lemburRepository;
        this.cutiRepository = cutiRepository;
        this.absensiRepository = absensiRepository;
    }


    @Autowired
    PasswordEncoder encoder;

    @Autowired
    AuthenticationManager authenticationManager;



    @Override
    public Admin RegisterBySuperAdmin(Long idSuperAdmin , Admin admin){
        Optional<SuperAdmin> superAdminOptional = superAdminRepository.findById(idSuperAdmin);
        if (superAdminOptional.isPresent()) {
            if (adminRepository.existsByEmail(admin.getEmail())) {
                throw new NotFoundException("Email sudah digunakan");
            }
            SuperAdmin superAdmin = superAdminOptional.get();

            admin.setSuperAdmin(superAdmin);
            admin.setUsername(admin.getUsername());
            admin.setPassword(encoder.encode(admin.getPassword()));
            admin.setRole("ADMIN");
            return adminRepository.save(admin);
        }
        throw new NotFoundException("Id super admin tidak ditemukan");
    }

    @Override
    public Admin RegisterAdmin(Admin admin) {
        if (adminRepository.existsByEmail(admin.getEmail())) {
            throw new NotFoundException("Email sudah digunakan");
        }
        admin.setUsername(admin.getUsername());
        admin.setPassword(encoder.encode(admin.getPassword()));
        admin.setRole("ADMIN");
        return adminRepository.save(admin);
    }

    @Override
    public Admin getById(Long id) {
        return adminRepository.findById(id).orElseThrow(() -> new NotFoundException("Id Admin Not Found"));
    }

    @Override
    public List<Admin>getAllBySuperAdmin (Long idSuperAdmin){
        return adminRepository.getallBySuperAdmin(idSuperAdmin);
    }
    @Override
    public List<Admin> getAll() {
        return adminRepository.findAll();
    }

    @Override
    public Admin edit(Long id, Admin admin) {

        Admin existingUser = adminRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Admin tidak ditemukan"));

        existingUser.setUsername(admin.getUsername());
        existingUser.setEmail(admin.getEmail());
        return adminRepository.save(existingUser);
    }

    @Override
    public Admin uploadImage(Long id, MultipartFile image) throws IOException {
        Optional<Admin> adminOptional = adminRepository.findById(id);
        if (adminOptional.isEmpty()) {
            throw new NotFoundException("Id admin tidak ditemukan");
        }
        String fileUrl = uploadFoto(image , "admin" + id);
        Admin admin = adminOptional.get();
        admin.setImageAdmin(fileUrl);
        return adminRepository.save(admin);
    }


    @Override
    public Admin ubahUsernamedanemail(Long id ,Admin updateadmin){
        Optional<Admin> adminOptional = adminRepository.findById(id);
        if (adminOptional.isEmpty()) {
            throw new NotFoundException("Id admin tidak ditemukan :" + id);
        }
        Admin admin = adminOptional.get();
        admin.setEmail(updateadmin.getEmail());
        admin.setUsername(updateadmin.getUsername());
        return  adminRepository.save(admin);
    }

    @Override
    public Admin putPasswordAdmin(PasswordDTO passwordDTO, Long id) {
        Admin update = adminRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Id Not Found"));

        boolean isOldPasswordCorrect = encoder.matches(passwordDTO.getOld_password(), update.getPassword());

        if (!isOldPasswordCorrect) {
            throw new NotFoundException("Password lama tidak sesuai");
        }

        if (passwordDTO.getNew_password().equals(passwordDTO.getConfirm_new_password())) {
            update.setPassword(encoder.encode(passwordDTO.getNew_password()));
            return adminRepository.save(update);
        } else {
            throw new BadRequestException("Password tidak sesuai");
        }
    }

    private String uploadFoto(MultipartFile multipartFile, String fileName) throws IOException {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String folderPath = "admin/";
        String fullPath = folderPath + timestamp + "_" + fileName;
        BlobId blobId = BlobId.of("absensireact.appspot.com", fullPath);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("./src/main/resources/FirebaseConfig.json"));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, multipartFile.getBytes());
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fullPath, StandardCharsets.UTF_8));
    }


@Override
public Map<String, Boolean> delete(Long id) {
    Map<String, Boolean> res = new HashMap<>();
    try {
        adminRepository.deleteById(id);
        res.put("Deleted", Boolean.TRUE);
    } catch (Exception e) {
        res.put("Deleted", Boolean.FALSE);
    }
    return res;
}

}
