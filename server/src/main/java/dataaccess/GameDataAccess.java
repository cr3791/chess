package dataaccess;
import model.*;
import java.util.ArrayList;

public interface GameDataAccess {
    void clearGameData();
    void registerGame();
    ArrayList<GameData> getGameList();
}
