package passoff.server;

import dataaccess.MemoryAuthDataAccess;
import dataaccess.MemoryGameDataAccess;
import dataaccess.MemoryUserDataAccess;
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

    }

    @Test
    public void testRegisterExistingFailure(){

    }
 /*
    @Test
    @Order(1)
    public void testClear(){
        try {
            service.registerUser(user1);
            service.registerUser(user2);
            service.registerUser(user3);
            service.registerUser(user4);
            service.registerUser(user5);
            service.registerUser(user6);

            //remove register user, and just shove users into user data when instantiating :)

            ArrayList<UserData> userDataList = service.getUserDataAccess().getUserDataList();
            assertNotNull(userDataList);

            service.clearData();

            userDataList = service.getUserDataAccess().getUserDataList();

            assertEquals(new ArrayList<UserData>(), userDataList);
        } catch (AlreadyTakenException e){
            //assert(register user failed, test can't continue)
        }
    }

    @Test
    @Order(3)
    public void registerNewUserSuccess() throws AlreadyTakenException{
        AuthData authData = service.registerUser(user1);
        assertEquals("ExistingUser", authData.username());
    }

    @Test
    @Order(4)
    public void registerNewUserFailure() throws AlreadyTakenException {
        AuthData authData1 = service.registerUser(user2);


    }

  */
}
