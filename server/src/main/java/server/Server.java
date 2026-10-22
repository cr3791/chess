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
                .post("/user", this::registerUser)
                .post("/session", this::logIn)
                .delete("/session", this::logOut)
                //add list games on this line
                .post("/game", this::createGame);
        /*
                .get("/game", this::listGames)
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

    private void clearData(Context ctx){
        service.clearData();
    }

    private void registerUser(Context ctx){
        try {
            UserData userData = new Gson().fromJson(ctx.body(), UserData.class);
            AuthData authData = service.registerUser(userData);
            ctx.result(new Gson().toJson(authData));
        } catch (AlreadyTakenException ex){
            ctx.status(403);
            ctx.result(new Gson().toJson("Error: username already taken"));
        }
    }

    private void logIn(Context ctx){
        try {
            UserData userData = new Gson().fromJson(ctx.body(), UserData.class);
            AuthData authData = service.logIn(userData);
            ctx.result(new Gson().toJson(authData));
        } catch (BadRequestException ex){
            ctx.status(400);
            ctx.result(new Gson().toJson("Error: bad request"));
        } catch (UnauthorizedUserException ex){
            ctx.status(401);
            ctx.result(new Gson().toJson("Error: unauthorized user"));
        }
    }

    private void logOut(Context ctx){
        String authToken = new Gson().fromJson(ctx.header("authorization"), String.class);
        service.logOut(authToken);
    }

    private void listGames(Context ctx){

    }

    private void createGame(Context ctx){

    }

    private void joinGame(Context ctx){

    }
}
