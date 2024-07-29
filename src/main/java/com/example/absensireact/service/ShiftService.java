package com.example.absensireact.service;

import com.example.absensireact.model.Shift;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ShiftService {
    List<Shift> getAllShift();

    List<Shift> getAllShiftByAdmin(Long idAdmin);

    Optional<Shift> getshiftById(Long id);

    Optional<Shift>getbyAdmin(Long idAdmin);


    List<Shift> getShiftsByAdmin(Long idAdmin);

    List<Shift> getShiftBySuperAdminId(Long idSuperAdmin);

    Optional<Shift> getByUserId(Long userId);

    Shift PostShift(Long idAdmin, Shift shift);





    Shift editShiftById(Long id, Shift updatedShift);

    Map<String, Boolean> delete(Long id);
}
