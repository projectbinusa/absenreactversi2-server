package com.example.absensireact.helper;

import com.example.absensireact.exception.NotFoundException;
import com.example.absensireact.model.Cuti;
import com.example.absensireact.model.Lembur;
import com.example.absensireact.model.User;
import com.example.absensireact.repository.LemburRepository;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component
public class LemburPDF {

    @Autowired
    private LemburRepository lemburRepository;

    public void generatePDF(Long id, ByteArrayOutputStream baos) throws IOException {
        Lembur lembur = lemburRepository.findById(id).orElseThrow(() -> new NotFoundException("Id lembur tidak ditemukan"));

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy");

        String tanggalLembur = formatDate(lembur.getTanggalLembur(), inputFormat, outputFormat);
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
                "<h1 style='font-weight: bold; font-size: 24px;'>Pernyataan Lembur</h1>" +
                "<h2 style='font-weight: bold; font-size: 20px;'>Excelent Computer</h2>" +
//                "<p>Jl. Kemantren Raya No.5, RT.02/RW.04, Wonosari, Kec. Ngaliyan, Kota Semarang, Jawa Tengah 50186</p>" +
                "<hr/>" +
                "</div>" +
                "<div class='content'>" +
                "<p>Yth. HRD PT. Exxcelent Computer</p>" +
                "<p>di tempat</p>" +
                "<p>Dengan hormat,<br>Yang bertanda tangan di bawah ini:</p>" +
                "<p>Nama: " + lembur.getUser().getUsername() + "</p>" +
                "<p>Jabatan: " + lembur.getUser().getJabatan().getNamaJabatan() + "</p>" +
                "<p>Untuk melaksanakan lembur pada : " + tanggalLembur + "</p>" +
                "<p>Keterangan Lembur : " + lembur.getKeteranganLembur() + "</p>" +
                "<p>Demikian surat ini buat , untuk digunakan sebagaimana mestinya</p>" +
                "<p>Terimakasih atas perhatian  Bapak/Ibu</p>" +
                "<div class='signature-container'>" +
                "<div class='left-signature'>" +
                "<p>_________________________</p>" +
                "<p style='text-align: center;'><b>(" + lembur.getUser().getUsername() + ")</b></p>" +
                "</div>" +
                "<div class='right-signature'>" +
                "<p>_________________________</p>" +
                "<p style='text-align: center;'><b>(" + lembur.getUser().getAdmin().getUsername() + ")</b></p>" +
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
