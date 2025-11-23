package com.example;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import io.javalin.http.ContentType;
import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import java.io.FileInputStream;

public class Data {

    public static void upload(Context ctx) {
        UploadedFile uploadedFile = ctx.uploadedFile("myFile");

        if (uploadedFile == null) {
            ctx.status(400).result("No file uploaded under the name 'myFile'.");
            return;
        }
        System.out.println("File uploaded: " + uploadedFile.filename());

        // File destination = new File("uploads_" + uploadedFile.filename());
        File destination = new File("original.xlsx");
        try (InputStream inputStream = uploadedFile.content()) {

            System.out.println("And saved as " + destination.toPath());
            Files.copy(inputStream, destination.toPath(), StandardCopyOption.REPLACE_EXISTING);

            checkZip.replaceFileInArchive(destination.toPath());

            /*ctx.result("File <u>" + uploadedFile.filename() + "</u> (" + uploadedFile.size() + " bytes, "
                    + uploadedFile.contentType() + ") <font color='darkgreen'>uploaded successfully!</font><br><a href='/download'>Download unlocked file</a>");*/
            ctx.result(checkZip.Rprt);
        } catch (Exception e) {
            ctx.status(500).result("File upload failed: " + e.getMessage());
            return;
        }
    }

    public static void serve(Context ctx, String path) {
        ctx.contentType(ContentType.TEXT_HTML);
        InputStream inputStream = App.class.getResourceAsStream(path);
        if (inputStream != null) ctx.result(inputStream);
        else ctx.status(404).result("Index file not found.");
    }

    public static void downloadFile(Context ctx) throws Exception {
        
        File fileToDownload = new File(Paths.get("unlocked.xlsx").toString());
        if(!fileToDownload.exists()) {
            ctx.result("File "+ fileToDownload.getPath() +" doesn't exist.");
            return;
        }
        ctx.contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        ctx.header("Content-Disposition", "attachment; filename=\"" + fileToDownload.getName() + "\"");
        ctx.header("Content-Length", String.valueOf(fileToDownload.length()));
        if (fileToDownload.exists()) ctx.result(new FileInputStream(fileToDownload));
        else ctx.status(404).result("File not found.");
    }
}