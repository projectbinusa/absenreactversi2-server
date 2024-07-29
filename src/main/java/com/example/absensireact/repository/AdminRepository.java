package com.example.absensireact.repository;

import com.example.absensireact.model.Admin;
import com.example.absensireact.model.Organisasi;
import com.example.absensireact.model.SuperAdmin;
import com.example.absensireact.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin , Long> {


    List<Admin> findBySuperAdminId(Long superAdminId);

    List<Admin> findBySuperAdmin (SuperAdmin superAdmin);
    @Query(value = "SELECT * FROM admin WHERE email = :email", nativeQuery = true)
    Optional<Admin> findByEmail (String email);
    Boolean existsByEmail(String email);
    @Query(value = "SELECT * FROM admin WHERE username = :username", nativeQuery = true)
    boolean existsByUsername (String username);

    @Query(value = "SELECT * FROM admin WHERE username = :username", nativeQuery = true)
    Optional<Admin> findByUsername (String username);

    @Query(value = "SELECT * FROM admin WHERE id_super_admin = :idSuperAdmin" , nativeQuery = true)
    List<Admin> getallBySuperAdmin (Long idSuperAdmin);
}
