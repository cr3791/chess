package service;
import dataaccess.*;
import model.*;

public class ChessService {
    private final UserDataAccess userDataAccess;
    private final GameDataAccess gameDataAccess;
    private final AuthDataAccess authDataAccess;

    public ChessService(UserDataAccess userDataAccess, GameDataAccess gameDataAccess, AuthDataAccess authDataAccess){
        this.userDataAccess = userDataAccess;
        this.gameDataAccess = gameDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public void clearData(){
        userDataAccess.clearUserData();
        gameDataAccess.clearGameData();
        authDataAccess.clearAuthData();
    }

    public AuthData registerUser(UserData userData){
        userData = userDataAccess.addUser(userData);
        AuthData authData = authDataAccess.addAuthData(userData.username());
        return authData;
    }

}
