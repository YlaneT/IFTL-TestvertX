package com.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class Server extends AbstractVerticle {
    
    @Override
    public void start (Promise<Void> startPromise) throws Exception {
        // Création du routeur
        Router router = Router.router(vertx);
        // Définition des routes
        router.get("/").handler(req -> {
            req.response()
               .putHeader("content-type", "text/plain")
               .end("Hello from Vert.X !");
        });
        router.get("/shutdown").handler(req -> {
//            Main.observableShutdown.setShutdownCalled(true);
            Main.closeVertx();
            req.response().putHeader("content-type", "text/plain").end("Server closed");
        });
        
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
    
//    private void shutdown (RoutingContext routingContext) {
//        System.out.println("shutdown vertex server...");
//        try {
//            Main.observableShutdown.setShutdownCalled(true);
//        } catch (Exception e) {
//            System.err.println("Error during server stopping : ");
//        }
//    }
    
    // Quand le verticle s'arrête
    @Override
    public void stop () throws Exception {
        System.out.println("Stop Vertx server by YT");
    }
}
