package service;
import dataaccess.*;
import model.*;

import java.util.ArrayList;

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

    public AuthData logIn(UserData userData) throws UnauthorizedUserException, BadRequestException{
        try {
            checkLogInInputs(userData);
            UserData storedUser = userDataAccess.getUser(userData);

            if (storedUser != null && checkPassword(storedUser.password(), userData.password())) {
                return authDataAccess.addAuthData(userData.username());
            }
            return null;
        } catch (UnauthorizedUserException ex){
            throw new UnauthorizedUserException("Unauthorized User");
        } catch (BadRequestException ex){
            throw new BadRequestException("Bad Request");
        }
    }

    public void logOut(String authToken) throws UnauthorizedUserException{
        AuthData authData = authDataAccess.getAuthData(authToken);

        if(authData!=null){
            authDataAccess.removeAuthData(authData);
        } else {
            throw new UnauthorizedUserException("");
        }
    }

    public ArrayList<GameData> listGames(String authToken) throws UnauthorizedUserException{
        AuthData authData = authDataAccess.getAuthData(authToken);

        if(authData!=null){
            return gameDataAccess.getGameList();
        } else {
            throw new UnauthorizedUserException("");
        }
    }

    private boolean checkPassword(String storedPassword, String passwordAttempt) throws UnauthorizedUserException {
        if(storedPassword.equals(passwordAttempt)){
            return true;
        } else {
            throw new UnauthorizedUserException("");
        }
    }

    private void checkLogInInputs(UserData userData) throws BadRequestException{
        if (userData.username()==null || userData.password()==null || userData.email()!=null){
            throw new BadRequestException("");
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
