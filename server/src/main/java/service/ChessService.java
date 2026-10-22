package service;
import dataaccess.UserDataAccess;
import model.*;

public class ChessService {
    private final UserDataAccess userDataAccess;
    public ChessService(UserDataAccess userDataAccess){
        this.userDataAccess = userDataAccess;
    }

    public UserData getUser(UserData userData){
        return userDataAccess.getUser(userData);
    }
}
