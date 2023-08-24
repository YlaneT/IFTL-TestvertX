package com.example;

import io.vertx.core.Vertx;

public class Main {
    
    private static Vertx vertx;
    
    public static void main (String[] args) {
        vertx = Vertx.vertx();
        vertx.deployVerticle(new Server());
        
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // Le déploiement des verticle est un processus asynchrone.
        // Si l'on veut voir les id, il faut laisser du temps à la machine.
        System.out.println(vertx.deploymentIDs());
    }
    
    public static void closeVertx () {
        vertx.close();
    }
}
