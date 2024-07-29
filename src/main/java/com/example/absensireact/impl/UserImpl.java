package com.example.absensireact.impl;

import com.example.absensireact.config.AppConfig;
import com.example.absensireact.dto.PasswordDTO;
import com.example.absensireact.exception.BadRequestException;
import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.*;
import com.example.absensireact.repository.*;
import com.example.absensireact.service.UserService;
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
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class UserImpl implements UserService {

    static final String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/absensireact.appspot.com/o/%s?alt=media";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private JabatanRepository jabatanRepository;

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private OrganisasiRepository organisasiRepository;

    @Autowired
    private AppConfig appConfig;



    @Autowired
    PasswordEncoder encoder;


    @Autowired
    AuthenticationManager authenticationManager;

    @Override
    public User Register(User user, Long idOrganisasi, Long idShift) {
        if (adminRepository.existsByEmail(user.getEmail())) {
            throw new BadRequestException("Email sudah digunakan oleh admin");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BadRequestException("Email sudah digunakan oleh user");
        }

        Organisasi organisasi = organisasiRepository.findById(idOrganisasi)
                .orElseThrow(() -> new NotFoundException("Organisasi tidak ditemukan"));

        Shift shift = shiftRepository.findById(idShift)
                .orElseThrow(() -> new NotFoundException("id shift tidak ditemnukan : " + idShift));

        Long adminId = organisasi.getAdmin().getId();

        Optional<Admin> adminOptional = adminRepository.findById(adminId);

        if (adminOptional.isEmpty()) {
            throw new NotFoundException("id Admin tidak ditemukan : " + adminId);
        }

        Admin admin = adminOptional.get();

        Date date = new Date();

        SimpleDateFormat indonesianDateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("id", "ID"));
        String tanggalKerja = indonesianDateFormat.format(date);

        user.setShift(shift);
        user.setStartKerja(tanggalKerja);
        user.setStatusKerja("aktif");
        user.setJabatan(null);
        user.setAdmin(admin);
        user.setOrganisasi(organisasi);
        user.setRole("USER");
        user.setUsername(user.getUsername());
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllByJabatan(Long idJabatan) {
        Optional<Jabatan> jabatanOptional = jabatanRepository.findById(idJabatan);
        if (jabatanOptional.isEmpty()) {
            throw new NotFoundException("id Jabatan tidak ditemukan");
        }

        List<User> users = userRepository.findByIdJabatan(idJabatan);
        if (users.isEmpty()) {
            return new ArrayList<>();
        }

        return users;
    }

    @Override
    public List<User> getAllByAdmin(Long idAdmin) {
        Admin admin = adminRepository.findById(idAdmin)
                .orElseThrow(() -> new NotFoundException("id Admin tidak ditemukan: " + idAdmin));
        List<User> userList = userRepository.findByIdAdmin(idAdmin);
        return userList;
    }
    @Override
    public List<User> getAllByShift(Long idShift) {
        Optional<Shift> shiftOptional = shiftRepository.findById(idShift);
        if (shiftOptional.isEmpty()) {
            throw new NotFoundException("id Jabatan tidak ditemukan");
        }

        List<User> users = userRepository.findByIdShift(idShift);
        if (users.isEmpty()) {
            return new ArrayList<>();
        }

        return users;
    }


    @Override
    public User editUsernameJabatanShift(Long id, Long idJabatan, Long idShift, User updatedUser) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("id user tidak ditemukan");
        }
        User user = userOptional.get();
        user.setJabatan(jabatanRepository.findById(idJabatan)
                .orElseThrow(() -> new NotFoundException("id jabatan tidak ditemukan")));
        user.setShift(shiftRepository.findById(idShift)
                .orElseThrow(() -> new NotFoundException("id shift tidak ditemukan")));
        if (updatedUser.getUsername() != null) {
            user.setUsername(updatedUser.getUsername());
        }


        return userRepository.save(user);
    }



    @Override
    public User putPassword(PasswordDTO passwordDTO, Long id) {
        User update = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Id Not Found"));

        boolean isOldPasswordCorrect = encoder.matches(passwordDTO.getOld_password(), update.getPassword());

        if (!isOldPasswordCorrect) {
            throw new NotFoundException("Password lama tidak sesuai");
        }

        if (passwordDTO.getNew_password().equals(passwordDTO.getConfirm_new_password())) {
            update.setPassword(encoder.encode(passwordDTO.getNew_password()));
            return userRepository.save(update);
        } else {
            throw new BadRequestException("Password tidak sesuai");
        }
    }

    @Override
    public User ubahUsernamedanemail(Long id, User updateUser){
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("Id User tidak ditemukan :" + id);
        }
        User user = userOptional.get();
        user.setEmail(updateUser.getEmail());
        user.setUsername(updateUser.getUsername());


        return userRepository.save(user);
    }

    @Override
    public User EditUserBySuper (Long id , Long idJabatan , Long idShift , User updateUser ){
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("id user tidak ditenukan : " + id);
        }
        User user = userOptional.get();
        user.setUsername(updateUser.getUsername());
        user.setJabatan(jabatanRepository.findById(idJabatan)
                .orElseThrow(() -> new NotFoundException("id Jabatan tidak ditemukan :" + idJabatan)));
        user.setShift(shiftRepository.findById(idShift)
                .orElseThrow(() -> new NotFoundException("id Shift tidak ditemukan : " + idShift)));
        return userRepository.save(user);
    }

    @Override
    public User Tambahkaryawan(User user, Long idAdmin, Long idOrganisasi, Long idJabatan, Long idShift) {
        Optional<Admin> adminOptional = adminRepository.findById(idAdmin);
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            user.setPassword(encoder.encode(user.getPassword()));
            user.setRole("USER");

            user.setEmail(user.getEmail());
            user.setUsername(user.getUsername());
            user.setOrganisasi(organisasiRepository.findById(idOrganisasi)
                    .orElseThrow(() -> new NotFoundException("Organisasi tidak ditemukan")));
            user.setJabatan(jabatanRepository.findById(idJabatan)
                    .orElseThrow(() -> new NotFoundException("Jabatan tidak ditemukan")));
            user.setShift(shiftRepository.findById(idShift)
                    .orElseThrow(() -> new NotFoundException("Shift tidak ditemukan")));
            user.setStartKerja(new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("id", "ID")).format(new Date()));
            user.setStatusKerja("aktif");
            user.setAdmin(admin);

            return userRepository.save(user);
        } else {
            throw new NotFoundException("Id Admin tidak ditemukan");
        }
}



    @Override
    public List<User> GetAllKaryawanByIdAdmin(Long idAdmin){
        return userRepository.findByIdAdmin(idAdmin);
    }


    @Override
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Id Not Found"));
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public  User fotoUser(Long id, MultipartFile image) throws  IOException{
        User exisUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User tidak ditemukan"));
        String file = uploadFoto(image , "user");
        exisUser.setFotoUser(file);
        return userRepository.save(exisUser);
    }
    @Override
    public User edit(Long id, User user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User tidak ditemukan"));

        existingUser.setUsername(user.getUsername());
        existingUser.setOrganisasi(user.getOrganisasi());
        existingUser.setEmail(user.getEmail());
        return userRepository.save(existingUser);


    }

        private String uploadFoto(MultipartFile multipartFile, String fileName) throws IOException {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String folderPath = "user/";
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



    @Override
   public void delete(Long id) throws IOException {
    Optional<User> userOptional = userRepository.findById(id);
    if (userOptional.isPresent()) {
        User user = userOptional.get();
        String fotoUrl = user.getFotoUser();
        String fileName = fotoUrl.substring(fotoUrl.indexOf("/o/") + 3, fotoUrl.indexOf("?alt=media"));
        deleteFoto(fileName);
    } else {
        throw new NotFoundException("User not found with id: " + id);
    }
}

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private Date truncateTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
