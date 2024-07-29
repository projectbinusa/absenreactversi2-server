package com.example.absensireact.exel;

import com.example.absensireact.model.Absensi;
import com.example.absensireact.repository.AbsensiRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ExcelAbsensiMingguan {

    @Autowired
    private AbsensiRepository absensiRepository;

    private String getMonthName(int month) {
        String[] monthNames = new DateFormatSymbols().getMonths();
        int index = month - 1;
        if (index >= 0 && index < monthNames.length) {
            return monthNames[index];
        }
        return "Bulan Tidak Valid";
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        return sdf.format(date);
    }
    public void excelAbsensiMingguan(Date tanggalAwal, Date tanggalAkhir, HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Absensi-mingguan");
        int rowNum = 0;

        Font fontWhite = workbook.createFont();
        fontWhite.setColor(IndexedColors.WHITE.getIndex());

        // Cell styles
        CellStyle styleHeader = workbook.createCellStyle();
        styleHeader.setAlignment(HorizontalAlignment.CENTER);
        styleHeader.setVerticalAlignment(VerticalAlignment.CENTER);
        styleHeader.setBorderTop(BorderStyle.THIN);
        styleHeader.setBorderRight(BorderStyle.THIN);
        styleHeader.setBorderBottom(BorderStyle.THIN);
        styleHeader.setBorderLeft(BorderStyle.THIN);

        CellStyle styleTitle = workbook.createCellStyle();
        styleTitle.setAlignment(HorizontalAlignment.CENTER);
        styleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        styleTitle.setFont(titleFont);

        CellStyle styleNumber = workbook.createCellStyle();
        styleNumber.setAlignment(HorizontalAlignment.CENTER);
        styleNumber.setVerticalAlignment(VerticalAlignment.CENTER);
        styleNumber.setBorderTop(BorderStyle.THIN);
        styleNumber.setBorderRight(BorderStyle.THIN);
        styleNumber.setBorderBottom(BorderStyle.THIN);
        styleNumber.setBorderLeft(BorderStyle.THIN);
        styleNumber.setFont(fontWhite);

        CellStyle styleCenterNumber = workbook.createCellStyle();
        styleCenterNumber.setAlignment(HorizontalAlignment.CENTER);
        styleCenterNumber.setVerticalAlignment(VerticalAlignment.CENTER);
        styleCenterNumber.setBorderTop(BorderStyle.THIN);
        styleCenterNumber.setBorderRight(BorderStyle.THIN);
        styleCenterNumber.setBorderBottom(BorderStyle.THIN);
        styleCenterNumber.setBorderLeft(BorderStyle.THIN);

        // Conditional formatting colors
        CellStyle styleColorLate = workbook.createCellStyle();
        styleColorLate.cloneStyleFrom(styleNumber);
        styleColorLate.setFillForegroundColor(IndexedColors.RED.getIndex());
        styleColorLate.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle styleColorPermission = workbook.createCellStyle();
        styleColorPermission.cloneStyleFrom(styleNumber);
        styleColorPermission.setFillForegroundColor(IndexedColors.DARK_YELLOW.getIndex());
        styleColorPermission.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle styleColorEarly = workbook.createCellStyle();
        styleColorEarly.cloneStyleFrom(styleNumber);
        styleColorEarly.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        styleColorEarly.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        List<Absensi> absensiList = absensiRepository.findByMingguan(tanggalAwal, tanggalAkhir);

        Map<String, List<Absensi>> userAbsensiMap = new HashMap<>();
        for (Absensi absensi : absensiList) {
            userAbsensiMap.computeIfAbsent(absensi.getUser().getUsername(), k -> new ArrayList<>()).add(absensi);
        }

        // Title row
        Row titleRow = sheet.createRow(rowNum++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("DATA ABSENSI MINGGUAN : " + formatDate(tanggalAwal) + " - " + formatDate(tanggalAkhir));
        titleCell.setCellStyle(styleTitle);
        sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 4)); // Merging cells for title

        for (Map.Entry<String, List<Absensi>> entry : userAbsensiMap.entrySet()) {
            String userName = entry.getKey();
            List<Absensi> userAbsensi = entry.getValue();
            String position = userAbsensi.get(0).getUser().getJabatan().getNamaJabatan();

            // Variables to count absences for each user
            int userTotalLate = 0;
            int userTotalPermission = 0;
            int userTotalEarly = 0;

            // Name and Position row
            Row nameRow = sheet.createRow(rowNum++);
            Cell nameCell = nameRow.createCell(0);
            nameCell.setCellValue("Nama :  " + userName);
            nameCell.setCellStyle(styleHeader);

            Row positionRow = sheet.createRow(rowNum++);
            Cell positionCell = positionRow.createCell(0);
            positionCell.setCellValue("Jabatan :   " + position);
            positionCell.setCellStyle(styleHeader);

            // Header row
            Row headerRow = sheet.createRow(rowNum++);
            String[] headers = {"No", "Tanggal Absen", "Jam Masuk", "Jam Pulang", "Keterangan"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(styleHeader);
            }

            // Data rows
            int userRowNum = 1;
            for (Absensi absensi : userAbsensi) {
                Row row = sheet.createRow(rowNum++);
                Cell cell0 = row.createCell(0);
                cell0.setCellValue(userRowNum++);
                cell0.setCellStyle(styleCenterNumber); // Use the centered number style

                Cell cell1 = row.createCell(1);
                cell1.setCellValue(new SimpleDateFormat("dd MMM yyyy").format(absensi.getTanggalAbsen())); // Use the new date format
                cell1.setCellStyle(styleNumber);

                Cell cell2 = row.createCell(2);
                cell2.setCellValue(absensi.getJamMasuk());
                cell2.setCellStyle(styleNumber);

                Cell cell3 = row.createCell(3);
                cell3.setCellValue(absensi.getJamPulang());
                cell3.setCellStyle(styleNumber);

                Cell cell4 = row.createCell(4);
                cell4.setCellValue(absensi.getStatusAbsen());
                cell4.setCellStyle(styleNumber);

                CellStyle styleColor = null;
                switch (absensi.getStatusAbsen()) {
                    case "Terlambat":
                        styleColor = styleColorLate;
                        userTotalLate++; // Increment late count
                        break;
                    case "Izin":
                        styleColor = styleColorPermission;
                        userTotalPermission++; // Increment permission count
                        break;
                    case "Lebih Awal":
                        styleColor = styleColorEarly;
                        userTotalEarly++; // Increment early count
                        break;
                }
                if (styleColor != null) {
                    for (int i = 0; i < headers.length; i++) {
                        row.getCell(i).setCellStyle(styleColor);
                    }
                }
            }

            // Adding summary row for each user
            Row lateRow = sheet.createRow(rowNum++);
            Cell lateCell = lateRow.createCell(0);
            lateCell.setCellValue("Terlambat: " + userTotalLate);
            lateCell.setCellStyle(styleHeader);

            Row permissionRow = sheet.createRow(rowNum++);
            Cell permissionCell = permissionRow.createCell(0);
            permissionCell.setCellValue("Izin: " + userTotalPermission);
            permissionCell.setCellStyle(styleHeader);

            Row earlyRow = sheet.createRow(rowNum++);
            Cell earlyCell = earlyRow.createCell(0);
            earlyCell.setCellValue("Lebih Awal: " + userTotalEarly);
            earlyCell.setCellStyle(styleHeader);

            // Add a blank row between each user's table for readability
            rowNum++;
        }

        // Adjust column width
        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=AbsensiMingguan.xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    public void excelAbsensiHarian(Date tanggal, HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Absensi-harian");


        Font fontWhite = workbook.createFont();
        fontWhite.setColor(IndexedColors.WHITE.getIndex());


        // Cell styles
        CellStyle styleHeader = workbook.createCellStyle();
        styleHeader.setAlignment(HorizontalAlignment.CENTER);
        styleHeader.setVerticalAlignment(VerticalAlignment.CENTER);
        styleHeader.setBorderTop(BorderStyle.THIN);
        styleHeader.setBorderRight(BorderStyle.THIN);
        styleHeader.setBorderBottom(BorderStyle.THIN);
        styleHeader.setBorderLeft(BorderStyle.THIN);

        CellStyle styleTitle = workbook.createCellStyle();
        styleTitle.setAlignment(HorizontalAlignment.CENTER);
        styleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        styleTitle.setFont(titleFont);

        CellStyle styleNumber = workbook.createCellStyle();
        styleNumber.setAlignment(HorizontalAlignment.CENTER);
        styleNumber.setVerticalAlignment(VerticalAlignment.CENTER);
        styleNumber.setBorderTop(BorderStyle.THIN);
        styleNumber.setBorderRight(BorderStyle.THIN);
        styleNumber.setBorderBottom(BorderStyle.THIN);
        styleNumber.setBorderLeft(BorderStyle.THIN);
        styleNumber.setFont(fontWhite);


        CellStyle styleCenterNumber = workbook.createCellStyle();
        styleCenterNumber.setAlignment(HorizontalAlignment.CENTER);
        styleCenterNumber.setVerticalAlignment(VerticalAlignment.CENTER);
        styleCenterNumber.setBorderTop(BorderStyle.THIN);
        styleCenterNumber.setBorderRight(BorderStyle.THIN);
        styleCenterNumber.setBorderBottom(BorderStyle.THIN);
        styleCenterNumber.setBorderLeft(BorderStyle.THIN);

        // Conditional formatting colors
        CellStyle styleColorLate = workbook.createCellStyle();
        styleColorLate.cloneStyleFrom(styleNumber);
        styleColorLate.setFillForegroundColor(IndexedColors.RED.getIndex());
        styleColorLate.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle styleColorPermission = workbook.createCellStyle();
        styleColorPermission.cloneStyleFrom(styleNumber);
        styleColorPermission.setFillForegroundColor(IndexedColors.DARK_YELLOW.getIndex());
        styleColorPermission.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle styleColorEarly = workbook.createCellStyle();
        styleColorEarly.cloneStyleFrom(styleNumber);
        styleColorEarly.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        styleColorEarly.setFillPattern(FillPatternType.SOLID_FOREGROUND);



        Calendar calendar = Calendar.getInstance();
        calendar.setTime(tanggal);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH is zero-based
        int year = calendar.get(Calendar.YEAR);

        List<Absensi> absensiList = absensiRepository.findByTanggalAbsen(day, month, year);

        Map<String, List<Absensi>> userAbsensiMap = new HashMap<>();
        for (Absensi absensi : absensiList) {
            userAbsensiMap.computeIfAbsent(absensi.getUser().getUsername(), k -> new ArrayList<>()).add(absensi);
        }

        int rowNum = 0;


        // Title row
        Row titleRow = sheet.createRow(rowNum++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("DATA ABSENSI HARIAN : " + day + " " + getMonthName(month) + " " + year);
        titleCell.setCellStyle(styleTitle);
        sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 4)); // Merging cells for title
        rowNum++;

        for (Map.Entry<String, List<Absensi>> entry : userAbsensiMap.entrySet()) {
            String userName = entry.getKey();
            List<Absensi> userAbsensi = entry.getValue();
            String position = userAbsensi.get(0).getUser().getJabatan().getNamaJabatan();

            // Variables to count absences for each user
            int userTotalLate = 0;
            int userTotalPermission = 0;
            int userTotalEarly = 0;

            // Name and Position row
            Row nameRow = sheet.createRow(rowNum++);
            Cell nameCell = nameRow.createCell(0);
            nameCell.setCellValue("Nama :  " + userName);
            nameCell.setCellStyle(styleHeader);

            Row positionRow = sheet.createRow(rowNum++);
            Cell positionCell = positionRow.createCell(0);
            positionCell.setCellValue("Jabatan :   " + position);
            positionCell.setCellStyle(styleHeader);

            // Add a blank row between header and data
            rowNum++;

            // Header row
            Row headerRow = sheet.createRow(rowNum++);
            String[] headers = {"No", "Tanggal Absen", "Jam Masuk", "Jam Pulang", "Keterangan"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(styleHeader);
            }

            // Data rows
            int userRowNum = 1;
            for (Absensi absensi : userAbsensi) {
                Row row = sheet.createRow(rowNum++);
                Cell cell0 = row.createCell(0);
                cell0.setCellValue(userRowNum++);
                cell0.setCellStyle(styleCenterNumber); // Use the centered number style

                Cell cell1 = row.createCell(1);
                cell1.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(absensi.getTanggalAbsen()));
                cell1.setCellStyle(styleNumber);

                Cell cell2 = row.createCell(2);
                cell2.setCellValue(absensi.getJamMasuk());
                cell2.setCellStyle(styleNumber);

                Cell cell3 = row.createCell(3);
                cell3.setCellValue(absensi.getJamPulang());
                cell3.setCellStyle(styleNumber);

                Cell cell4 = row.createCell(4);
                cell4.setCellValue(absensi.getStatusAbsen());
                cell4.setCellStyle(styleNumber);

                CellStyle styleColor = null;
                rowNum++;
                switch (absensi.getStatusAbsen()) {
                    case "Terlambat":
                        styleColor = styleColorLate;
                        userTotalLate++; // Increment late count
                        break;
                    case "Izin":
                        styleColor = styleColorPermission;
                        userTotalPermission++; // Increment permission count
                        break;
                    case "Lebih Awal":
                        styleColor = styleColorEarly;
                        userTotalEarly++; // Increment early count
                        break;
                }
                if (styleColor != null) {
                    for (int i = 0; i < headers.length; i++) {
                        row.getCell(i).setCellStyle(styleColor);
                    }
                }
            }

            // Adding summary row for each user
            Row lateRow = sheet.createRow(rowNum++);
            Cell lateCell = lateRow.createCell(0);
            lateCell.setCellValue("Terlambat: " + userTotalLate);
            lateCell.setCellStyle(styleHeader);

            Row permissionRow = sheet.createRow(rowNum++);
            Cell permissionCell = permissionRow.createCell(0);
            permissionCell.setCellValue("Izin: " + userTotalPermission);
            permissionCell.setCellStyle(styleHeader);

            Row earlyRow = sheet.createRow(rowNum++);
            Cell earlyCell = earlyRow.createCell(0);
            earlyCell.setCellValue("Lebih Awal: " + userTotalEarly);
            earlyCell.setCellStyle(styleHeader);

            // Add a blank row between each user's table for readability
            rowNum++;
        }

        // Adjust column width
        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=AbsensiHarian.xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    }
