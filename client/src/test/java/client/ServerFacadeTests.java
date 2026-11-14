import dataaccess.UserDataAccess;
import dataaccess.sql.MySQLAuthDataAccess;
import dataaccess.sql.MySQLGameDataAccess;
import dataaccess.sql.MySQLUserDataAccess;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import service.ChessService;
import ui.ServerFacade;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    private static ChessService service;

    @BeforeAll
    public static void init() {
        server = new Server();
        facade = new ServerFacade();
        var port = server.run(8080);
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

}
