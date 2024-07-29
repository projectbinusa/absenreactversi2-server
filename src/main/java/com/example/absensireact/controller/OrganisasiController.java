package com.example.absensireact.controller;

import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.Organisasi;
import com.example.absensireact.service.OrganisasiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class OrganisasiController {


    private final OrganisasiService organisasiService;

    public OrganisasiController(OrganisasiService organisasiService) {
        this.organisasiService = organisasiService;
    }

    @GetMapping("/organisasi/all")
    public ResponseEntity<List<Organisasi>> getAllOrganisasi() {
        List<Organisasi> organisasiList =  organisasiService.getAllOrganisasi();
        return ResponseEntity.ok(organisasiList);
    }

    @GetMapping("/organisasi/all-by-admin/{idAdmin}")
    public ResponseEntity<List<Organisasi>> getAllOrganisasiByAdmin(@PathVariable Long idAdmin) {
        List<Organisasi> organisasiList =  organisasiService.getAllByAdmin(idAdmin);
        return ResponseEntity.ok(organisasiList);
    }
    @GetMapping("/organisasi/getByAdmin/{idAdmin}")
    public ResponseEntity<Organisasi> getAllByadmin(@PathVariable Long idAdmin){
        Optional<Organisasi> organisasiList = organisasiService.GetByIdAdmin(idAdmin);
        return organisasiList.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/organisasi/getById/{id}")
    public ResponseEntity<Organisasi> getOrganisasiById(@PathVariable Long id) {
        Optional<Organisasi> organisasi = organisasiService.GetOrganisasiById(id);
        return organisasi.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/organisasi/superadmin/{idSuperAdmin}")
    public ResponseEntity<List<Organisasi>> getAllOrganisasiBySuperAdmin(@PathVariable Long idSuperAdmin) {
        try {
            List<Organisasi> organisasiList = organisasiService.getAllBySuperAdmin(idSuperAdmin);
            return ResponseEntity.ok().body(organisasiList);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/organisasi/tambahByIdAdmin/{idAdmin}/data")
    public ResponseEntity<Organisasi> tambahOrganisasiData(
            @PathVariable Long idAdmin,
            @RequestBody Organisasi organisasi) {

        Organisasi savedOrganisasi = organisasiService.TambahOrganisasi(idAdmin, organisasi);
        return ResponseEntity.ok(savedOrganisasi);
    }

    @PostMapping(value = "/organisasi/tammbahImageByOrg/{idAdmin}/image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> tambahOrganisasiImage(
            @PathVariable Long idAdmin,
            @RequestParam("organisasiId") Long organisasiId,
            @RequestPart("image") MultipartFile image) throws IOException {

        organisasiService.saveOrganisasiImage(idAdmin, organisasiId, image);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/organisasi/tambahByIdSuperAdmin/{idSuperAdmin}")
    public ResponseEntity<Organisasi> tambahOrganisasi(
            @PathVariable Long idSuperAdmin,
             @RequestParam Long idAdmin,
             @RequestBody Organisasi organisasi) throws IOException {



        Organisasi savedOrganisasi = organisasiService.TambahOrganisasiBySuperAdmin( idSuperAdmin , idAdmin, organisasi);
        return ResponseEntity.ok(savedOrganisasi);
    }

    @PutMapping("/organisasi/putByIdAdmin/{idAdmin}" )
    public ResponseEntity<Organisasi> ubahDataOrganisasi(
            @PathVariable Long idAdmin,
            @RequestBody Organisasi organisasi,
            @RequestPart("image") MultipartFile image
    ) throws IOException {
        Organisasi updatedOrganisasi = organisasiService.UbahDataOrgannisasi(idAdmin, organisasi, image);
        return ResponseEntity.ok(updatedOrganisasi);
    }

    @PutMapping(value = "/organisasi/editById/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Organisasi> editOrganisasi(
            @PathVariable Long id,
            @RequestParam Long idAdmin,
            @RequestPart(value = "organisasi") Organisasi organisasi,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {
        Organisasi updatedOrganisasi = organisasiService.EditByid(id, idAdmin, organisasi, image);
        return ResponseEntity.ok(updatedOrganisasi);
    }
    @DeleteMapping("/organisasi/delete/{id}")
    public ResponseEntity<Void> deleteOrganisasi(@PathVariable Long id) throws IOException {
        organisasiService.deleteOrganisasi(id);
        return ResponseEntity.noContent().build();
    }
}