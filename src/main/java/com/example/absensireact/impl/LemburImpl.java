package com.example.absensireact.impl;

import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.Admin;
import com.example.absensireact.model.Lembur;
import com.example.absensireact.model.User;
import com.example.absensireact.repository.AdminRepository;
import com.example.absensireact.repository.LemburRepository;
import com.example.absensireact.repository.UserRepository;
import com.example.absensireact.service.LemburService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LemburImpl implements LemburService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private LemburRepository lemburRepository;

    @Override
    public List<Lembur> getAllLembur() {
        return lemburRepository.findAll();
    }


    @Override
    public Lembur getLemburById(Long id) {
        Optional<Lembur> lemburOptional = lemburRepository.findById(id);
        return lemburOptional.orElse(null);
    }
    @Override
    public List<Lembur> getLemburByUserId(Long userId) {
        return lemburRepository.findByuserId(userId);

    }

    @Override
    public Lembur IzinLembur(Long userId, Lembur lembur){
        User userLembur = userRepository.findById(userId).orElse(null);
        if (userLembur == null) {
            throw new NotFoundException("User id tidak ditemukan");
        }
        lembur.setTanggalLembur(lembur.getTanggalLembur());
        lembur.setJamMulai(lembur.getJamMulai());
        lembur.setJamSelesai(lembur.getJamSelesai());
        lembur.setKeteranganLembur(lembur.getKeteranganLembur());
        lembur.setUser(userLembur);
        lembur.setNama(lembur.getNama());
        return lemburRepository.save(lembur);
    }

    @Override
    public Lembur updateLembur(Long id, Lembur updatedLembur) {
        Optional<Lembur> lemburOptional = lemburRepository.findById(id);
        if (lemburOptional.isPresent()) {
            Lembur lembur = lemburOptional.get();
            lembur.setTanggalLembur(updatedLembur.getTanggalLembur());
            lembur.setJamMulai(updatedLembur.getJamMulai());
            lembur.setJamSelesai(updatedLembur.getJamSelesai());
            lembur.setKeteranganLembur(updatedLembur.getKeteranganLembur());
            lembur.setUser(updatedLembur.getUser());
            lembur.setNama(lembur.getNama());
            return lemburRepository.save(lembur);
        }
        return null;
    }

    @Override
    public List<Lembur> getAllByAdmin(Long adminId) {
        Optional<Admin> adminOptional = adminRepository.findById(adminId);

        if (adminOptional.isPresent()) {
            Long admin = adminOptional.get().getId();
            List<User> users = userRepository.findByadminIdAbsensi(admin);

            if (users.isEmpty()) {
                throw new NotFoundException("Tidak ada pengguna yang terkait dengan admin dengan id: " + adminId);
            }

            List<Lembur> lemburList = new ArrayList<>();
            for (User user : users) {
                List<Lembur> lemburUser = lemburRepository.findByUser(user);
                lemburList.addAll(lemburUser);
            }

            return lemburList;
        } else {
            throw new NotFoundException("Id Admin tidak ditemukan dengan id: " + adminId);
        }
    }
    @Override
    public void deleteLembur(Long id) {
        lemburRepository.deleteById(id);
    }
}
