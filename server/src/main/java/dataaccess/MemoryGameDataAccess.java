package dataaccess;

import model.GameData;
import java.util.ArrayList;

public class MemoryGameDataAccess implements GameDataAccess{
    private final ArrayList<GameData> gameData = new ArrayList<>();


    @Override
    public void clearGameData() {
        gameData.clear();
    }

    @Override
    public void registerGame() {

    }
}
