package com.example.absensireact.impl;

import com.example.absensireact.dto.ProfileAdminDTO;
import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.Admin;
import com.example.absensireact.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ProfileAdminImpl {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder encoder;

    public Admin editProfile(Long id, ProfileAdminDTO adminDTO, String oldPassword, String newPassword, String confirmPassword) {
        Admin admin = adminRepository.findById(id).orElseThrow(() -> new NotFoundException("Admin not found"));
        admin.setUsername(adminDTO.getUsername());
        admin.setEmail(adminDTO.getEmail());

        // Password change logic
        if (newPassword != null && !newPassword.isEmpty()) {
            if (encoder.matches(oldPassword, admin.getPassword()) && newPassword.equals(confirmPassword)) {
                admin.setPassword(encoder.encode(newPassword));
            } else {
                throw new IllegalArgumentException("Invalid old password or new password mismatch.");
            }
        }
        return adminRepository.save(admin);
    }

    public Admin uploadProfilePhoto(Long id, MultipartFile file) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Admin not found"));
        try {
            // Save the file
            Path directory = Paths.get("image/admin/");
            Path targetLocation = directory.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), targetLocation);

            // Update the image path in the admin entity
            admin.setImageAdmin(targetLocation.toString());
            return adminRepository.save(admin);
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + file.getOriginalFilename(), ex);
        }
    }

    public Admin updateProfilePhoto(Long id, MultipartFile file) {
        Admin admin = adminRepository.findById(id).orElseThrow(() -> new NotFoundException("Admin not found"));
        try {
            Path targetLocation = Paths.get("foto/admin" + file.getOriginalFilename());
            Files.copy(file.getInputStream(), targetLocation, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            admin.setImageAdmin(targetLocation.toString());
            return adminRepository.save(admin);
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + file.getOriginalFilename(), ex);
        }
    }
    }

