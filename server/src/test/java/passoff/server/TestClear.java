package passoff.server;

import dataaccess.AlreadyTakenException;
import dataaccess.MemoryAuthDataAccess;
import dataaccess.MemoryGameDataAccess;
import dataaccess.MemoryUserDataAccess;
import org.junit.jupiter.api.*;
import model.*;
import service.ChessService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestClear {
    private static ChessService service;
    private static UserData user1;
    private static UserData user2;
    private static UserData user3;
    private static UserData user4;
    private static UserData user5;
    private static UserData user6;

    @BeforeAll
    public static void setUp(){
        service = new ChessService(new MemoryUserDataAccess(), new MemoryGameDataAccess(), new MemoryAuthDataAccess());
        user1 = new UserData("user1", "user1p", "u1@mail.com");
        user2 = new UserData("user2", "user2p", "u2@mail.com");
        user3 = new UserData("user3", "user3p", "u3@mail.com");
        user4 = new UserData("user4", "user4p", "u4@mail.com");
        user5 = new UserData("user5", "user5p", "u5@mail.com");
        user6 = new UserData("user6", "user6p", "u6@mail.com");
    }

    @Test
    @Order(1)
    public void testClear() throws AlreadyTakenException{
        service.registerUser(user1);
        service.registerUser(user2);
        service.registerUser(user3);
        service.registerUser(user4);
        service.registerUser(user5);
        service.registerUser(user6);
        ArrayList<UserData> userDataList = service.getUserDataAccess().getUserDataList();
        assertNotNull(userDataList);

        service.clearData();

        userDataList = service.getUserDataAccess().getUserDataList();

        assertEquals(new ArrayList<UserData>(),userDataList);
    }
}
