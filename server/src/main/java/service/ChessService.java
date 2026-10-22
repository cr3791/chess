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
        userDataAccess.clear();
        gameDataAccess.clearGameData();
        authDataAccess.clear();
    }

}
