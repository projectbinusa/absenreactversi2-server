package com.example.absensireact.impl;

import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.Admin;
import com.example.absensireact.model.Jabatan;
import com.example.absensireact.model.User;
import com.example.absensireact.repository.AdminRepository;
import com.example.absensireact.repository.JabatanRepository;
import com.example.absensireact.repository.UserRepository;
import com.example.absensireact.service.JabatanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JabatanImpl implements JabatanService {


    @Autowired
    private UserRepository userRepository;
    private final JabatanRepository jabatanRepository;
    private final AdminRepository adminRepository;


    public JabatanImpl(AdminRepository adminRepository, JabatanRepository jabatanRepository ) {
        this.adminRepository = adminRepository;
        this.jabatanRepository = jabatanRepository;
     }

    @Override
    public List<Jabatan> getAllJabatan() {
        return jabatanRepository.findAll();
    }

    @Override
    public List<Jabatan> getJabatanByAdminId(Long adminId) {
        return jabatanRepository.findByAdminId(adminId);
    }

    @Override
    public Optional<Jabatan> getJabatanById(Long idJabatan) {
        return  jabatanRepository.findById(idJabatan);
    }

    @Override
    public Jabatan addJabatan(Long adminId, Jabatan jabatan ) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new NotFoundException(  "Admin not found with id: " + adminId));

        jabatan.setAdmin(admin);
        jabatan.setNamaJabatan(jabatan.getNamaJabatan());
        return jabatanRepository.save(jabatan);
    }

    @Override
    public List<Jabatan> getJabatanBySuperAdminId(Long idSuperAdmin) {
        List<Admin> admins = adminRepository.findBySuperAdminId(idSuperAdmin);
        List<Jabatan> jabatans = new ArrayList<>();
        for (Admin admin : admins) {
            jabatans.addAll(jabatanRepository.findByAdminId(admin.getId()));
        }
        return jabatans;
    }
    @Override
    public Jabatan editJabatan(Long adminId, Jabatan jabatan) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new NotFoundException("Admin not found with id: " + adminId));

        Jabatan existingJabatan = jabatanRepository.findById(jabatan.getIdJabatan())
                .orElseThrow(() -> new NotFoundException("Jabatan not found with id: " + jabatan.getIdJabatan()));

         if (!existingJabatan.getAdmin().equals(admin)) {
            throw new IllegalArgumentException("You are not authorized to edit this Jabatan.");
        }

        jabatan.setAdmin(admin);
        return jabatanRepository.save(jabatan);
    }

    @Override
    public Jabatan editJabatanById(Long idJabatan, Jabatan updateJabatan) {
        Jabatan jabatan = jabatanRepository.findById(idJabatan).orElseThrow(() -> new NotFoundException("id jabatan tidak ditemukan" + idJabatan));
        jabatan.setNamaJabatan(updateJabatan.getNamaJabatan());
        return jabatanRepository.save(jabatan);
    }

    @Override
    public Map<String, Boolean> deleteJabatan(Long idJabatan) {
        try {
            List<User> users = userRepository.findByIdJabatan(idJabatan);
            for (User user : users) {
                user.setJabatan(null);
                userRepository.save(user);
            }

            jabatanRepository.deleteById(idJabatan);

            Map<String, Boolean> res = new HashMap<>();
            res.put("Deleted", Boolean.TRUE);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Boolean> res = new HashMap<>();
            res.put("Deleted", Boolean.FALSE);
            return res;
        }
    }
}
