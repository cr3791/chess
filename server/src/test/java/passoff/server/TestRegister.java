package passoff.server;

import dataaccess.AlreadyTakenException;
import dataaccess.MemoryAuthDataAccess;
import dataaccess.MemoryGameDataAccess;
import dataaccess.MemoryUserDataAccess;
import org.junit.jupiter.api.*;
import model.*;
import service.ChessService;
import static org.junit.jupiter.api.Assertions.*;

public class TestRegister {
    private static ChessService service;
    private static UserData existingUser;
    private static UserData newUser;

    @BeforeAll
    public static void setUp(){
        service = new ChessService(new MemoryUserDataAccess(), new MemoryGameDataAccess(), new MemoryAuthDataAccess());
        existingUser = new UserData("ExistingUser", "existingUserPassword", "eu@mail.com");
        newUser = new UserData("NewUser", "newUserPassword", "nu@mail.com");
    }

    @Test
    @Order(1)
    public void registerNewUserSuccess() throws AlreadyTakenException{
        AuthData authData = service.registerUser(existingUser);
        assertEquals("ExistingUser", authData.username());
    }

    @Test
    @Order(1)
    public void registerNewUserFailure() throws AlreadyTakenException {
        AuthData authData1 = service.registerUser(existingUser);


    }
}
