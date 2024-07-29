package com.example.absensireact.service;

import com.example.absensireact.model.Cuti;

import java.util.List;
import java.util.Optional;

public interface CutiService {
    List<Cuti> GetCutiAll();

    Optional<Cuti> GetCutiById(long id);

    List<Cuti>GetCutiByUserId(Long userId);

    Cuti updateCutiById(Long id, Cuti updatedCuti);


    List<Cuti> getAllByAdmin(Long adminId);

    Cuti IzinCuti(Long userId, Cuti cuti);

    Cuti TolakCuti(Long id, Cuti cuti);

    Cuti TerimaCuti(Long id, Cuti cuti);

    boolean deleteCuti(Long id);
}
