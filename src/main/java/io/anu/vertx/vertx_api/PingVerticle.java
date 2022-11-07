package io.anu.vertx.vertx_api;
import java.util.HashSet;
import java.util.Set;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import vertxentity.Signup;
import vertxservice.SignupService;

public class PingVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> fut) {
        Router router = Router.router(vertx); // <1>
        // CORS support
        Set<String> allowHeaders = new HashSet<>();
        allowHeaders.add("x-requested-with");
        allowHeaders.add("Access-Control-Allow-Origin");
        allowHeaders.add("origin");
        allowHeaders.add("Content-Type");
        allowHeaders.add("accept");
        Set<HttpMethod> allowMethods = new HashSet<>();
        allowMethods.add(HttpMethod.GET);
        allowMethods.add(HttpMethod.POST);



        router.route().handler(CorsHandler.create("*") // <2>
                .allowedHeaders(allowHeaders)
                .allowedMethods(allowMethods));
        router.route().handler(BodyHandler.create()); // <3>

        // routes


        router.post("/Signup").handler(this::save);
        router.get("/Signup").handler(this::getSignup);
        router.get("/signup/:token").handler(this::getToken);


        router.post("/Login").handler(this::saved);



        router.route().handler(CookieHandler.create());
        router.route().handler(BodyHandler.create());
        router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));





        vertx.createHttpServer() // <4>
                .requestHandler(router::accept)
                .listen(8080, "0.0.0.0", result -> {
                    if (result.succeeded())
                        fut.complete();
                    else
                        fut.fail(result.cause());
                });
    }








    SignupService signupService = new SignupService();




















/*postcall*/



    private void save(RoutingContext context) {
        signupService. save(context,Json.decodeValue(context.getBodyAsString(), Signup.class), ar -> {
            if (ar.succeeded()) {
                sendSuccess(context.response());
            } else {
                sendError(ar.cause().getMessage(), context.response());
            }
        });
    }







/*getcall*/



    private void saved(RoutingContext context) {
        signupService.login(context,Json.decodeValue(context.getBodyAsString(), Signup.class), ar -> {
            if (ar.succeeded()) {
                sendSuccess(context.response());
            } else {
                sendError(ar.cause().getMessage(), context.response());
            }
        });
    }



/*postcall*/

    private void getSignup(RoutingContext context) {
        signupService.list(ar -> {
            if (ar.succeeded()) {
                sendSuccess(Json.
                		encodePrettily(ar.result()), context.response());
            } else {
                sendError(ar.cause().getMessage(), context.response());
            }
        });
    }





    private void getToken(RoutingContext context) {
    	 signupService. gettoken(context, context.request().getHeader("Authorization"),context.request().getParam("token"), ar -> {
    		 
            if (ar.succeeded()) {
                if (ar.result() != null){
                    sendSuccess(Json.encodePrettily(ar.result()), context.response());
                } else {
                    sendSuccess(context.response());
                }
            } else {
                sendError(ar.cause().getMessage(), context.response());
            }
        });
    }








    private void sendError(String errorMessage, HttpServerResponse response) {
        JsonObject jo = new JsonObject();
        jo.put("errorMessage", errorMessage);

        response
                .setStatusCode(500)
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(jo));
    }

    private void sendSuccess(HttpServerResponse response) {
        response
                .setStatusCode(200)
                .putHeader("content-type", "application/json; charset=utf-8")
                .end();
    }

    private void sendSuccess(String responseBody, HttpServerResponse response) {
        response
                .setStatusCode(200)
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(responseBody);
    }
}
