package com.example.starter;

import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main (String[] args) {
        System.out.println("Début du programme");
        try {
            ReadProperties.read("conf.param");
            System.out.println("initialized vertex server");
            final Vertx vertx = Vertx.vertx();
            vertx.deployVerticle(new Server());
        } catch (Exception e) {
            System.err.println("error in constructor Main() : " + e);
        }
        System.out.println("Fin du programme");
    }
}
