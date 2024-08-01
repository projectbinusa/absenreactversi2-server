package com.example.absensireact.controller;

import com.example.absensireact.dto.ForGotPass;
import com.example.absensireact.dto.PasswordDTO;
import com.example.absensireact.dto.ResetPassDTO;
import com.example.absensireact.dto.VerifyCode;
import com.example.absensireact.exception.BadRequestException;
import com.example.absensireact.exception.CommonResponse;
import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.exception.ResponseHelper;
import com.example.absensireact.model.Admin;
import com.example.absensireact.model.SuperAdmin;
import com.example.absensireact.model.User;
import com.example.absensireact.service.SuperAdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class SuperAdminController {
    private final SuperAdminService superAdminService;


    public SuperAdminController(SuperAdminService superAdminService) {
        this.superAdminService = superAdminService;
    }


    @PostMapping("/superadmin/validasi-code")
    public void requestPasswordReset(@RequestBody VerifyCode verifyCode) {
        try {
            superAdminService.validasiCodeUniqResPass(verifyCode);
        } catch (NotFoundException e) {
            throw new BadRequestException("Invalid reset code or email.");
        }
    }

    @PutMapping("/superadmin/ubahPassByForgot")
    public void resetPassword(@RequestBody ResetPassDTO resetPassDTO) {
        try {
            superAdminService.ubahPassByForgot(resetPassDTO);
        } catch (NotFoundException e) {
            throw new BadRequestException("Email not found.");
        } catch (BadRequestException e) {
            throw new BadRequestException("Password does not match.");
        }
    }

    @PostMapping("/superadmin/forgot_password")
    public CommonResponse<ForGotPass> sendEmail(@RequestBody ForGotPass forGotPass) throws MessagingException {
        return ResponseHelper.ok(superAdminService.sendEmail(forGotPass));

    }
    @PutMapping("/superadmin/edit-email-username/{id}")
    public ResponseEntity<SuperAdmin> editemailusernamesuperadmin(@PathVariable Long id, @RequestBody SuperAdmin updateAdmin) {
        SuperAdmin Admin = superAdminService.ubahUsernamedanemail(id , updateAdmin);
        return new ResponseEntity<>(Admin, HttpStatus.OK);
    }

    @PutMapping("/superadmin/ubah-foto/{id}")
    public ResponseEntity<?>EditFotoSuperAdmin(@PathVariable Long id , @RequestPart MultipartFile image  ){
        try {
            SuperAdmin updateSuperAdmin = superAdminService.uploadImage(id, image );
            return new ResponseEntity<>(updateSuperAdmin, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/superadmin/getAll")
    public ResponseEntity<List<SuperAdmin>> getAllSuperAdmin() {
        List<SuperAdmin> superAdmins = (List<SuperAdmin>) superAdminService.getAllSuperAdmin();
        return ResponseEntity.ok(superAdmins);
    }

    @GetMapping("/superadmin/getbyid/{id}")
    public ResponseEntity<SuperAdmin> getSuperAdminById(@PathVariable Long id) {
        Optional<SuperAdmin> superAdmin = superAdminService.getSuperadminbyId(id);
        return superAdmin.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping("/superadmin/register")
    public ResponseEntity<SuperAdmin> register(@RequestBody SuperAdmin superAdmin){

        return ResponseEntity.ok( superAdminService.RegisterSuperAdmin(superAdmin));
    }


    @PostMapping("/superadmin/tambahdata/{id} ")
    public ResponseEntity<SuperAdmin> tambahSuperAdmin(@PathVariable Long id,
                                                       @RequestBody SuperAdmin superAdmin,
                                                       @RequestPart("image") MultipartFile image) throws IOException {
        SuperAdmin newSuperAdmin = superAdminService.tambahSuperAdmin(id, superAdmin, image);
        return ResponseEntity.ok(newSuperAdmin);
    }

    @PutMapping("/superadmin/edit/{id}")
    public ResponseEntity<SuperAdmin> editSuperAdmin(@PathVariable Long id,
                                                     @RequestPart("image") MultipartFile image,
                                                     @RequestBody SuperAdmin superAdmin) throws IOException {
        SuperAdmin editedSuperAdmin = superAdminService.EditSuperAdmin(id, image, superAdmin);
        return ResponseEntity.ok(editedSuperAdmin);
    }

    @PutMapping(path = "/superadmin/edit-password/{id}")
    public CommonResponse<SuperAdmin> putPassword(@RequestBody PasswordDTO password, @PathVariable Long id ) {
        return ResponseHelper.ok(superAdminService.putPasswordSuperAdmin(password , id));
    }

    @DeleteMapping("/superadmin/delete/{id}")
    public ResponseEntity<Void> deleteSuperAdmin(@PathVariable Long id) throws IOException {
        superAdminService.deleteSuperAdmin(id);
        return ResponseEntity.ok().build();
    }
}
