package com.example.absensireact.repository;


import com.example.absensireact.model.Admin;
import com.example.absensireact.model.Jabatan;
import com.example.absensireact.model.Shift;
import com.example.absensireact.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User , Long> {

    @Query(value = "SELECT * FROM user WHERE id_jabatan = :idJabatan" , nativeQuery = true)
    List<User> findByIdJabatan(Long idJabatan);

    @Query(value = "SELECT * FROM user WHERE id_jabatan = :idJabatan" , nativeQuery = true)
    Optional<User> findByJabatanId(Long idJabatan);

    @Query(value = "SELECT * FROM user WHERE id_admin = :adminId" , nativeQuery = true)
    List<User> findByadminIdAbsensi (Long adminId);

    @Query(value = "SELECT * FROM user WHERE id_shift = :idShift" , nativeQuery = true)
    List<User> findByIdShift(Long idShift);


    @Query(value = "SELECT * FROM user WHERE email = :email", nativeQuery = true)
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    @Query(value = "SELECT * FROM user WHERE id_admin = :idAdmin", nativeQuery = true)
    List<User> findByIdAdmin (Long idAdmin);
    @Query(value = "SELECT * FROM user WHERE username = :username", nativeQuery = true)
    Optional<User> findByUsername (String username);




}
