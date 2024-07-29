package com.example.absensireact.controller;

import com.example.absensireact.dto.AdminDTO;
import com.example.absensireact.dto.LokasiDTO;
import com.example.absensireact.dto.OrganisasiDTO;
import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.Lokasi;
import com.example.absensireact.service.LokasiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lokasi")
public class LokasiController {

    @Autowired
    private LokasiService lokasiService;

    @PostMapping("/add/superadmin/{idSuperAdmin}")
    public ResponseEntity<Lokasi> tambahLokasiBySuperAdmin(
            @PathVariable("idSuperAdmin") Long idSuperAdmin,
            @RequestParam("idOrganisasi") Long idOrganisasi,
            @RequestBody Lokasi lokasi) {

        Lokasi savedLokasi = lokasiService.tambahLokasiBySuperAdmin(idSuperAdmin, lokasi, idOrganisasi);
        return ResponseEntity.ok(savedLokasi);
    }
    @PostMapping("/tambah/{idAdmin}")
    public ResponseEntity<Lokasi> tambahLokasi(@PathVariable("idAdmin") Long idAdmin, @RequestBody Lokasi lokasi , @RequestParam Long idOrganisasi) {
        Lokasi lokasiBaru = lokasiService.tambahLokasi(idAdmin, lokasi , idOrganisasi);
        return ResponseEntity.ok(lokasiBaru);
    }

    @GetMapping("/getall")
    public ResponseEntity<List<LokasiDTO>> getAllLokasi() {
        return ResponseEntity.ok(lokasiService.getAllLokasi());
    }

    @GetMapping("/GetById/{id}")
    public ResponseEntity<LokasiDTO> getLokasiById(@PathVariable Long id) {
        LokasiDTO lokasiDTO = lokasiService.getLokasiById(id);
        return ResponseEntity.ok(lokasiDTO);
    }

    @PutMapping("/editByIdLokasi/{idLokasi}")
    public ResponseEntity<Lokasi> updateLokasiByIdLokasi(@PathVariable Long idLokasi, @RequestBody Lokasi lokasiDetails) {
        try {
            Lokasi updatedLokasi = lokasiService.updateLokasiByIdlokasi(idLokasi, lokasiDetails);
            return ResponseEntity.ok(updatedLokasi);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getByIdLokasi/{idLokasi}")
    public ResponseEntity<Lokasi> getByIdLokasi(@PathVariable Long idLokasi) {
        Optional<Lokasi> lokasi = lokasiService.getByIdLokasi(idLokasi);
        if (lokasi.isPresent()) {
            return ResponseEntity.ok(lokasi.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
     @GetMapping("/get-admin/{idAdmin}")
    public ResponseEntity<List<Lokasi>>getLokasiByAdmin (@PathVariable Long idAdmin){
        List<Lokasi> lokasi = lokasiService.getAllByAdmin(idAdmin);
        return ResponseEntity.ok(lokasi);

    }

    @GetMapping("/GetOrganisasiById/{id}")
    public ResponseEntity<OrganisasiDTO> getOrganisasiById(@PathVariable Long id) {
        OrganisasiDTO organisasiDTO = lokasiService.getOrganisasiById(id);
        if (organisasiDTO != null) {
            return ResponseEntity.ok(organisasiDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/GetAdminById/{id}")
    public ResponseEntity<AdminDTO> getAdminById(@PathVariable Long id) {
        AdminDTO adminDTO = lokasiService.getAdminById(id);
        if (adminDTO != null) {
            return ResponseEntity.ok(adminDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/superadmin/{idSuperAdmin}")
    public List<Lokasi> getLokasiBySuperAdmin(@PathVariable Long idSuperAdmin) {
        return lokasiService.getAllBySuperAdmin(idSuperAdmin);
    }

      @PutMapping("/Update/{id}")
    public ResponseEntity<LokasiDTO> updateLokasi(@PathVariable Long id, @RequestBody LokasiDTO lokasiDTO) {
        LokasiDTO updatedLokasi = lokasiService.updateLokasi(id, lokasiDTO);
        return ResponseEntity.ok(updatedLokasi);
    }

    @DeleteMapping("/delete/{idLokasi}")
    public void deleteLembur(@PathVariable Long idLokasi) {
        lokasiService.deleteLokasi(idLokasi);
    }
}
