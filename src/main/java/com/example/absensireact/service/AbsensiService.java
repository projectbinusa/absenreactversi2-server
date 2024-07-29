package com.example.absensireact.service;

import com.example.absensireact.model.Absensi;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface  AbsensiService {


    List<Absensi> getAllByAdmin(Long adminId);


//    List<Absensi> getByAdminAndDate(Long adminId, String date);
//
//    List<Absensi> getByAdminAndDate(Long adminId, int year, int month, int day);

    List<Absensi> getAllAbsensi();

    List<Absensi> getAbsensiByTanggal(Date tanggalAbsen);

    List<Absensi> getAbsensiByBulan(Date tanggalAbsen);


    List<Absensi> getAbsensiByBulanSimpel(int month);

    Map<String, List<Absensi>> getAbsensiByMingguan(Date startDate, Date endDate);


    Absensi PostAbsensi(Long userId, MultipartFile image, String lokasiMasuk, String keteranganTerlambat) throws IOException, ParseException;


    Absensi Pulang(Long userId, MultipartFile image, String lokasiPulang, String keteranganPulangAwal) throws IOException, ParseException;

    boolean checkUserAlreadyAbsenToday(Long userId);

    Absensi izin(Long userId, String keteranganIzin)  ;

    Absensi izinTengahHari(Long userId, String keteranganPulangAwal)  ;

    List<Absensi>getByStatusAbsen(Long userId, String statusAbsen);

    Optional<Absensi> getAbsensiById(Long id);

    Absensi updateAbsensi(Long id, Absensi absensi);


    void deleteAbsensi(Long id) throws IOException;

    List<Absensi> getAbsensiByUserId(Long userId);







}
