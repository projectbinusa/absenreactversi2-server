package com.example.absensireact.helper;

import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.Cuti;
import com.example.absensireact.model.User;
import com.example.absensireact.repository.CutiRepository;
import com.example.absensireact.repository.UserRepository;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component
public class CutiPDF {

    private final CutiRepository cutiRepository;

    @Autowired
    private UserRepository userRepository;

    public CutiPDF(CutiRepository cutiRepository) {
        this.cutiRepository = cutiRepository;
    }

    public void generatePDF(Long id, ByteArrayOutputStream baos) throws IOException {
        Cuti cuti = cutiRepository.findById(id).orElseThrow(() -> new NotFoundException("Id cuti tidak ditemukan"));
        User user = userRepository.findById(cuti.getUser().getId())
                .orElseThrow(() -> new NotFoundException("user tidak ditemukan"));

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy");

        String awalCuti = formatDate(cuti.getAwalCuti(), inputFormat, outputFormat);
        String akhirCuti = formatDate(cuti.getAkhirCuti(), inputFormat, outputFormat);
        String masukKerja = formatDate(cuti.getMasukKerja(), inputFormat, outputFormat);

        String htmlContent = "<html><head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; }" +
                ".header { text-align: center; margin-bottom: 20px; }" +
                ".address { text-align: center; }" +
                ".content p { margin-top: 20px; margin-left: 50px; }" +
                ".signature-container { clear: both; margin-top: 250px; }" +
                ".left-signature { float: left; margin-right: 50px; }" +
                ".right-signature { float: right; margin-left: 50px; }" +
                "</style>" +
                "</head><body>" +
                "<div class='header'>" +
                "<h1 style='font-weight: bold; font-size: 24px;'>Permohonan Cuti</h1>" +
                "<h2 style='font-weight: bold; font-size: 20px;'>SMK BINA NUSANTARA SEMARANG</h2>" +
                "<p>Jl. Kemantren Raya No.5, RT.02/RW.04, Wonosari, Kec. Ngaliyan, Kota Semarang, Jawa Tengah 50186</p>" +
                "<hr/>" +
                "</div>" +
                "<div class='content'>" +
                "<p>Yth. HRD PT. SMK Bina Nusantara Semarang</p>" +
                "<p>di tempat</p>" +
                "<p>Dengan hormat,<br>Yang bertanda tangan di bawah ini:</p>" +
                "<p>Nama: " + user.getUsername() + "</p>" +
                "<p>Jabatan: " + user.getJabatan().getNamaJabatan() + "</p>" +
                "<p>Tanggal Pengambilan Cuti: " + awalCuti + "</p>" +
                "<p>Tanggal Kembali Kerja: " + masukKerja + "</p>" +
                "<p>Keperluan: " + cuti.getKeperluan() + "</p>" +
                "<p>Bermaksud mengajukan cuti tahunan dari <strong>" + awalCuti + " hingga " + akhirCuti + "</strong>, saya akan mulai bekerja kembali pada <strong>" + masukKerja + "</strong>.</p>" +
                "<p>Demikian permohonan cuti ini saya ajukan. Terima kasih atas perhatian Bapak/Ibu.</p>" +
                "<p>Tanggal: " + awalCuti + "</p>" +
                "<div class='signature-container'>" +
                "<div class='left-signature'>" +
                "<p>_________________________</p>" +
                "<p style='text-align: center;'><b>(" + user.getUsername() + ")</b></p>" +
                "</div>" +
                "<div class='right-signature'>" +
                "<p>_________________________</p>" +
                "<p style='text-align: center;'><b>(" + user.getAdmin().getUsername() + ")</b></p>" +
                "</div>" +
                "</div>" +
                "</div>" +
                "</body></html>";


        ConverterProperties properties = new ConverterProperties();
        HtmlConverter.convertToPdf(htmlContent, baos, properties);
    }

    private String formatDate(String dateString, SimpleDateFormat inputFormat, SimpleDateFormat outputFormat) {
        try {
            return outputFormat.format(inputFormat.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString;
        }
    }
}
