package com.example.absensireact.service;

import com.example.absensireact.model.Lembur;

import java.util.List;

public interface LemburService {
    List<Lembur> getAllLembur();
    Lembur getLemburById(Long id);

    List<Lembur> getLemburByUserId(Long userId);

    Lembur IzinLembur(Long userId, Lembur lembur);

     Lembur updateLembur(Long id, Lembur lembur);

    List<Lembur> getAllByAdmin(Long adminId);

    void deleteLembur(Long id);
}
