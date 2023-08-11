package com.example.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.PfxOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Server extends AbstractVerticle {
    private static final Logger log = LoggerFactory.getLogger(AbstractVerticle.class);

    private static final String[]   requestStatusTable = {
        "STATUS_INITIALIZED",
        "STATUS_ENDED",
        "STATUS_VALIDATED",
        "STATUS_CANCELED",
        "STATUS_ABORTED",
        "STATUS_WORKING",
        "STATUS_ERROR",
        "STATUS_PROCEEDED",
        "STATUS_CLOSED"
    };
    private              int        listenPort         = 443;
    private              String     bindAddress        = "0.0.0.0";
    private              String     keystoreType       = "JKS";
    private              String     keystorePath       = "C:\\_Infotel\\prj_idea\\CallBackRest\\etc\\my_keystore.jks";
    private              String     keystorePassword   = "infotel0";
    private              String     aliasName          = "arcsys";
    private              String     aliasPassword      = "infotel0";
    private              boolean    https              = true;
    private              HttpServer server             = null;

    // Quand le verticle se lance
    @Override
    public void start () {
        try {
            beforeStopJVM();
            // for https look at https://gist.github.com/InfoSec812/a45eb3b7ba9d4b2a9b94
            listenPort = Integer.parseInt(ReadProperties.getParameter("LISTEN_PORT"));
            bindAddress = ReadProperties.getParameter("BIND_ADDRESS");
            keystoreType = ReadProperties.getParameter("KEYSTORE_TYPE");
            keystorePath = ReadProperties.getParameter("KEYSTORE_PATH");
            keystorePassword = ReadProperties.getParameter("KEYSTORE_PASSWORD");
            aliasName = ReadProperties.getParameter("KEYSTORE_ALIAS");
            aliasPassword = ReadProperties.getParameter("KEYSTORE_ALIAS_PASSWORD");
            keystoreType = ReadProperties.getParameter("KEYSTORE_TYPE");
            https = Boolean.parseBoolean(ReadProperties.getParameter("HTTPS"));

            System.out.println("Start Vertx server by XL");

            HttpServerOptions httpOpts = new HttpServerOptions();
            if (https) {
                if (keystoreType.equals("JKS")) {
                    System.out.println("keystore type = JKS");
                    httpOpts.setKeyStoreOptions(new JksOptions().setAlias(aliasName)
                                                                .setAliasPassword(aliasPassword)
                                                                .setPassword(keystorePassword)
                                                                .setPath(keystorePath));
                } else if (keystoreType.equals("PKCS12")) {
                    System.out.println("keystore type = PKCS12");
                    httpOpts.setPfxKeyCertOptions(new PfxOptions().setAlias(aliasName)
                                                                  .setAliasPassword(aliasPassword)
                                                                  .setPassword(keystorePassword)
                                                                  .setPath(keystorePath));
                } else {
                    System.err.println("keystoreType must be equal to JKS or PKCS12");
                    System.exit(42);
                }
                httpOpts.setSsl(true);
            }

            System.out.println(httpOpts.isSsl() ? "https enabled" : "https disabled");

            // Création du routeur
            Router router = Router.router(vertx);
            // Définition de la route
            router.get("/shutdown").handler(this::shutdown);
            router.get("/cb").handler(this::getCB);
            router.get("/").handler(this::getIndex);


            // Lancement du serveur
            /** Les trois lignes ci-dessous fonctionnent
             vertx.createHttpServer(httpOpts)
             .requestHandler(router)
             .listen(listenPort, bindAddress);
             **/

            server = vertx.createHttpServer(httpOpts);
            server.requestHandler(router);
            server.listen(listenPort, bindAddress);
        } catch (Exception e) {
            System.err.println("Error in start() Method : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Quand le verticle s'arrête
    @Override
    public void stop () throws Exception {
        System.out.println("Stop Vertx server by XL");
        System.exit(0);
        //server.close();
    }

/*    @Override
    public void stop () {
        for(String deploymentId : vertx.deploymentIDs()) {
            vertx.undeploy(deploymentId);
        }
    }*/

    private void beforeStopJVM () {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("just before stop jvm");
            vertx.close();
        }));
    }

    private void getIndex (RoutingContext routingContext) {
        System.out.println("Dans getIndex...");
        // Création et remplissage de la réponse
        String response = "<H1>Salut Marcel !</H1>";
        // Envoi de la réponse
        routingContext.response().setStatusCode(200)
                      // .putHeader("content-type", "application/javascript")
                      .end(response);
    }

    private void shutdown (RoutingContext routingContext) {
        System.out.println("shutdown vertex server...");
        try {
            stop();
        } catch (Exception e) {
            System.err.println("Error during server stopping : " + e.getMessage());
        }
    }

    private void getCB (RoutingContext routingContext) {
        try {
            /**
             CREATE TABLE MC001_VERSEMENT
             (
             LOTCODE             VARCHAR(64) PRIMARY KEY,
             DOCPATH             VARCHAR(64),
             LOTIDENT            BIGINT,
             REQIDENT            BIGINT,
             REQLASTSTATUS       VARCHAR(64)
             );
             **/
            System.out.println("*****************************************");
            System.out.println("Dans getCB...");

            String lotCode = routingContext.request().getParam("lotCode");
            lotCode = URLEncoder.encode(lotCode, StandardCharsets.UTF_8.toString());
            String requestStatus = requestStatusTable[Integer.parseInt(routingContext.request().getParam("status"))];
            long   reqID         = Long.parseLong(routingContext.request().getParam("reqId"));
            long   lotId         = Long.parseLong(routingContext.request().getParam("lotId"));
            System.out.println("lotCode......" + lotCode);
            System.out.println("status......." + requestStatus);
            System.out.println("reqId........" + reqID);
            System.out.println("lotId........" + lotId);
            System.out.println("*****************************************");
            // UPDATE MC001_VERSEMENT SET
            // LOTIDENT = routingContext.request().getParam("lotId"),
            // REQIDENT = routingContext.request().getParam("reqId"),
            // REQLASTSTATUS = routingContext.request().getParam("status")
            //  WHERE LOTCODE = routingContext.request().getParam("lotCode");

            // Création et remplissage de la réponse
            String response = "<H1>Salut Arcsys !</H1>";
            //        routingContext.request().
            // Envoi de la réponse
            routingContext.response().setStatusCode(200)
                          // .putHeader("content-type", "application/javascript")
                          .end(response);
        } catch (Exception e) {
            System.err.println("Error during server getCB method : " + e.getMessage());
            routingContext.response().setStatusCode(500);
        }
    }
}
