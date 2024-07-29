package com.example.absensireact.repository;

import com.example.absensireact.model.Absensi;
import com.example.absensireact.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AbsensiRepository extends JpaRepository<Absensi , Long> {

    Optional<Absensi> findByUserAndTanggalAbsen(User user, Date tanggalAbsen);

    @Query("SELECT a FROM Absensi a WHERE a.tanggalAbsen BETWEEN :tanggalAwal AND :tanggalAkhir")
    List<Absensi> findByMingguan(@Param("tanggalAwal") Date tanggalAwal, @Param("tanggalAkhir") Date tanggalAkhir);

    List<Absensi> findByUser(User user);
    @Query("SELECT a FROM Absensi a WHERE YEAR(a.tanggalAbsen) = :year AND MONTH(a.tanggalAbsen) = :month AND DAY(a.tanggalAbsen) = :day AND a.user = :user")
    List<Absensi> findByUserAndDate(@Param("user") User user, @Param("year") int year, @Param("month") int month, @Param("day") int day);

    @Query("SELECT a FROM Absensi a WHERE FUNCTION('DAY', a.tanggalAbsen) = :day AND FUNCTION('MONTH', a.tanggalAbsen) = :month AND FUNCTION('YEAR', a.tanggalAbsen) = :year")
    List<Absensi> findByTanggalAbsen(@Param("day") int day, @Param("month") int month, @Param("year") int year);

    @Query("SELECT a FROM Absensi a WHERE FUNCTION('MONTH', a.tanggalAbsen) = :month AND FUNCTION('YEAR', a.tanggalAbsen) = :year")
    List<Absensi> findByMonthAndYear(@Param("month") int month, @Param("year") int year);

    @Query("SELECT a FROM Absensi a WHERE FUNCTION('MONTH', a.tanggalAbsen) = :month")
    List<Absensi> findByMonth(@Param("month") int month);
    @Query(value = "SELECT * FROM absensi WHERE user_id = :userId" , nativeQuery = true)
    Optional<Absensi>findByUserId (Long userId);
    @Query(value = "SELECT * FROM absensi WHERE user_id = :userId" , nativeQuery = true)
    List<Absensi>findabsensiByUserId (Long userId);

    @Query(value = "SELECT * FROM absensi WHERE user_id = :userId AND tanggal_absen = :tanggalAbsen", nativeQuery = true)
    Optional<Absensi> findByUserIdAndTanggalAbsen(Long userId, Date tanggalAbsen);

    @Query(value = "SELECT * FROM absensi WHERE user_id = :userId AND status_absen = :statusAbsen " , nativeQuery = true)
    List<Absensi> getByStatusAbsen (Long userId  , String statusAbsen);
}
