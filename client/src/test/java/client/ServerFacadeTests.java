package client;

import dataaccess.sql.MySQLAuthDataAccess;
import dataaccess.sql.MySQLGameDataAccess;
import dataaccess.sql.MySQLUserDataAccess;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import service.ChessService;
import ui.ServerFacade;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    private static ChessService service;

    @BeforeAll
    public static void init() {
        server = new Server();
        facade = new ServerFacade();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void setUp() throws Exception {
        facade.clear();
        try {
            service = new ChessService(new MySQLUserDataAccess(), new MySQLGameDataAccess(), new MySQLAuthDataAccess());
        } catch (Exception ex){
            throw new Exception("Had a data access issue");
        }
    }

    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void registerSuccess(){
        ArrayList<UserData> testList = new ArrayList<>();
        testList.add(new UserData("currently", "testing","yay"));
        testList.add(new UserData("one", "more","test"));
        testList.add(new UserData("keep","testing","things"));

        try {
            for(UserData user : testList){
                facade.register(user);
            }

            ArrayList<UserData> userList = service.getUserDataAccess().getUserDataList();
            assertEquals(userList.size(),testList.size());
        } catch (Exception ex){
            fail();
        }
    }

    @Test
    public void registerExistingUsernameFail(){
        ArrayList<UserData> testList = new ArrayList<>();
        testList.add(new UserData("currently", "testing","yay"));
        testList.add(new UserData("one", "more","test"));
        testList.add(new UserData("keep","testing","things"));

        try {
            for(UserData user : testList){
                facade.register(user);
            }

            facade.register(new UserData("one","asdfasdfasd","email"));
            fail();
        } catch (Exception ex){
            assert(true);
        }
    }

    @Test
    public void loginExistingUsernameSuccess(){
        ArrayList<UserData> testList = new ArrayList<>();
        testList.add(new UserData("currently", "testing","yay"));
        testList.add(new UserData("one", "more","test"));
        testList.add(new UserData("keep","testing","things"));

        try {
            for(UserData user : testList){
                facade.register(user);
            }
            String username = facade.logIn(new UserData("currently", "testing",null));
            assertNotNull(username);
            assertEquals("currently",username);

        } catch (Exception ex){
            fail();
        }
    }

    @Test
    public void loginWrongPasswordFail(){
        ArrayList<UserData> testList = new ArrayList<>();
        testList.add(new UserData("currently", "testing","yay"));
        testList.add(new UserData("one", "more","test"));
        testList.add(new UserData("keep","testing","things"));

        try {
            for(UserData user : testList){
                facade.register(user);
            }
            String username = facade.logIn(new UserData("currently", "yay",null));
            fail();

        } catch (Exception ex){
            assert(true);
        }
    }

    @Test
    public void logoutSuccess(){
        UserData user1 = new UserData("currently", "testing","yay");

        try {
            facade.register(user1);
            facade.logIn(user1);
            facade.logOut();
            try{
                facade.createGame("yayay");
                fail();
            } catch (Exception ex){
                assert(true);
            }
        } catch (Exception ex){
            fail();
        }
    }

    @Test
    public void logoutNoUserFail(){
        try {
            facade.logOut();
            fail();
        } catch (Exception ex){
            assert(true);
        }
    }

    @Test
    public void createGameSuccess(){
        UserData user1 = new UserData("currently", "testing","yay");

        try {
            facade.register(user1);
            try{
                int gameID = facade.createGame("yayay");
                assertNotNull(service.getGameDataAccess().getGame(gameID));

            } catch (Exception ex){
                fail("create game failed");
            }
        } catch (Exception ex){
            fail("set up failed");
        }
    }

    @Test
    public void createDuplicateGameFail(){
        UserData user1 = new UserData("currently", "testing","yay");

        try {
            facade.register(user1);
            facade.logIn(user1);
            try{
                facade.createGame("yayay");
                facade.createGame("yayay");
                fail("allowed duplicate names");
            } catch (Exception ex){
                assert(true);
            }
        } catch (Exception ex){
            fail("set up failed");
        }
    }

    @Test
    public void joinGameAsWhite(){
        UserData user1 = new UserData("currently", "testing","yay");

        try {
            facade.register(user1);
            int gameID = facade.createGame("yayay");
            try{
                GameData game = facade.playGame(gameID,"WHITE");
                assertNotNull(game);
                assertEquals("currently",game.whiteUsername());
            } catch (Exception ex){
                fail("join game failed");
            }
        } catch (Exception ex){
            fail("set up failed");
        }
    }

    @Test
    public void joinGameAsBlack(){
        UserData user1 = new UserData("currently", "testing","yay");

        try {
            facade.register(user1);
            int gameID = facade.createGame("yayay");
            try{
                GameData game = facade.playGame(gameID,"BLACK");
                assertNotNull(game);
                assertEquals("currently",game.blackUsername());
            } catch (Exception ex){
                fail("join game failed");
            }
        } catch (Exception ex){
            fail("set up failed");
        }
    }

    @Test
    public void joinBadGameIDFail() {
        UserData user1 = new UserData("currently", "testing", "yay");

        try {
            facade.register(user1);
            facade.createGame("yayay");
            try {
                GameData game = facade.playGame(-4, "BLACK");
                fail();
            } catch (Exception ex) {
                assert (true);
            }
        } catch (Exception ex) {
            fail("set up failed");
        }
    }

    @Test
    public void observeGameSuccess() {
        UserData user1 = new UserData("currently", "testing", "yay");

        try {
            facade.register(user1);
            int gameID = facade.createGame("yayay");
            try {
                GameData game = facade.observeGame(gameID);
                assertNotNull(game);
            } catch (Exception ex){
                fail("observe game failed");
            }
        } catch (Exception ex) {
            fail("set up failed");
        }
    }

    @Test
    public void observeBadGameIDFail() {
        UserData user1 = new UserData("currently", "testing", "yay");

        try {
            facade.register(user1);
            int gameID = facade.createGame("yayay");
            try {
                GameData game = facade.observeGame(33);
                fail();
            } catch (Exception ex){
                assert(true);
            }
        } catch (Exception ex) {
            fail("set up failed");
        }
    }

    @Test
    public void listGameSuccess(){
        UserData user1 = new UserData("currently", "testing", "yay");
        try {
            facade.register(user1);
            ArrayList<Integer> gameIDs = new ArrayList<>();
            gameIDs.add(facade.createGame("yayay"));
            gameIDs.add(facade.createGame("nope"));
            gameIDs.add(facade.createGame("lol"));
            gameIDs.add(facade.createGame("haha"));
            try {
                ArrayList<GameData> games = facade.getGameList();
                for(GameData game : games){
                    if(!gameIDs.contains(game.gameID())){
                        fail();
                    }
                }
            } catch (Exception ex){
                fail("game list failed");
            }
        } catch (Exception ex) {
            fail("set up failed");
        }
    }
}
