package dataaccess;
import model.*;
import java.util.ArrayList;

public interface GameDataAccess {
    void clearGameData() throws DataAccessException;
    int createGame(String gameName) throws DataAccessException;
    ArrayList<GameData> getGameList() throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    void addWhitePlayer(String username, int gameID) throws DataAccessException;
    void addBlackPlayer(String username, int gameID) throws DataAccessException;
}
