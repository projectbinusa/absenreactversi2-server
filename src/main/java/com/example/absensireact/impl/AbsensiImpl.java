package com.example.absensireact.impl;

import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.Absensi;
import com.example.absensireact.model.Admin;
import com.example.absensireact.model.Shift;
import com.example.absensireact.model.User;
import com.example.absensireact.repository.AbsensiRepository;
import com.example.absensireact.repository.AdminRepository;
import com.example.absensireact.repository.ShiftRepository;
import com.example.absensireact.repository.UserRepository;
import com.example.absensireact.service.AbsensiService;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;


@Service
public class AbsensiImpl implements AbsensiService {
    static final String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/absensireact.appspot.com/o/%s?alt=media";

    private static final Logger logger = Logger.getLogger(AbsensiService.class.getName());

    private final AbsensiRepository absensiRepository;

    private final UserRepository userRepository;

    private final AdminRepository adminRepository;
    private final ShiftRepository shiftRepository;



    public AbsensiImpl(AbsensiRepository absensiRepository, UserRepository userRepository, AdminRepository adminRepository, ShiftRepository shiftRepository) throws IOException {
        this.absensiRepository = absensiRepository;
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.shiftRepository = shiftRepository;
    }


    @Override
    public List<Absensi> getAllByAdmin(Long adminId) {
        Optional<Admin> adminOptional = adminRepository.findById(adminId);

        if (adminOptional.isPresent()) {
            Long admin = adminOptional.get().getId();
            List<User> users = userRepository.findByadminIdAbsensi(admin);

            if (users.isEmpty()) {
                throw new NotFoundException("Tidak ada pengguna yang terkait dengan admin dengan id: " + adminId);
            }

            List<Absensi> absensiList = new ArrayList<>();
            for (User user : users) {
                List<Absensi> userAbsensi = absensiRepository.findByUser(user);
                absensiList.addAll(userAbsensi);
            }

            return absensiList;
        } else {
            throw new NotFoundException("Id Admin tidak ditemukan dengan id: " + adminId);
        }
    }



    @Override
    public List<Absensi> getAllAbsensi(){
        return  absensiRepository.findAll();
    }

    public List<Absensi> getAbsensiByTanggal(Date tanggalAbsen) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(tanggalAbsen);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH is zero-based
        int year = calendar.get(Calendar.YEAR);

        logger.info("Fetching absensi for day: " + day + ", month: " + month + ", year: " + year);

        List<Absensi> absensiList = absensiRepository.findByTanggalAbsen(day, month, year);

        logger.info("Number of records found: " + absensiList.size());

        return absensiList;
    }

    @Override
    public List<Absensi> getAbsensiByBulan(Date tanggalAbsen) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(tanggalAbsen);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        logger.info("Fetching absensi for month: " + month + ", year: " + year);

        List<Absensi> absensiList = absensiRepository.findByMonthAndYear(month, year);

        logger.info("Number of records found: " + absensiList.size());

        return absensiList;
    }

    @Override
    public List<Absensi> getAbsensiByBulanSimpel(int month) {
        logger.info("Fetching absensi for month: " + month);

        List<Absensi> absensiList = absensiRepository.findByMonth(month);

        logger.info("Number of records found: " + absensiList.size());

        return absensiList;
}


    @Override
    public Map<String, List<Absensi>> getAbsensiByMingguan(Date tanggalAwal, Date tanggalAkhir) {
        List<Absensi> absensiList = absensiRepository.findByMingguan(tanggalAwal, tanggalAkhir);
        Map<String, List<Absensi>> weeklyAbsensiMap = new HashMap<>();

        for (Absensi absensi : absensiList) {
            String weekRange = getWeekRange(absensi.getTanggalAbsen());
            weeklyAbsensiMap.computeIfAbsent(weekRange, k -> new ArrayList<>()).add(absensi);
        }

        return weeklyAbsensiMap;
    }

    private String getWeekRange(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date startOfWeek = calendar.getTime();
        calendar.add(Calendar.DAY_OF_WEEK, 6);
        Date endOfWeek = calendar.getTime();
        return startOfWeek.toString() + " - " + endOfWeek.toString();
    }
    @Override
    public Absensi PostAbsensi(Long userId, MultipartFile image, String lokasiMasuk, String keteranganTerlambat) throws IOException, ParseException {
        Optional<Absensi> existingAbsensi = absensiRepository.findByUserIdAndTanggalAbsen(userId, truncateTime(new Date()));
        if (existingAbsensi.isPresent()) {
            throw new NotFoundException("User sudah melakukan absensi masuk pada hari yang sama sebelumnya.");
        } else {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User dengan ID " + userId + " tidak ditemukan."));
            Shift shift = shiftRepository.findById(user.getShift().getId())
                    .orElseThrow(() -> new NotFoundException("ID shift tidak ditemukan"));

            Date tanggalHariIni = truncateTime(new Date());
            Date masuk = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            String jamMasukString = formatter.format(masuk);

            SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date waktuMasukShift = timeFormatter.parse(shift.getWaktuMasuk());

            String keterangan = (masuk.before(waktuMasukShift)) ? "Lebih Awal" : "Terlambat";

            Absensi absensi = new Absensi();
            absensi.setUser(user);
            absensi.setTanggalAbsen(tanggalHariIni);
            absensi.setJamMasuk(jamMasukString);
            absensi.setJamPulang("-");
            absensi.setLokasiMasuk(lokasiMasuk);
            absensi.setLokasiPulang("-");
            absensi.setKeteranganTerlambat(keteranganTerlambat != null ? keteranganTerlambat : "-");
            absensi.setStatusAbsen(keterangan);

            absensi.setFotoMasuk(uploadFile(image, "foto_masuk_" + userId));

            return absensiRepository.save(absensi);
        }
    }


    @Override
    public Absensi Pulang(Long userId, MultipartFile image, String lokasiPulang, String keteranganPulangAwal) throws IOException, ParseException {
        Absensi absensi = absensiRepository.findByUserIdAndTanggalAbsen(userId, truncateTime(new Date()))
                .orElseThrow(() -> new NotFoundException("User belum melakukan absensi masuk hari ini."));

        if (!absensi.getJamPulang().equals("-")) {
            throw new NotFoundException("User sudah melakukan absensi pulang hari ini.");
        }

        Shift shift = shiftRepository.findById(absensi.getUser().getShift().getId())
                .orElseThrow(() -> new NotFoundException("ID shift tidak ditemukan"));

        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Date waktuPulangShift = timeFormatter.parse(shift.getWaktuPulang());

        Date pulang = new Date();

        if (pulang.before(waktuPulangShift)) {
            throw new NotFoundException("User tidak bisa melakukan absensi pulang sebelum waktu yang ditentukan dalam shift.");
        }

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String jamPulangString = formatter.format(pulang);

        absensi.setKeteranganPulangAwal(keteranganPulangAwal != null ? keteranganPulangAwal : "-");
        absensi.setJamPulang(jamPulangString);
        absensi.setLokasiPulang(lokasiPulang);
        absensi.setFotoPulang(uploadFile(image, "foto_pulang_" + userId));

        return absensiRepository.save(absensi);
    }


    @Override
    public boolean checkUserAlreadyAbsenToday(Long userId) {
        Optional<Absensi> absensi = absensiRepository.findByUserIdAndTanggalAbsen(userId, truncateTime(new Date()));
        return absensi.isPresent();
    }

    @Override
    public Absensi izin(Long userId, String keteranganIzin) {
        Optional<Absensi> existingAbsensi = absensiRepository.findByUserIdAndTanggalAbsen(userId, truncateTime(new Date()));
        if (existingAbsensi.isPresent()) {
            throw new NotFoundException("User sudah melakukan absensi masuk pada hari yang sama sebelumnya.");
        } else {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User dengan ID " + userId + " tidak ditemukan."));

            Date tanggalHariIni = truncateTime(new Date());
            Absensi absensi = new Absensi();
            absensi.setUser(user);
            absensi.setTanggalAbsen(tanggalHariIni);
            absensi.setJamMasuk("-");
            absensi.setJamPulang("-");
            absensi.setKeteranganIzin(keteranganIzin);
            absensi.setStatusAbsen("Izin");

            return absensiRepository.save(absensi);
        }
    }
    @Override
    public Absensi izinTengahHari(Long userId , String keterangaPulangAwal )   {
        Optional<Absensi> existingAbsensi = absensiRepository.findByUserIdAndTanggalAbsen(userId, truncateTime(new Date()));
        if (existingAbsensi.isPresent()) {
            Absensi absensi = existingAbsensi.get();
            Date masuk = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            String jamPulang = formatter.format(masuk);
            absensi.setJamPulang(jamPulang);
            absensi.setKeteranganPulangAwal(keterangaPulangAwal);
            absensi.setStatusAbsen("Izin Tengah Hari");
            return absensiRepository.save(absensi);
        } else {
            throw new NotFoundException("User belum melakukan absensi masuk pada hari ini.");
        }
    }


    @Override
    public List<Absensi> getByStatusAbsen(Long userId, String statusAbsen) {
        return absensiRepository.getByStatusAbsen(userId, statusAbsen);
    }
    @Override
    public Optional<Absensi> getAbsensiById(Long id) {
        return absensiRepository.findById(id);
    }

    @Override
    public Absensi updateAbsensi(Long id, Absensi absensi) {
        return absensiRepository.findById(id)
                .map(existingAbsensi -> {
                    existingAbsensi.setTanggalAbsen(absensi.getTanggalAbsen());
                    existingAbsensi.setJamMasuk(absensi.getJamMasuk());
                    existingAbsensi.setJamPulang(absensi.getJamPulang());
                    existingAbsensi.setLokasiMasuk(absensi.getLokasiMasuk());
                    existingAbsensi.setLokasiPulang(absensi.getLokasiPulang());
                    existingAbsensi.setKeteranganTerlambat(absensi.getKeteranganTerlambat());
                    existingAbsensi.setFotoMasuk(absensi.getFotoMasuk());
                    existingAbsensi.setFotoPulang(absensi.getFotoPulang());
                    existingAbsensi.setStatus(absensi.getStatus());
                    existingAbsensi.setStatusAbsen(absensi.getStatusAbsen());
                    existingAbsensi.setKeteranganIzin(absensi.getKeteranganIzin());
                    existingAbsensi.setKeteranganPulang(absensi.getKeteranganPulang());
                    existingAbsensi.setKeteranganPulangAwal(absensi.getKeteranganPulangAwal());
                    return absensiRepository.save(existingAbsensi);
                })
                .orElseThrow(() -> new NotFoundException("Absensi not found with id: " + id));
    }

    @Override
    public void deleteAbsensi(Long id) throws IOException {
        Optional<Absensi> absensiOptional = absensiRepository.findById(id);
        if (absensiOptional.isPresent()) {
            Absensi absensi = absensiOptional.get();

            String fotoMasukUrl = absensi.getFotoMasuk();
            if (fotoMasukUrl != null) {
                String fileNameMasuk = fotoMasukUrl.substring(fotoMasukUrl.indexOf("/o/") + 3, fotoMasukUrl.indexOf("?alt=media"));
                deleteFoto(fileNameMasuk);
            }

            String fotoPulangUrl = absensi.getFotoPulang();
            if (fotoPulangUrl != null && !fotoPulangUrl.isEmpty()) {
                String fileNamePulang = fotoPulangUrl.substring(fotoPulangUrl.indexOf("/o/") + 3, fotoPulangUrl.indexOf("?alt=media"));
                deleteFoto(fileNamePulang);
            }

            absensiRepository.deleteById(id);

        } else {
            throw new NotFoundException("Absensi not found with id: " + id);
        }
    }
    @Override
    public List<Absensi> getAbsensiByUserId(Long userId) {
        return absensiRepository.findabsensiByUserId(userId);
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

    private int getHourOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }
    private boolean deleteFoto(String fileName) throws IOException {
        BlobId blobId = BlobId.of("absensireact.appspot.com", fileName);
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("./src/main/resources/FirebaseConfig.json"));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.delete(blobId);
        return true;
    }

    private String uploadFile(MultipartFile multipartFile, String fileName) throws IOException {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String folderPath = "fotoAbsen/fotoMasuk/";
        String fullPath = folderPath + timestamp + "_" + fileName;
        BlobId blobId = BlobId.of("absensireact.appspot.com", fullPath);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("./src/main/resources/FirebaseConfig.json"));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, multipartFile.getBytes());
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fullPath, StandardCharsets.UTF_8));
    }

    private String uploadFilePulang(MultipartFile multipartFile, String fileName) throws IOException {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String folderPath = "fotoAbsen/fotoPulang/";
        String fullPath = folderPath + timestamp + "_" + fileName;
        BlobId blobId = BlobId.of("absensireact.appspot.com", fullPath);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("./src/main/resources/FirebaseConfig.json"));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, multipartFile.getBytes());
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fullPath, StandardCharsets.UTF_8));
    }

}
