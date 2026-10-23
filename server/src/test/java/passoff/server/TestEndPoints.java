package passoff.server;

import chess.ChessGame;
import dataaccess.*;
import org.junit.jupiter.api.*;
import model.*;
import service.ChessService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestEndPoints {
    private static ChessService service;
    UserData newUser1 = new UserData("user", "pass", "email");
    UserData newUser2 = new UserData("user2", "pass2", "email2");

    @BeforeEach
    public void setUp(){
        service = new ChessService(new MemoryUserDataAccess(), new MemoryGameDataAccess(), new MemoryAuthDataAccess());
    }

    @Test
    public void testClearSuccess(){
        try {
            AuthData authData = service.registerUser(newUser1);

            service.createGame(authData.authToken(), "Party party");
            service.createGame(authData.authToken(), "new Game");

            service.clearData();

            assertEquals(new ArrayList<AuthData>(), service.getAuthDataAccess().getAuthDataList());
            assertEquals(new ArrayList<GameData>(), service.getGameDataAccess().getGameList());
            assertEquals(new ArrayList<UserData>(), service.getUserDataAccess().getUserDataList());

        } catch (AlreadyTakenException | BadRequestException | UnauthorizedUserException ex){
            fail();
        }
    }

    @Test
    public void testRegisterNewUserSuccess(){
        try {
            service.registerUser(newUser1);
            service.registerUser(newUser2);
            UserDataAccess userDAO = service.getUserDataAccess();
            assertEquals(userDAO.getUser(newUser1),newUser1);
            assertEquals(userDAO.getUser(newUser2),newUser2);
        } catch (AlreadyTakenException | BadRequestException ex){
            fail();
        }
    }

    @Test
    public void testRegisterExistingFailure(){
        try {
            service.registerUser(newUser1);
            service.registerUser(newUser1);
            fail();

        } catch (AlreadyTakenException ex){
            assertTrue(true);
        } catch (BadRequestException ex){
            fail();
        }
    }

    @Test
    public void testRegisterBadRequestFailure(){
        try {
            service.registerUser(newUser1);
            service.registerUser(new UserData(null, "yay", null));
            fail();

        } catch (AlreadyTakenException ex){
            fail();
        } catch (BadRequestException ex){
            assertTrue(true);
        }
    }

    @Test
    public void testlogInNewUserSuccess(){
        try {
            AuthData authData1 = service.registerUser(newUser1);
            AuthData authData2 = service.registerUser(newUser2);

            service.logOut(authData1.authToken());
            service.logOut(authData2.authToken());

            authData1 = service.logIn(newUser1);
            authData2 = service.logIn(newUser2);

            AuthDataAccess authDataAccess = service.getAuthDataAccess();
            UserDataAccess userDataAccess = service.getUserDataAccess();

            assertEquals(authData1, authDataAccess.getAuthData(authData1.authToken()));
            assertEquals(authData2, authDataAccess.getAuthData(authData2.authToken()));
            assertEquals(2, userDataAccess.getUserDataList().size());

        } catch (BadRequestException | AlreadyTakenException | UnauthorizedUserException ex){
            fail();
        }
    }

    @Test
    public void testlogInBadRequestFailure(){
        try {
            UserData newUser3 = new UserData(null, "pass2", null);
            AuthData authData1 = service.registerUser(newUser1);
            AuthData authData2 = service.registerUser(newUser2);

            service.logOut(authData1.authToken());
            service.logOut(authData2.authToken());

            service.logIn(newUser3);

            fail();

        } catch (AlreadyTakenException | UnauthorizedUserException ex){
            fail();
        } catch (BadRequestException ex){
            assertTrue(true);
        }
    }

    @Test
    public void testlogInUnauthorizedFailure(){
        try {
            UserData newUser3 = new UserData("user2", "pass", null);
            AuthData authData1 = service.registerUser(newUser1);
            AuthData authData2 = service.registerUser(newUser2);

            service.logOut(authData1.authToken());
            service.logOut(authData2.authToken());

            service.logIn(newUser3);

            fail();

        } catch (BadRequestException | AlreadyTakenException ex){
            fail();
        } catch (UnauthorizedUserException ex){
            assertTrue(true);
        }
    }


    @Test
    public void testlogOutNewUserSuccess(){
        try {
            AuthData authData1 = service.registerUser(newUser1);
            AuthData authData2 = service.registerUser(newUser2);
            service.logOut(authData1.authToken());
            service.logOut(authData2.authToken());

            AuthDataAccess authDataAccess = service.getAuthDataAccess();
            UserDataAccess userDataAccess = service.getUserDataAccess();

            assertEquals(new ArrayList<AuthData>(), authDataAccess.getAuthDataList());
            assertEquals(2, userDataAccess.getUserDataList().size());

        } catch (AlreadyTakenException | UnauthorizedUserException | BadRequestException ex){
            fail();
        }
    }

    @Test
    public void testlogOutFailure(){
        try {
            service.registerUser(newUser1);
            service.registerUser(newUser2);
            service.logOut("happy");
            service.logOut("goofy");

            fail();

        } catch (AlreadyTakenException | BadRequestException ex){
            fail();
        } catch (UnauthorizedUserException ex){
            assertTrue(true);
        }
    }

    @Test
    public void testCreateGamesSuccess(){
        try {
            AuthData authData = service.registerUser(newUser1);

            service.createGame(authData.authToken(), "Party party");
            service.createGame(authData.authToken(), "new Game");

            GameDataAccess gameDataAccess = service.getGameDataAccess();

            ArrayList<GameData> gameList = gameDataAccess.getGameList();

            assertEquals(2, gameList.size());

        } catch (BadRequestException | AlreadyTakenException | UnauthorizedUserException ex){
            fail();
        }
    }

    @Test
    public void testCreateGamesUnauthorizedUserFailure(){
        try {
            AuthData authData = service.registerUser(newUser1);

            service.createGame(authData.authToken(), "Party party");
            service.createGame("authData", "new Game");

            fail();

        } catch (BadRequestException | AlreadyTakenException ex){
            fail();
        } catch (UnauthorizedUserException ex){
            assertTrue(true);
        }
    }

    @Test
    public void testListGamesSuccess(){
        try{
            AuthData authData = service.registerUser(newUser1);

            service.createGame(authData.authToken(), "Party party");
            service.createGame(authData.authToken(), "Party party party");

            ArrayList<GameData> gameList = service.listGames(authData.authToken());

            assertEquals(2, gameList.size());

        } catch (BadRequestException | AlreadyTakenException | UnauthorizedUserException ex){
            fail();
        }
    }
    @Test
    public void testJoinGameWhiteSuccess(){
        try{
            AuthData authData = service.registerUser(newUser1);
            int gameID = service.createGame(authData.authToken(), "Party party");

            GameData gameData = new GameData(gameID, null, null, null, null);

            service.joinGame(authData.authToken(), gameData, "White");

            ArrayList<GameData> gameList = service.listGames(authData.authToken());

            for(GameData game : gameList){
                assertEquals(new GameData(gameID, newUser1.username(),null,"Party party",new ChessGame()), game);
            }
        } catch (BadRequestException | AlreadyTakenException | UnauthorizedUserException ex){
            fail();
        }
    }

    @Test
    public void testJoinGameBlackSuccess(){
        try{
            AuthData authData = service.registerUser(newUser1);
            int gameID = service.createGame(authData.authToken(), "Party party");

            GameData gameData = new GameData(gameID, null, null, null, null);

            service.joinGame(authData.authToken(), gameData, "BLACK");
            ArrayList<GameData> gameList = service.listGames(authData.authToken());

            for(GameData game : gameList){
                assertEquals(new GameData(gameID, null,newUser1.username(),"Party party",new ChessGame()), game);
            }
        } catch (BadRequestException | AlreadyTakenException | UnauthorizedUserException ex){
            fail();
        }
    }

    @Test
    public void testJoinGameBadRequestFailure(){
        try{
            AuthData authData = service.registerUser(newUser1);

            service.createGame(authData.authToken(), "Party party");
            int gameID = service.createGame(authData.authToken(), "Party party party");

            GameData gameData = new GameData(gameID, null, null, null, null);
            service.joinGame(null, gameData, "BLACK");
            fail();

        } catch (UnauthorizedUserException | AlreadyTakenException ex){
            fail();
        } catch (BadRequestException ex){
            assertTrue(true);
        }
    }

    @Test
    public void testJoinGameUnauthorizedUserFailure(){
        try{
            AuthData authData = service.registerUser(newUser1);

            service.createGame(authData.authToken(), "Party party");
            int gameID = service.createGame(authData.authToken(), "Party party party");

            GameData gameData = new GameData(gameID, null, null, null, null);
            service.joinGame("ladeeda", gameData, "BLACK");
            fail();

        } catch (BadRequestException | AlreadyTakenException ex){
            fail();
        } catch (UnauthorizedUserException ex){
            assertTrue(true);
        }
    }

    @Test
    public void testJoinGameAlreadyTakenFailure(){
        try{
            AuthData authData = service.registerUser(newUser1);
            int gameID = service.createGame(authData.authToken(), "Party party");

            GameData gameData = new GameData(gameID, null, null, null, null);

            service.joinGame(authData.authToken(), gameData, "BLACK");
            service.joinGame(authData.authToken(), gameData, "BLACK");
            fail();

        } catch (BadRequestException | UnauthorizedUserException ex){
            fail();
        } catch (AlreadyTakenException ex){
            assertTrue(true);
        }
    }
}
