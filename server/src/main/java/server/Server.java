package server;

import dataaccess.NotImplementedException;
import io.javalin.*;
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
        .register("/user", this::??);
        .login("/session", this::??);
        .logout("/session", this::??);
        .listgames("/game", this::??);
        .creategame("/game", this::??);
        .joingame("/game", this::??);
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
        throw new NotImplementedException;
    }
}
