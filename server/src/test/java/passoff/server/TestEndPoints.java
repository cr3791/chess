package passoff.server;

import dataaccess.*;
import org.junit.jupiter.api.*;
import model.*;
import service.ChessService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestEndPoints {
    private static ChessService service;

    @BeforeAll
    public static void setUp(){
        service = new ChessService(new MemoryUserDataAccess(), new MemoryGameDataAccess(), new MemoryAuthDataAccess());
    }

    @Test
    public void testClearSuccess(){

    }

    @Test
    public void testRegisterNewUserSuccess(){
        try {
            UserData newUser = new UserData("user", "pass", "email");
            UserData newUser2 = new UserData("user2", "pass2", "email2");
            AuthData authData1 = service.registerUser(newUser);
            AuthData authData2 = service.registerUser(newUser2);
            UserDataAccess userDAO = service.getUserDataAccess();
            assertEquals(userDAO.getUser(newUser),newUser);
            assertEquals(userDAO.getUser(newUser2),newUser2);
        } catch (AlreadyTakenException ex){
            fail();
        }
    }

    @Test
    public void testRegisterExistingFailure(){
        try {
            UserData newUser = new UserData("user", "pass", "email");
            AuthData authData1 = service.registerUser(newUser);
            AuthData authData2 = service.registerUser(newUser);
            fail();

        } catch (AlreadyTakenException ex){
            testClearSuccess();
        }
    }

    @Test
    public void testlogInNewUserSuccess(){
        try {
            UserData newUser1 = new UserData("user", "pass", "email");
            UserData newUser2 = new UserData("user2", "pass2", "email2");
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
            UserData newUser1 = new UserData("user", "pass", "email");
            UserData newUser2 = new UserData("user2", "pass2", "email2");
            UserData newUser3 = new UserData(null, "pass2", null);
            AuthData authData1 = service.registerUser(newUser1);
            AuthData authData2 = service.registerUser(newUser2);

            service.logOut(authData1.authToken());
            service.logOut(authData2.authToken());

            authData1 = service.logIn(newUser3);

            fail();

        } catch (AlreadyTakenException | UnauthorizedUserException ex){
            fail();
        } catch (BadRequestException ex){
            testClearSuccess();
        }
    }

    @Test
    public void testlogInUnauthorizedFailure(){
        try {
            UserData newUser1 = new UserData("user", "pass", "email");
            UserData newUser2 = new UserData("user2", "pass2", "email2");
            UserData newUser3 = new UserData("user2", "pass", null);

            AuthData authData1 = service.registerUser(newUser1);
            AuthData authData2 = service.registerUser(newUser2);

            service.logOut(authData1.authToken());
            service.logOut(authData2.authToken());

            authData1 = service.logIn(newUser3);

            fail();

        } catch (BadRequestException | AlreadyTakenException ex){
            fail();
        } catch (UnauthorizedUserException ex){
            testClearSuccess();
        }
    }


    @Test
    public void testlogOutNewUserSuccess(){
        try {
            UserData newUser = new UserData("user", "pass", "email");
            UserData newUser2 = new UserData("user2", "pass2", "email2");
            AuthData authData1 = service.registerUser(newUser);
            AuthData authData2 = service.registerUser(newUser2);
            service.logOut(authData1.authToken());
            service.logOut(authData2.authToken());

            AuthDataAccess authDataAccess = service.getAuthDataAccess();
            UserDataAccess userDataAccess = service.getUserDataAccess();

            assertEquals(new ArrayList<AuthData>(), authDataAccess.getAuthDataList());
            assertEquals(2, userDataAccess.getUserDataList().size());

        } catch (AlreadyTakenException | UnauthorizedUserException ex){
            fail();
        }
    }

    @Test
    public void testlogOutFailure(){
        try {
            UserData newUser = new UserData("user", "pass", "email");
            UserData newUser2 = new UserData("user2", "pass2", "email2");
            AuthData authData1 = service.registerUser(newUser);
            AuthData authData2 = service.registerUser(newUser2);
            service.logOut("happy");
            service.logOut("goofy");

            fail();

        } catch (AlreadyTakenException ex){
            fail();
        } catch (UnauthorizedUserException ex){
            testClearSuccess();
        }
    }
}
