package com.example.absensireact.controller;


import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.helper.CutiPDF;
import com.example.absensireact.model.Absensi;
import com.example.absensireact.model.Cuti;
import com.example.absensireact.service.CutiService;
import com.example.absensireact.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class CutiController {
    private final CutiService cutiService;

    private final UserService userService;

    private final CutiPDF cutiPDF;

    public CutiController(CutiService cutiService, UserService userService, CutiPDF cutiPDF) {
        this.cutiService = cutiService;
        this.userService = userService;
        this.cutiPDF = cutiPDF;
    }

    @GetMapping("/cuti/download-pdf/{id}")
    public void downloadPDF(@PathVariable Long id, HttpServletResponse response) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        cutiPDF.generatePDF(id, baos);

        // Set response headers
        response.setContentType("application/pdf");
        response.setHeader("Content-disposition", "attachment; filename=cuti.pdf");
        response.setContentLength(baos.size());

        // Write PDF content to response output stream
        response.getOutputStream().write(baos.toByteArray());
        response.getOutputStream().flush();
    }

    @GetMapping("/cuti/getById/{id}")
    public Optional<Cuti> GetCutiById(@PathVariable Long id ){
        Optional<Cuti> cutiOptional = cutiService.GetCutiById(id);
        if (cutiOptional.isEmpty()) {
            throw new NotFoundException("Cuti tidak ditemukan dengan id : " + id);
        }
        return cutiOptional;
    }

    @GetMapping("/cuti/admin/{adminId}")
    public ResponseEntity<List<Cuti>> getAllByAdmin(@PathVariable Long adminId) {
        try {
            List<Cuti> absensiList = cutiService.getAllByAdmin(adminId);
            return new ResponseEntity<>(absensiList, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/cuti/getByUser/{userId}")
    public List<Cuti> GetCutiByUserId(@PathVariable Long userId){
        List<Cuti> cuti =  cutiService.GetCutiByUserId(userId);
        if (cuti == null) {
            throw new NotFoundException("User id tidak ditemukan dengan id : " + userId);
        }
        return cuti;

    }

    @GetMapping("/cuti/getall")
    public ResponseEntity<List<Cuti>> getAllCuti() {
        List<Cuti> cutiList = cutiService.GetCutiAll();
        return new ResponseEntity<>(cutiList, HttpStatus.OK);
    }

    @PostMapping("/cuti/tambahCuti/{userId}")
    public ResponseEntity<Cuti> createCuti(@PathVariable Long userId, @RequestBody Cuti cuti) {
        Cuti createdCuti = cutiService.IzinCuti(userId, cuti);
        return new ResponseEntity<>(createdCuti, HttpStatus.CREATED);
    }

    @PutMapping("/cuti/tolak-cuti/{id}")
    public ResponseEntity<Cuti> tolakCuti(@PathVariable Long id, @RequestBody Cuti cuti) {
        Cuti updatedCuti = cutiService.TolakCuti(id, cuti);
        if (updatedCuti == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedCuti, HttpStatus.OK);
    }
    @PutMapping("/cuti/terima-cuti/{id}")
    public ResponseEntity<Cuti> terimaCuti(@PathVariable Long id, @RequestBody Cuti cuti) {
        Cuti updatedCuti = cutiService.TerimaCuti(id, cuti);
        if (updatedCuti == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedCuti, HttpStatus.OK);
    }
    @PutMapping("/cuti/update-cuti-user/{id}")
    public ResponseEntity<Cuti> updateCutiById(@PathVariable Long id, @RequestBody Cuti updatedCuti) {
        Cuti updated = cutiService.updateCutiById(id, updatedCuti);
        if (updated != null) {
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/cuti/delete/{id}")
    public ResponseEntity<Void> deleteCuti(@PathVariable Long id) {
        boolean deleted = cutiService.deleteCuti(id);
        if (!deleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
