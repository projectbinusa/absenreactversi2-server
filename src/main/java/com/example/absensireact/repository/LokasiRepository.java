package com.example.absensireact.repository;

import com.example.absensireact.model.Admin;
import com.example.absensireact.model.Lokasi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LokasiRepository extends JpaRepository<Lokasi, Long> {

    @Query(value = "SELECT * FROM lokasi WHERE id_admin = :idAdmin" , nativeQuery = true)
    List<Lokasi>findbyAdmin(Long idAdmin);
    List<Lokasi> findByAdmin(Admin admin);



}
