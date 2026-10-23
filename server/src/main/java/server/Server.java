package server;

import com.google.gson.Gson;
import dataaccess.*;
import model.*;
import io.javalin.*;
import io.javalin.http.Context;
import request.JoinGameRequest;
import service.ChessService;

import java.util.ArrayList;
import java.util.Map;

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
                .get("/game", this::listGames)
                .post("/game", this::createGame)
                .put("/game", this::joinGame);
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
            String error = new Gson().toJson(Map.of("message", "Error: already taken"));
            ctx.result(error);
        } catch (BadRequestException ex){
            ctx.status(400);
            String error = new Gson().toJson(Map.of("message", "Error: bad request"));
            ctx.result(error);
        }
    }

    private void logIn(Context ctx){
        try {
            UserData userData = new Gson().fromJson(ctx.body(), UserData.class);
            AuthData authData = service.logIn(userData);
            ctx.result(new Gson().toJson(authData));
        } catch (BadRequestException ex){
            ctx.status(400);
            String error = new Gson().toJson(Map.of("message", "Error: bad request"));
            ctx.result(error);
        } catch (UnauthorizedUserException ex){
            ctx.status(401);
            String error = new Gson().toJson(Map.of("message", "Error: unauthorized"));
            ctx.result(error);
        }
    }

    private void logOut(Context ctx){
        try {
            String authToken = ctx.header("authorization");
            service.logOut(authToken);
        } catch (UnauthorizedUserException ex){
            ctx.status(401);
            String error = new Gson().toJson(Map.of("message", "Error: unauthorized"));
            ctx.result(error);
        }
    }

    private void listGames(Context ctx){
        try {
            String authToken = ctx.header("authorization");
            ArrayList<GameData> games = service.listGames(authToken);
            ctx.result(new Gson().toJson(Map.of("games", games)));
        } catch (UnauthorizedUserException ex){
            ctx.status(401);
            String error = new Gson().toJson(Map.of("message", "Error: unauthorized"));
            ctx.result(error);
        }
    }

    private void createGame(Context ctx){
        try{
            String authToken = ctx.header("authorization");
            GameData gameData = new Gson().fromJson(ctx.body(), GameData.class);

            int gameID = service.createGame(authToken,gameData.gameName());
            ctx.result(new Gson().toJson(Map.of("gameID", gameID)));

        } catch (BadRequestException ex){
            ctx.status(400);
            String error = new Gson().toJson(Map.of("message", "Error: bad request"));
            ctx.result(error);
        } catch (UnauthorizedUserException ex){
            ctx.status(401);
            String error = new Gson().toJson(Map.of("message", "Error: unauthorized"));
            ctx.result(error);
        }
    }

    private void joinGame(Context ctx){
        try{
            String authToken = ctx.header("authorization");
            GameData gameData = new Gson().fromJson(ctx.body(), GameData.class);
            JoinGameRequest joinGame = new Gson().fromJson(ctx.body(), JoinGameRequest.class);

            service.joinGame(authToken, gameData, joinGame.playerColor());

        } catch (BadRequestException ex){
            ctx.status(400);
            String error = new Gson().toJson(Map.of("message", "Error: bad request"));
            ctx.result(error);
        } catch (UnauthorizedUserException ex){
            ctx.status(401);
            String error = new Gson().toJson(Map.of("message", "Error: unauthorized"));
            ctx.result(error);
        } catch (AlreadyTakenException ex){
            ctx.status(403);
            String error = new Gson().toJson(Map.of("message", "Error: already taken"));
            ctx.result(error);
        }
    }
}
