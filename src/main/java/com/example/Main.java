package com.example;

import io.vertx.core.Vertx;

import java.util.Observer;

public class Main {
    
    private static Vertx              vertx;
    public static  ObservableShutdown observableShutdown = new ObservableShutdown();
    public static  ObserverShutdown   observerShutdown   = new ObserverShutdown();
    
    public static void main (String[] args) throws InterruptedException {
        vertx = Vertx.vertx();
        vertx.deployVerticle(new Server());
        vertx.deployVerticle(new Server());
        vertx.deployVerticle(new Server());
        vertx.deployVerticle(new Server());
        vertx.deployVerticle(new Server());
        vertx.deployVerticle(new Server());
        vertx.deployVerticle(new Server());
        vertx.deployVerticle(new Server());
        
        Thread.sleep(500);
        
        System.out.println(vertx.deploymentIDs());
        
        observerShutdown.observe(observableShutdown);
    }
    
    public static void closeVertx () {
        vertx.close();
    }
}
