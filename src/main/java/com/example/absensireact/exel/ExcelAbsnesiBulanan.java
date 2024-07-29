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
import java.io.InputStream;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ExcelAbsnesiBulanan {
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


    public void excelAbsensiBulanan(int month, int year, HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Absensi-Bulanan");

        Font fontWhite = workbook.createFont();
        fontWhite.setColor(IndexedColors.WHITE.getIndex()); // Set font color to white

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

        // Fetch data
        List<Absensi> absensiList = absensiRepository.findByMonthAndYear(month, year);

        if (absensiList.isEmpty()) {
            // Handle case when there are no absences for the given month and year
            Row emptyRow = sheet.createRow(0);
            Cell emptyCell = emptyRow.createCell(0);
            emptyCell.setCellValue("Tidak ada data absensi untuk bulan " + getMonthName(month) + " tahun " + year);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4)); // Merge cells for message
        } else {
            // Group by user
            Map<String, List<Absensi>> userAbsensiMap = new HashMap<>();
            for (Absensi absensi : absensiList) {
                userAbsensiMap.computeIfAbsent(absensi.getUser().getUsername(), k -> new ArrayList<>()).add(absensi);
            }

            int rowNum = 0;

            // Title row
            Row titleRow = sheet.createRow(rowNum++);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("DATA ABSENSI BULAN : " + getMonthName(month) + " - " + year);
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
        }

        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=AbsensiBulanan_" + getMonthName(month) + "_" + year + ".xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
    }


        public void excelAbsensiBulananSimpel(int month, int year ,  HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Absensi-Simpel");

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

        // Fetch data
        List<Absensi> absensiList = absensiRepository.findByMonthAndYear(month , year);

        if (absensiList.isEmpty()) {
            // Handle case when there are no absences for the given month and year
            Row emptyRow = sheet.createRow(0);
            Cell emptyCell = emptyRow.createCell(0);
            emptyCell.setCellValue("Tidak ada data absensi simpel untuk bulan " + getMonthName(month) + "-" + year );
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4)); // Merge cells for message
        } else {
            // Group by user
            Map<String, List<Absensi>> userAbsensiMap = new HashMap<>();
            for (Absensi absensi : absensiList) {
                userAbsensiMap.computeIfAbsent(absensi.getUser().getUsername(), k -> new ArrayList<>()).add(absensi);
            }

            int rowNum = 0;

            // Title row
            Row titleRow = sheet.createRow(rowNum++);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("DATA ABSENSI SIMPEL : " + getMonthName(month) );
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
        }

        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=AbsensiSimpel" + getMonthName(month) + ".xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
    }





    private int countStatus(List<Absensi> absensiList, String status) {
        int count = 0;
        for (Absensi absensi : absensiList) {
            if (absensi.getStatusAbsen().equals(status)) {
                count++;
            }
        }
        return count;
    }
    public List<Absensi> importFromExcel(InputStream inputStream) throws IOException {
        List<Absensi> absensiList = new ArrayList<>();


        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> iterator = sheet.iterator();
            while (iterator.hasNext()) {
                Row currentRow = iterator.next();

                if (currentRow.getRowNum() == 0) {
                    continue;
                }

                Absensi absensi = new Absensi();

                Iterator<Cell> cellIterator = currentRow.iterator();
                while (cellIterator.hasNext()) {
                    Cell currentCell = cellIterator.next();
                    int columnIndex = currentCell.getColumnIndex();

                    switch (columnIndex) {
                        case 0:
                            absensi.setTanggalAbsen(currentCell.getDateCellValue());
                            break;
                        case 1:
                            absensi.setJamMasuk(currentCell.getStringCellValue());
                            break;
                        case 2:
                            absensi.setLokasiMasuk(currentCell.getStringCellValue());
                            break;
                    }
                }

                absensiList.add(absensi);
            }
        }
        return absensiList;
    }
}
