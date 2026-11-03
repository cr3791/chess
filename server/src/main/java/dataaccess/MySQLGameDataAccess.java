package dataaccess;

import chess.ChessGame;
import model.GameData;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MySQLGameDataAccess implements GameDataAccess{
    private final ArrayList<GameData> gameDataList = new ArrayList<>();
    private final AtomicInteger gameIDGenerator = new AtomicInteger(10000);

    public MySQLGameDataAccess() throws DataAccessException{
        configureDatabase();
    }
    @Override
    public void clearGameData() throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()){
            var statement = "TRUNCATE TABLE gameData";
            try(var ps = conn.prepareStatement(statement)){
                ps.executeUpdate();
                gameDataList.clear();
            }
        } catch (SQLException ex){
            throw new DataAccessException("Couldn't access the database");
        }

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

    private String createGameDataString(){
        return """
                CREATE TABLE IF NOT EXISTS gameData(
                    `gameID` INT NOT NULL,
                    `whiteUsername` VARCHAR(255),
                    `blackUsername` VARCHAR(255),
                    `gameName` VARCHAR(255) NOT NULL,
                    `game` BLOB,
                    PRIMARY KEY (`gameID`)
                )
                """;
    }

    private void configureDatabase() throws DataAccessException{
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement(createGameDataString())){
                preparedStatement.executeUpdate();
            }
        } catch (DataAccessException | SQLException ex){
            throw new DataAccessException("");
        }
    }
}
