package com.example.absensireact.controller;

import com.example.absensireact.model.Jabatan;
import com.example.absensireact.service.JabatanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class JabatanController {

    private final JabatanService jabatanService;

    public JabatanController(JabatanService jabatanService) {
        this.jabatanService = jabatanService;
    }

    @GetMapping("/jabatan/all")
    public ResponseEntity<List<Jabatan>> getAllJabatan() {
        return ResponseEntity.ok(jabatanService.getAllJabatan());
    }

    @GetMapping("/jabatan/getbyid/{idJabatan}")
    public ResponseEntity<Jabatan> getJabatanById(@PathVariable("idJabatan") Long idJabatan) {
        Optional<Jabatan> jabatan = jabatanService.getJabatanById(idJabatan);
        return jabatan.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/jabatan/getByAdmin/{adminId}")
    public ResponseEntity<List<Jabatan>> getJabatanByAdminId(@PathVariable Long adminId) {
        return ResponseEntity.ok(jabatanService.getJabatanByAdminId(adminId));
    }

    @PostMapping("/jabatan/add/{adminId}")
    public ResponseEntity<Jabatan> addJabatan(@PathVariable Long adminId, @RequestBody Jabatan jabatan) {
        Jabatan newJabatan = jabatanService.addJabatan(adminId, jabatan);
        return ResponseEntity.ok(newJabatan);
    }

    @GetMapping("/jabatan/getBySuper/{idSuperAdmin}")
    public List<Jabatan> getJabatanBySuperAdminId(@PathVariable Long idSuperAdmin) {
        return jabatanService.getJabatanBySuperAdminId(idSuperAdmin);
    }
    @PutMapping("/jabatan/edit/{adminId}")
    public ResponseEntity<Jabatan> editJabatan(@PathVariable Long adminId, @RequestBody Jabatan jabatan) {
        Jabatan updatedJabatan = jabatanService.editJabatan(adminId, jabatan);
        return ResponseEntity.ok(updatedJabatan);
    }
    @PutMapping("/jabatan/editById/{idJabatan}")
    public ResponseEntity<Jabatan> editJabatanById(@PathVariable("idJabatan") Long idJabatan, @RequestBody Jabatan jabatan) {
        Jabatan updatedJabatan = jabatanService.editJabatanById(idJabatan, jabatan);
        return ResponseEntity.ok(updatedJabatan);
    }

    @DeleteMapping("/jabatan/delete/{idJabatan}")
    public ResponseEntity<Map<String, Boolean>> deleteJabatan(@PathVariable("idJabatan") Long idJabatan) {
        Map<String, Boolean> response = jabatanService.deleteJabatan(idJabatan);
        if (response.get("Deleted")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
