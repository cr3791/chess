package service;
import dataaccess.*;
import model.*;
import org.mindrot.jbcrypt.BCrypt;

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

    public void clearData() throws DataAccessException {
        try {
            userDataAccess.clearUserData();
            gameDataAccess.clearGameData();
            authDataAccess.clearAuthData();
        } catch (DataAccessException ex){
            throw new DataAccessException("unable to access database from clear");
        }
    }

    public AuthData registerUser(UserData userData) throws AlreadyTakenException, BadRequestException, DataAccessException {
        try {
            checkRegisterInputs(userData);
            if (userDataAccess.getUser(userData) == null) {
                userDataAccess.addUser(userData);
                return authDataAccess.addAuthData(userData.username());
            } else {
                throw new AlreadyTakenException("This username is already taken");
            }
        } catch (BadRequestException ex){
            throw new BadRequestException("");
        } catch (DataAccessException ex){
            throw new DataAccessException("unable to access database from Register User");
        }
    }

    public AuthData logIn(UserData userData) throws UnauthorizedUserException, BadRequestException, DataAccessException{
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
        } catch (DataAccessException ex){
            throw new DataAccessException("Can't access database from logIn");
        }
    }

    public void logOut(String authToken) throws UnauthorizedUserException, DataAccessException{
        try {
            AuthData authData = authDataAccess.getAuthData(authToken);
            if (authData != null) {
                authDataAccess.removeAuthData(authData);
            } else {
                throw new UnauthorizedUserException("");
            }
        } catch (DataAccessException ex){
            throw new DataAccessException("Could not access database from logOut");
        }
    }

    public ArrayList<GameData> listGames(String authToken) throws UnauthorizedUserException, DataAccessException {
        try {
            AuthData authData = authDataAccess.getAuthData(authToken);
            if (authData != null) {
                return gameDataAccess.getGameList();
            } else {
                throw new UnauthorizedUserException("");
            }
        } catch (DataAccessException ex){
            throw new DataAccessException("Could not access database from listGames");
        }
    }

    public int createGame(String authToken, String gameName) throws BadRequestException, UnauthorizedUserException, DataAccessException {
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
        } catch (DataAccessException ex){
            throw new DataAccessException("Unable to access Database at createGame");
        }
    }

    public void joinGame(String authToken,GameData game,String color)throws AlreadyTakenException,BadRequestException,UnauthorizedUserException,DataAccessException{
        try{
            checkJoinGameInputs(authToken, game, color);
            color = identifyPlayerColor(color);

            AuthData authData = authDataAccess.getAuthData(authToken);

            if(authData!=null){
                checkIfPlayerTaken(color, game);
                addNewPlayer(color, game.gameID(), authToken);
            } else {
                throw new UnauthorizedUserException("");
            }
        } catch (BadRequestException ex){
            throw new BadRequestException("");
        } catch (AlreadyTakenException ex){
            throw new AlreadyTakenException("");
        } catch (DataAccessException ex){
            throw new DataAccessException("Could not access the database from joinGame");
        }
    }

    private boolean checkPassword(String storedPassword, String passwordAttempt) throws UnauthorizedUserException {

        if(BCrypt.checkpw(passwordAttempt,storedPassword)){
            return true;
        } else {
            throw new UnauthorizedUserException("");
        }
    }

    private void checkRegisterInputs(UserData userData) throws BadRequestException{
        if(userData.username() == null || userData.password() == null || userData.email() == null){
            throw new BadRequestException("");
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
        try {
            GameData game = gameDataAccess.getGame(gameData.gameID());

            if (game == null) {
                throw new BadRequestException("Game doesn't exist");
            }

            if (playerColor.equals("white") && game.whiteUsername() != null) {
                throw new AlreadyTakenException("");
            } else if (playerColor.equals("black") && game.blackUsername() != null) {
                throw new AlreadyTakenException("");
            }
        } catch (DataAccessException ex){
            throw new RuntimeException("Couldn't access database at checkIfPlayerTaken");
        }
    }

    private void addNewPlayer(String playerColor, int gameID, String authToken){
        try {
            String username = authDataAccess.getAuthData(authToken).username();

            if (playerColor.equals("white")) {
                gameDataAccess.addWhitePlayer(username, gameID);
            } else if (playerColor.equals("black")) {
                gameDataAccess.addBlackPlayer(username, gameID);
            }
        } catch (DataAccessException ex){
            throw new RuntimeException("Couldn't access the database at addNewPlayer");
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
