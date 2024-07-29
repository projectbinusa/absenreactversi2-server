package com.example.absensireact.impl;

import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.Admin;
import com.example.absensireact.model.Cuti;
import com.example.absensireact.model.User;
import com.example.absensireact.repository.AdminRepository;
import com.example.absensireact.repository.CutiRepository;
 import com.example.absensireact.repository.UserRepository;
import com.example.absensireact.service.CutiService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CutiImpl implements CutiService {
    private final CutiRepository cutiRepository;

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;


    public CutiImpl(CutiRepository cutiRepository, UserRepository userRepository, AdminRepository adminRepository) {
        this.cutiRepository = cutiRepository;
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public List<Cuti> GetCutiAll(){
        return cutiRepository.findAll();
    }

    @Override
    public Optional<Cuti> GetCutiById(long id){
        return cutiRepository.findById(id);
    }

    @Override
    public List<Cuti>GetCutiByUserId(Long userId){
        return cutiRepository.findByUserId(userId);
    }
    @Override
    public Cuti updateCutiById(Long id, Cuti updatedCuti) {
        Optional<Cuti> cutiOptional = cutiRepository.findById(id);
        if (cutiOptional.isPresent()) {
            Cuti existingCuti = cutiOptional.get();
            existingCuti.setAwalCuti(updatedCuti.getAwalCuti());
            existingCuti.setAkhirCuti(updatedCuti.getAkhirCuti());
            existingCuti.setMasukKerja(updatedCuti.getMasukKerja());
            existingCuti.setKeperluan(updatedCuti.getKeperluan());
            existingCuti.setStatus(updatedCuti.getStatus());
            existingCuti.setUser(updatedCuti.getUser());

            return cutiRepository.save(existingCuti);
        } else {
            return null;
        }
    }

    @Override
    public List<Cuti> getAllByAdmin(Long adminId) {
        Optional<Admin> adminOptional = adminRepository.findById(adminId);

        if (adminOptional.isPresent()) {
            Long admin = adminOptional.get().getId();
            List<User> users = userRepository.findByadminIdAbsensi(admin);

            if (users.isEmpty()) {
                throw new NotFoundException("Tidak ada pengguna yang terkait dengan admin dengan id: " + adminId);
            }

            List<Cuti> cutiList = new ArrayList<>();
            for (User user : users) {
                List<Cuti> cutiUser = cutiRepository.findByUser(user);
                cutiList.addAll(cutiUser);
            }

            return cutiList;
        } else {
            throw new NotFoundException("Id Admin tidak ditemukan dengan id: " + adminId);
        }
    }
    @Override
    public Cuti IzinCuti(Long userId, Cuti cuti){
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("id user tidak ditemukan");
        }
        User user1 = userOptional.get();

        cuti.setAwalCuti(cuti.getAwalCuti());
        cuti.setAkhirCuti(cuti.getAkhirCuti());
        cuti.setKeperluan(cuti.getKeperluan());
        cuti.setMasukKerja(cuti.getMasukKerja());
        cuti.setStatus("diproses");
        cuti.setUser(user1);
        cuti.setOrganisasi(user1.getOrganisasi());

        return cutiRepository.save(cuti);

    }
    @Override
    public Cuti TolakCuti(Long id, Cuti cuti) {
        Cuti existingCuti = cutiRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cuti tidak ditemukan dengan id " + id));

        existingCuti.setStatus("ditolak");
        return cutiRepository.save(existingCuti);
    }
    @Override
    public Cuti TerimaCuti(Long id, Cuti cuti) {
        Cuti existingCuti = cutiRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cuti tidak ditemukan dengan id " + id));

        existingCuti.setStatus("disetujui");
        return cutiRepository.save(existingCuti);
    }

    @Override
    public boolean deleteCuti(Long id) {
        if (cutiRepository.existsById(id)) {
            cutiRepository.deleteById(id);
        } else {
            throw new NotFoundException("Cuti not found with id: " + id);
        }
        return false;
    }

}
