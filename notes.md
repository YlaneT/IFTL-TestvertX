La méthode `deployVerticle()` est asynchrone. J'utilise un `Thread.sleep(500);` pour pouvoir voir les changements sur la liste des deploymentsID.

Pareil pour `vertx.undeploy()`.

Dans `undeploy()` on appelle `stop()`. Dans la méthode @Override `stop()` si on met

```java
for(String dID : vertx.deploymentIDs()) {
    vertx.undeploy(dID);
}
```
On se retrouve avec une null pointer exception

```java
août 23, 2023 4:58:48 PM io.netty.channel.AbstractChannelHandlerContext invokeExceptionCaught
WARNING: An exception 'java.lang.NullPointerException: Cannot invoke "io.vertx.core.http.impl.Http1xServerResponse.ended()" because "this.response" is null' [enable DEBUG level for full stacktrace] was thrown by a user handler's exceptionCaught() method while handling the following exception:
java.lang.NullPointerException: Cannot invoke "io.vertx.core.http.impl.Http1xServerResponse.ended()" because "this.response" is null
```

En essayant de mettre `vertx.close()` dans `stop()` on a le meme résultat

Si on externalise `vertx.close()` par une méthode dans le main et qu'on l'appelle dans le `shutdown()` : on a le même résultat. La méthode `close()` de Vertx supprime l'objet déployé (étendant AbstractVerticle) et la méthode ne peut donc pas se finir.

Si on appelle `vertx.close()` directement dans le main, le vertx se ferme en appelant les méthodes `close()` de chaque Verticle.

