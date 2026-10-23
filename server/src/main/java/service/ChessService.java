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
            } else if (storedUser == null || !checkPassword(storedUser.password(), userData.password())){
                throw new UnauthorizedUserException("Unauthorized User");
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

    public int createGame(String authToken, String gameName) throws BadRequestException, UnauthorizedUserException{
        try{
            checkCreateGameInputs(authToken, gameName);
            AuthData authData = authDataAccess.getAuthData(authToken);

            if(authData!=null){
                return gameDataAccess.createGame(gameName);
            } else {
                throw new UnauthorizedUserException("");
            }
        } catch (BadRequestException ex){
            throw new BadRequestException("");
        }
    }

    public void joinGame(String authToken, GameData gameData, String playerColor) throws AlreadyTakenException, BadRequestException, UnauthorizedUserException{
        try{
            checkJoinGameInputs(authToken, gameData, playerColor);
            playerColor = identifyPlayerColor(playerColor);

            AuthData authData = authDataAccess.getAuthData(authToken);

            if(authData!=null){
                checkIfPlayerTaken(playerColor, gameData);
                addNewPlayer(playerColor, gameData.gameID(), authToken);
            } else {
                throw new UnauthorizedUserException("");
            }
        } catch (BadRequestException ex){
            throw new BadRequestException("");
        } catch (AlreadyTakenException ex){
            throw new AlreadyTakenException("");
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
        if (userData.username()==null || userData.password()==null){
            throw new BadRequestException("");
        }
    }

    private void checkCreateGameInputs(String authToken, String gameName) throws BadRequestException{
        if(authToken==null || gameName == null){
            throw new BadRequestException("");
        }
    }

    private void checkJoinGameInputs(String authToken, GameData gameData, String playerColor) throws BadRequestException{
        if(authToken == null || gameData == null || playerColor == null){
            throw new BadRequestException("");
        }
    }

    private String identifyPlayerColor(String playerColor) throws BadRequestException{
        if(playerColor.equalsIgnoreCase("white")){
            return "white";
        } else if (playerColor.equalsIgnoreCase("black")){
            return "black";
        } else {
            throw new BadRequestException("");
        }
    }

    private void checkIfPlayerTaken(String playerColor, GameData gameData) throws AlreadyTakenException, BadRequestException {
        GameData game = gameDataAccess.getGame(gameData.gameID());

        if(game == null){
            throw new BadRequestException("Game doesn't exist");
        }

        if(playerColor.equals("white") && game.whiteUsername() != null){
            throw new AlreadyTakenException("");
        } else if (playerColor.equals("black") && game.blackUsername() != null){
            throw new AlreadyTakenException("");
        }
    }

    private void addNewPlayer(String playerColor, int gameID, String authToken){
        String username = authDataAccess.getAuthData(authToken).username();

        if(playerColor.equals("white")){
            gameDataAccess.addWhitePlayer(username, gameID);
        } else if (playerColor.equals("black")){
            gameDataAccess.addBlackPlayer(username, gameID);
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
