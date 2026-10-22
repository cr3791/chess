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

    public AuthData registerUser(UserData userData) throws AlreadyTakenException {
        if (userDataAccess.getUser(userData) == null){
            userDataAccess.addUser(userData);
            return authDataAccess.addAuthData(userData.username());
        } else {
            throw new AlreadyTakenException("This username is already taken");
        }
    }

    public AuthDataAccess getAuthDataAccess() {
        return authDataAccess;
    }

    public GameDataAccess getGameDataAccess() {
        return gameDataAccess;
    }

    public UserDataAccess getUserDataAccess() {
        return userDataAccess;
    }
}
