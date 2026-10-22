package dataaccess;

import model.GameData;
import java.util.ArrayList;

public class MemoryGameDataAccess implements GameDataAccess{
    private final ArrayList<GameData> gameDataList = new ArrayList<>();

    @Override
    public void clearGameData() {
        gameDataList.clear();
    }

    @Override
    public void registerGame() {

    }

    @Override
    public ArrayList<GameData> getGameList() {
        return gameDataList;
    }
}
