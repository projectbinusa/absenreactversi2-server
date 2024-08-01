package com.example.absensireact.controller;

import com.example.absensireact.dto.ProfileAdminDTO;
import com.example.absensireact.model.Admin;
import com.example.absensireact.impl.ProfileAdminImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/profile")
public class ProfileAdminController {

    @Autowired
    private ProfileAdminImpl profileAdminImpl;

    @PutMapping("/editDetail/{id}")
    public ResponseEntity<Admin> editProfile(@PathVariable Long id,
                                             @RequestBody ProfileAdminDTO adminDTO,
                                             @RequestParam(required = false) String oldPassword,
                                             @RequestParam(required = false) String newPassword,
                                             @RequestParam(required = false) String confirmPassword) {
        Admin admin = profileAdminImpl.editProfile(id, adminDTO, oldPassword, newPassword, confirmPassword);
        return ResponseEntity.ok(admin);
    }

    // ProfileAdminController.java
    @PostMapping("/uploadPhoto/{id}")
    public ResponseEntity<Admin> uploadProfilePhoto(@PathVariable Long id,
                                                    @RequestParam("file") MultipartFile file) {
        Admin admin = profileAdminImpl.uploadProfilePhoto(id, file);
        return ResponseEntity.ok(admin);
    }

    @PutMapping("/editPhoto/{id}")
    public ResponseEntity<Admin> updateProfilePhoto(@PathVariable Long id,
                                                    @RequestParam("file") MultipartFile file) {
        Admin admin = profileAdminImpl.updateProfilePhoto(id, file);
        return ResponseEntity.ok(admin);
    }

}
