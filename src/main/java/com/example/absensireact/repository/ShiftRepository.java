package com.example.absensireact.repository;

import com.example.absensireact.model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShiftRepository extends JpaRepository<Shift , Long> {


    List<Shift> findByAdminId(Long idAdmin);

    @Query(value = "SELECT * FROM shift WHERE id_admin = :idAdmin" , nativeQuery = true)
    Optional<Shift> findByIdAdmin (Long idAdmin);

    @Query(value = "SELECT * FROM shift WHERE id_admin = :idAdmin" , nativeQuery = true)
    List<Shift> getByIdAdmin (Long idAdmin);
}
