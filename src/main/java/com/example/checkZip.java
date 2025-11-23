package com.example;

import java.io.*;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.*;
import java.nio.charset.StandardCharsets;

public class checkZip {

    public static String[][] report;
    public static String Rprt;
    public static boolean hasProtection;

    private static void printReport() {
        if (!hasProtection) {
            Rprt = "<font color='blue'>This workbook is not protected.</font>";
            return;
        }
        Rprt = "<table>";
        for (int i = 0; i < report.length; i++) {
            Rprt += String.format("<tr><td>%d</td><td>%s</td><td>%s</td><td>%s</td></tr>",
                    i + 1, report[i][0], report[i][1], report[i][2]);
        }
        Rprt += "</table><br><a href='/download'>Download unlocked file</a>";
    }

    public static void replaceFileInArchive(Path inputZipPath) throws IOException {

        String sheetRegex = "^xl/worksheets/sheet\\d+\\.xml$";
        hasProtection = false;
        Map<String, byte[]> entries = new HashMap<>();

        try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(inputZipPath))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {

                String entryName = entry.getName();
                if (entry.isDirectory()) {
                    entries.put(entryName, null);
                    continue;
                }

                byte[] content = zis.readAllBytes();
                String fullString = new String(content, StandardCharsets.UTF_8);

                if (entryName.equals("xl/workbook.xml")) {
                    content = checkXML.checkBook(fullString);
                    entries.put(entryName, content);

                } else if (entryName.matches(sheetRegex)) {
                    content = checkXML.checkSheet(entryName, fullString);
                    entries.put(entryName, content);
                } else {
                    entries.put(entryName, content);
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }

        //Path tempZipPath = Files.createTempFile("modified", ".xlsx");
        Data.tempZipPath = Files.createTempFile("modified", ".xlsx");

        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(Data.tempZipPath))) {
            for (Map.Entry<String, byte[]> entry : entries.entrySet()) {
                String entryName = entry.getKey();
                byte[] content = entry.getValue();

                ZipEntry newEntry = new ZipEntry(entryName);
                zos.putNextEntry(newEntry);
                if (content != null)
                    zos.write(content);
                zos.closeEntry();
            }
        }

        // --- REPLACE ORIGINAL ARCHIVE
        Files.move(Data.tempZipPath, Paths.get("unlocked.xlsx").toAbsolutePath(), StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Saved: " + Paths.get("unlocked.xlsx").toAbsolutePath().toString());
        printReport();
    }
}