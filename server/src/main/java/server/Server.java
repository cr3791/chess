package server;

import com.google.gson.Gson;
import dataaccess.*;
import model.*;
import io.javalin.*;
import io.javalin.http.Context;
import service.ChessService;

public class Server {
    private final ChessService service;
    private final Javalin javalin;

    public Server(){
        this(new ChessService(new MemoryUserDataAccess(), new MemoryGameDataAccess(), new MemoryAuthDataAccess()));
    }

    public Server(ChessService service) {
        this.service = service;

        // Register your endpoints and exception handlers here.
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .delete("/db", this::clearData)
                .post("/user", this::registerUser);
                /*
                .post("/session", this::logIn)
                .delete("/session", this::logOut)
                .get("/game", this::listGames)
                .post("/game", this::createGame)
                .put("/game", this::joinGame)
                .exception(DataAccessException.class, this::exceptionHandler) */
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    public void exceptionHandler(DataAccessException ex, Context ctx){

    }

    private void clearData(Context ctx){
        service.clearData();
    }

    private void registerUser(Context ctx) throws AlreadyTakenException{
        UserData userData = new Gson().fromJson(ctx.body(), UserData.class);
        AuthData authData = service.registerUser(userData);
        ctx.result(new Gson().toJson(authData));
    }

    private void logIn(Context ctx){

    }

    private void logOut(Context ctx){

    }

    private void listGames(Context ctx){

    }

    private void createGame(Context ctx){

    }

    private void joinGame(Context ctx){

    }
}
