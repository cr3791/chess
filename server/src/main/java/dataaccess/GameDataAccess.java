package dataaccess;
import model.*;
import java.util.ArrayList;

public interface GameDataAccess {
    void clearGameData();
    int createGame(String gameName);
    ArrayList<GameData> getGameList();
}
