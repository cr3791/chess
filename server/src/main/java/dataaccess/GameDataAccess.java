package dataaccess;
import model.*;
import java.util.ArrayList;

public interface GameDataAccess {
    void clearGameData() throws DataAccessException;
    int createGame(String gameName);
    ArrayList<GameData> getGameList();
    GameData getGame(int gameID);
    void addWhitePlayer(String username, int gameID);
    void addBlackPlayer(String username, int gameID);
}
