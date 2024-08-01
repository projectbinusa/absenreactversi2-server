package com.example.absensireact.service;

import com.example.absensireact.dto.ForGotPass;
import com.example.absensireact.dto.PasswordDTO;
import com.example.absensireact.dto.ResetPassDTO;
import com.example.absensireact.dto.VerifyCode;
import com.example.absensireact.model.Reset_Password;
import com.example.absensireact.model.SuperAdmin;
import com.example.absensireact.model.User;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Optional;

public interface SuperAdminService {
    SuperAdmin ubahPassByForgot (ResetPassDTO updatePass);

    Reset_Password validasiCodeUniqResPass(VerifyCode codeUser);

    ForGotPass sendEmail(ForGotPass forGotPass) throws MessagingException;

    SuperAdmin getAllSuperAdmin();

    Optional<SuperAdmin> getSuperadminbyId(Long id);


    SuperAdmin RegisterSuperAdmin(SuperAdmin superAdmin);

    SuperAdmin tambahSuperAdmin(Long id, SuperAdmin superAdmin, MultipartFile image) throws IOException;

    SuperAdmin EditSuperAdmin(Long id, MultipartFile image, SuperAdmin superAdmin) throws IOException;

    SuperAdmin putPasswordSuperAdmin(PasswordDTO passwordDTO, Long id);

    void deleteSuperAdmin(Long id)throws IOException ;

    SuperAdmin ubahUsernamedanemail(Long id, SuperAdmin updateadmin);

    SuperAdmin uploadImage(Long id, MultipartFile image) throws IOException;
}
