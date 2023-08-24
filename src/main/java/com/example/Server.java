package com.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;

public class Server extends AbstractVerticle {
    
    @Override
    public void start (Promise<Void> startPromise) {
        // Création du routeur
        Router router = Router.router(vertx);
        // Définition des routes
        router.get("/shutdown").handler(req -> {
            Main.closeVertx();
            req.response().putHeader("content-type", "text/plain").end("Server closed");
        });
        router.get("/*").handler(req -> req.response()
                                           .putHeader("content-type", "text/plain")
                                           .end("Hello from Vert.X !"));
        
        vertx.createHttpServer().requestHandler(router).listen(8888, http -> {
            if (http.succeeded()) {
                startPromise.complete();
                System.out.println("HTTP server started on port 8888");
            } else {
                startPromise.fail(http.cause());
                System.out.println("HTTP server COULDN'T start");
            }
        });
    }
    
    // Quand le verticle s'arrête
    @Override
    public void stop () {
        System.out.println("Stop Verticle");
    }
}
