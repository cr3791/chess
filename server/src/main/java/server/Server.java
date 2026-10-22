package server;

import com.google.gson.Gson;
import dataaccess.*;
import io.javalin.*;
import io.javalin.http.Context;
import service.ChessService;

public class Server {
    private final ChessService service;
    private final Javalin javalin;

    public Server(ChessService service) {
        this.service = service;
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        /*
        .clear("/db", this::clearData);
        .register("/user", this::registerUser);
        .login("/session", this::logIn);
        .logout("/session", this::logOut);
        .listgames("/game", this::listGames);
        .creategame("/game", this::createGame);
        .joingame("/game", this::joinGame);
         */
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }


    private void clearData(Context ctx){

    }
    /*
    private void registerUser(Context ctx){
        throw new NotImplementedException;
    }

    private void logIn(Context ctx){
        throw new NotImplementedException;
    }

    private void logOut(Context ctx){
        throw new NotImplementedException;
    }

    private void listGames(Context ctx){
        throw new NotImplementedException;
    }

    private void createGame(Context ctx){
        throw new NotImplementedException;
    }

    private void registerUser(Context ctx){
        throw new NotImplementedException;
    }
     */
}
