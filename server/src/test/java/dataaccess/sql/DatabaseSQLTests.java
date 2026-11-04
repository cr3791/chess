package dataaccess.sql;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ChessService;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class DatabaseSQLTests {

    private static MySQLUserDataAccess userDAO;
    private static MySQLAuthDataAccess authDAO;
    private static MySQLGameDataAccess gameDAO;
    UserData newUser1 = new UserData("user", "pass", "email");
    UserData newUser2 = new UserData("user2", "pass2", "email2");

    public DatabaseSQLTests() throws DataAccessException{
        userDAO = new MySQLUserDataAccess();
        authDAO = new MySQLAuthDataAccess();
        gameDAO = new MySQLGameDataAccess();
    }

    @BeforeEach
    public void setUp(){
        try {
            authDAO.clearAuthData();
            userDAO.clearUserData();
            gameDAO.clearGameData();
        } catch (DataAccessException ex){
            throw new RuntimeException("Couldn't access database from setUP");
        }
    }

    @Test
    public void testClearUserDataSuccess(){
        try{
            userDAO.addUser(newUser1);
            userDAO.addUser(newUser2);

            userDAO.clearUserData();

            assertEquals(new ArrayList<UserData>(), userDAO.getUserDataList());
        } catch (DataAccessException ex){
            fail();
        }
    }


    @Test
    public void testAddUserDataSuccess(){
        try{
            userDAO.addUser(newUser1);

            ArrayList<UserData> userDataList = userDAO.getUserDataList();

            assertEquals(1, userDataList.size());
        } catch (DataAccessException ex){
            fail();
        }
    }

    @Test
    public void testAddUserDataFailure(){
        try{
            userDAO.addUser(newUser1);
            userDAO.addUser(newUser1);
            fail();
        } catch (DataAccessException ex){
            assert(true);
        }
    }

    @Test
    public void testGetUserDataSuccess(){
        try{
            userDAO.addUser(newUser1);
            UserData user = userDAO.getUser(newUser1);
            assertNotNull(user);
        } catch (DataAccessException ex){
            fail();
        }
    }

    @Test
    public void testGetUserDataFailure(){
        try{
            userDAO.addUser(newUser1);
            UserData user = userDAO.getUser(newUser2);
            assertNull(user);
        } catch (DataAccessException ex){
            fail();
        }
    }

    @Test
    public void testGetUserDataListSuccess(){
        try{
            userDAO.addUser(newUser1);
            userDAO.addUser(newUser2);
            ArrayList<UserData> userDataList = userDAO.getUserDataList();
            assertEquals(2, userDataList.size());
        } catch (DataAccessException ex){
            fail();
        }
    }

    @Test
    public void testGetUserDataListEmpty(){
        try{
            userDAO.addUser(newUser1);
            userDAO.addUser(newUser2);
            userDAO.clearUserData();
            ArrayList<UserData> userDataList = userDAO.getUserDataList();
            assertEquals(0, userDataList.size());
        } catch (DataAccessException ex){
            fail();
        }
    }

    @Test
    public void testClearGameDataSuccess(){
        try{
            gameDAO.createGame("ladeeda");
            gameDAO.createGame("woot woot");

            gameDAO.clearGameData();

            assertEquals(0, gameDAO.getGameList().size());
        } catch (DataAccessException ex){
            fail();
        }
    }

    @Test
    public void testCreateGameSuccess(){
        try{
            gameDAO.createGame("This");
            gameDAO.createGame("is");
            gameDAO.createGame("a");
            gameDAO.createGame("game");
            gameDAO.createGame("test");

            assertEquals(5, gameDAO.getGameList().size());
        } catch (DataAccessException ex){
            fail();
        }
    }

    @Test
    public void testGetGameListSuccess(){
        try{
            gameDAO.createGame("is");
            gameDAO.createGame("a");
            gameDAO.createGame("game");
            gameDAO.createGame("test");

            assertEquals(4, gameDAO.getGameList().size());
        } catch (DataAccessException ex){
            fail();
        }
    }

    @Test
    public void testGetGameListEmpty(){
        try{
            gameDAO.createGame("is");
            gameDAO.createGame("a");
            gameDAO.createGame("game");
            gameDAO.createGame("test");

            gameDAO.clearGameData();

            assertEquals(0, gameDAO.getGameList().size());
        } catch (DataAccessException ex){
            fail();
        }
    }

    @Test
    public void testGetGameSuccess(){
        try{
            gameDAO.createGame("wonderful");
            gameDAO.createGame("they");
            gameDAO.createGame("call");
            gameDAO.createGame("me");

            for(GameData game : gameDAO.getGameList()){
                int gameID = game.gameID();
                assertNotNull(gameDAO.getGame(gameID));
            }
        } catch (DataAccessException ex){
            fail();
        }
    }

    @Test
    public void testGetGameFailure(){
        try{
            gameDAO.createGame("wonderful");
            gameDAO.createGame("they");
            gameDAO.createGame("call");
            gameDAO.createGame("me");

            assertNull(gameDAO.getGame(1));

        } catch (DataAccessException ex){
            fail();
        }
    }

    @Test
    public void testAddWhitePlayerSuccess(){
        try{
            gameDAO.createGame("red queen");
            gameDAO.createGame("white queen");

            for(GameData game : gameDAO.getGameList()){
                int gameID = game.gameID();
                gameDAO.addWhitePlayer("cards", gameID);
                assertNotNull(gameDAO.getGame(gameID).whiteUsername());
            }
        } catch (DataAccessException ex){
            fail();
        }
    }

    @Test
    public void testAddWhitePlayerFail(){
        try{
            gameDAO.createGame("red queen");
            gameDAO.createGame("white queen");

            gameDAO.addWhitePlayer("card", 2);
            for(GameData game : gameDAO.getGameList()){
                int gameID = game.gameID();
                assertNull(gameDAO.getGame(gameID).whiteUsername());
            }

        } catch (DataAccessException ex){
            fail();
        }
    }

    @Test
    public void testAddBlackPlayerSuccess(){
        try{
            gameDAO.createGame("red queen");
            gameDAO.createGame("white queen");

            for(GameData game : gameDAO.getGameList()){
                int gameID = game.gameID();
                gameDAO.addBlackPlayer("cards", gameID);
                assertNotNull(gameDAO.getGame(gameID).blackUsername());
            }
        } catch (DataAccessException ex){
            fail();
        }
    }

    @Test
    public void testAddBlackPlayerFail(){
        try{
            gameDAO.createGame("red queen");
            gameDAO.createGame("white queen");

            gameDAO.addBlackPlayer("card", 2);
            for(GameData game : gameDAO.getGameList()){
                int gameID = game.gameID();
                assertNull(gameDAO.getGame(gameID).blackUsername());
            }

        } catch (DataAccessException ex){
            fail();
        }
    }

    @Test
    public void testAuthDataClearSuccess(){
        try{
            authDAO.addAuthData("yeet");
            authDAO.addAuthData("skrrt");

            authDAO.clearAuthData();

            assertEquals(0,authDAO.getAuthDataList().size());
        } catch (DataAccessException ex){
            fail();
        }
    }

    @Test
    public void testAddAuthDataSuccess(){
        try{
            authDAO.addAuthData("yeet");
            authDAO.addAuthData("skrrt");

            assertEquals(2,authDAO.getAuthDataList().size());
        } catch (DataAccessException ex){
            fail();
        }
    }

    @Test
    public void testGetAuthDataSuccess(){
        try{
            authDAO.addAuthData("yeet");
            authDAO.addAuthData("skrrt");

            for(AuthData auth : authDAO.getAuthDataList()){
                String authToken = auth.authToken();
                assertNotNull(authDAO.getAuthData(authToken));
            }
        } catch (DataAccessException ex){
            fail();
        }
    }

    @Test
    public void testGetAuthDataFailure(){
        try{
            authDAO.addAuthData("yeet");
            authDAO.addAuthData("skrrt");
            assertNull(authDAO.getAuthData("1010101"));

        } catch (DataAccessException ex){
            fail();
        }
    }

    @Test
    public void testRemoveAuthDataSuccess(){
        try{
            authDAO.addAuthData("yeet");
            authDAO.addAuthData("skrrt");
            for(AuthData auth : authDAO.getAuthDataList()){
                if(auth.username().equals("yeet")) {
                    authDAO.removeAuthData(auth);
                }
            }
            assertEquals(1,authDAO.getAuthDataList().size());
        } catch (DataAccessException ex){
            fail();
        }
    }

    @Test
    public void testRemoveAuthDataFailure(){
        try{
            authDAO.addAuthData("yeet");
            authDAO.addAuthData("skrrt");

            authDAO.removeAuthData(new AuthData("skrrt", "lskdfgalsdkhf"));
            assertEquals(2,authDAO.getAuthDataList().size());
        } catch (DataAccessException ex){
            fail();
        }
    }

    @Test
    public void testGetAuthDataListSuccess(){
        try{
            authDAO.addAuthData("yeet");
            authDAO.addAuthData("skrrt");
            authDAO.addAuthData("whoop");
            authDAO.addAuthData("skippee");

            assertEquals(4,authDAO.getAuthDataList().size());
        } catch (DataAccessException ex){
            fail();
        }
    }

    @Test
    public void testGetAuthDataListEmpty(){
        try{
            authDAO.addAuthData("yeet");
            authDAO.addAuthData("skrrt");
            authDAO.addAuthData("whoop");
            authDAO.addAuthData("skippee");
            authDAO.clearAuthData();

            assertEquals(0,authDAO.getAuthDataList().size());
        } catch (DataAccessException ex){
            fail();
        }
    }
}
