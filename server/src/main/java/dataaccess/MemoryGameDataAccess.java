package dataaccess;

import model.GameData;
import chess.ChessGame;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryGameDataAccess implements GameDataAccess{
    private final ArrayList<GameData> gameDataList = new ArrayList<>();
    private final AtomicInteger gameIDGenerator = new AtomicInteger(10000);

    @Override
    public void clearGameData() {
        gameDataList.clear();
    }

    @Override
    public int createGame(String gameName) {
        int gameID = generateGameID();
        GameData gameData = new GameData(gameID, null, null, gameName, new ChessGame());
        gameDataList.add(gameData);

        return gameID;
    }

    @Override
    public ArrayList<GameData> getGameList() {
        return gameDataList;
    }

    @Override
    public GameData getGame(int gameID) {
        for(GameData game : gameDataList){
            if (game.gameID() == gameID){
                return game;
            }
        }
        return null;
    }

    @Override
    public void addWhitePlayer(String username, int gameID) {
        for(GameData game : gameDataList){
            if (game.gameID() == gameID){
                gameDataList.remove(game);
                gameDataList.add(game.setWhiteUsername(username));
            }
        }
    }

    @Override
    public void addBlackPlayer(String username, int gameID) {
        for(GameData game : gameDataList){
            if (game.gameID() == gameID){
                gameDataList.remove(game);
                gameDataList.add(game.setBlackUsername(username));
            }
        }
    }

    private int generateGameID(){
        return gameIDGenerator.getAndIncrement();
    }

}
