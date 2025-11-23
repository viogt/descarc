package com.example;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

public class App {
    public static void main(String[] args) {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "7000"));
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add(staticFiles -> {
                staticFiles.directory = "/public";
                staticFiles.location = Location.CLASSPATH;
            });
        }).start(port);

        app.get("/", ctx -> Data.serve(ctx, "/public/index.html"));
        app.post("/upload", ctx -> Data.upload(ctx));
        app.get("/download", ctx -> Data.downloadFile(ctx));
        app.error(404, ctx -> ctx.redirect("/"));
        System.out.println("> Javalin: http://localhost:" + port);
    }
}
