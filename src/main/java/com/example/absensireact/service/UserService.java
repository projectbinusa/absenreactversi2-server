package com.example.absensireact.service;

import com.example.absensireact.dto.ForGotPass;
import com.example.absensireact.dto.PasswordDTO;
import com.example.absensireact.dto.ResetPassDTO;
import com.example.absensireact.dto.VerifyCode;
import com.example.absensireact.model.Reset_Password;
import com.example.absensireact.model.User;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public interface UserService {

//    Map<Object, Object> login(LoginRequest loginRequest, HttpServletRequest request);

//    User Register(User user);


    User ubahPassByForgot(ResetPassDTO updatePass);


    Reset_Password validasiCodeUniqResPass(VerifyCode codeUser);

    ForGotPass sendEmail(ForGotPass forGotPass) throws MessagingException;

    User Register(User user, Long idOrganisasi, Long idShift);

    List<User> getAllByJabatan(Long idJabatan);

    List<User> getAllByAdmin(Long idAdmin);

    List<User> getAllByShift(Long idShift);

    User editUsernameJabatanShift(Long id, Long idJabatan, Long idShift, User user);

    User putPassword(PasswordDTO passwordDTO, Long id);


    User ubahUsernamedanemail(Long id, User updateUser);

    User EditUserBySuper(Long id, Long idJabatan, Long idShift, User updateUser);

    User Tambahkaryawan(User user, Long idAdmin, Long idOrganisasi, Long idJabatan, Long idShift);

    List<User> GetAllKaryawanByIdAdmin(Long idAdmin);


    User getById(Long id);

    List<User> getAll();


    User fotoUser(Long id, MultipartFile image) throws  IOException;

    User edit(Long id, User user);

    void delete(Long id) throws IOException;

    void deleteUser(Long id);
}
