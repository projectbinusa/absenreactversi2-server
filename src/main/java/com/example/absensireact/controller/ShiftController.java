package com.example.absensireact.controller;
import com.example.absensireact.model.Shift;
import com.example.absensireact.service.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/shift")
public class ShiftController {

    @Autowired
    private ShiftService shiftService;


    @GetMapping("/getbyadmin")
    public ResponseEntity<List<Shift>> getShiftsByAdmin(@RequestParam("idAdmin") Long idAdmin) {
        List<Shift> shifts = shiftService.getShiftsByAdmin(idAdmin);
        return ResponseEntity.ok(shifts);
    }
    @GetMapping("/getall")
    public ResponseEntity<List<Shift>> getAllShifts() {
        List<Shift> shifts = shiftService.getAllShift();
        return new ResponseEntity<>(shifts, HttpStatus.OK);
    }
    @GetMapping("/getall-byadmin/{idAdmin}")
    public ResponseEntity<List<Shift>> getAllShiftsByAdmin(@PathVariable Long idAdmin) {
        List<Shift> shifts = shiftService.getAllShiftByAdmin(idAdmin);
        return new ResponseEntity<>(shifts, HttpStatus.OK);
    }

    @GetMapping("/getShift-byUserId/{userId}")
    public ResponseEntity<?> getAllShiftsByUserId(@PathVariable Long userId) {
        Optional<Shift> shifts = shiftService.getByUserId(userId);
        return new ResponseEntity<>(shifts, HttpStatus.OK);
    }

    @GetMapping("/getbyId/{id}")
    public ResponseEntity<Shift> getShiftById(@PathVariable("id") Long id) {
        Optional<Shift> shift = shiftService.getshiftById(id);
        return shift.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/getBySuper/{idSuperAdmin}")
    public List<Shift> getShiftBySuperAdminId(@PathVariable Long idSuperAdmin) {
        return shiftService.getShiftBySuperAdminId(idSuperAdmin);
    }

    @PostMapping("/tambahShift/{idAdmin}")
    public ResponseEntity<Shift> postShift(@PathVariable("idAdmin") Long idAdmin,
                                           @RequestBody Shift shift) {
        Shift createdShift = shiftService.PostShift(idAdmin, shift);
        return new ResponseEntity<>(createdShift, HttpStatus.CREATED);
    }

    @PutMapping("/editbyId/{id}")
    public ResponseEntity<Shift> editShiftById(@PathVariable("id") Long id,
                                               @RequestBody Shift shift
    ) {
        Shift updatedShift = shiftService.editShiftById(id, shift);
        return new ResponseEntity<>(updatedShift, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteShift(@PathVariable("id") Long id) {
        Map<String, Boolean> response = shiftService.delete(id);
        if (response.get("Deleted")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}