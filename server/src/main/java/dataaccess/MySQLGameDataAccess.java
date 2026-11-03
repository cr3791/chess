package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MySQLGameDataAccess implements GameDataAccess{
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
            }
        } catch (SQLException ex){
            throw new DataAccessException("Couldn't access the database");
        }

    }

    @Override
    public int createGame(String gameName) throws DataAccessException{
        int gameID = generateGameID();

        try(Connection conn = DatabaseManager.getConnection()){
            var statement = "INSERT INTO gameData (gameID, gameName, game) VALUES (?,?,?)";
            try(var ps = conn.prepareStatement(statement)){
                ps.setInt(1,gameID);
                ps.setString(2,gameName);
                var gson = new Gson();
                ps.setString(3, gson.toJson(new ChessGame()));
                ps.executeUpdate();

                return gameID;
            }
        } catch (SQLException ex){
            throw new DataAccessException("Couldn't access the database from addUser");
        }
    }

    @Override
    public ArrayList<GameData> getGameList() throws DataAccessException{
        ArrayList<GameData> gameList= new ArrayList<>();
        try(Connection conn = DatabaseManager.getConnection()){
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM gameData";
            try(var ps = conn.prepareStatement(statement)){
                var rs = ps.executeQuery();

                var gson = new Gson();

                while(rs.next()){
                    int gameID = rs.getInt("gameID");
                    String whiteUsername = rs.getString("whiteUsername");
                    String blackUsername = rs.getString("blackUsername");
                    String gameName = rs.getString("gameName");
                    String gameString = rs.getString("game");
                    ChessGame game = gson.fromJson(gameString, ChessGame.class);
                    gameList.add(new GameData(gameID, whiteUsername, blackUsername, gameName, game));
                }
                return gameList;
            }
        } catch (SQLException ex){
            throw new DataAccessException("Couldn't access the database from addUser");
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException{
        try(Connection conn = DatabaseManager.getConnection()){
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM gameData WHERE gameID = ?";
            try(var ps = conn.prepareStatement(statement)){
                ps.setInt(1,gameID);
                var rs = ps.executeQuery();
                if (rs.next()){
                    String whiteUsername = rs.getString("whiteUsername");
                    String blackUsername = rs.getString("blackUsername");
                    String gameName = rs.getString("gameName");
                    String gameString = rs.getString("game");
                    var gson = new Gson();
                    ChessGame game = gson.fromJson(gameString, ChessGame.class);
                    return new GameData(gameID,whiteUsername,blackUsername, gameName, game);
                } else {
                    return null;
                }
            }
        } catch (SQLException ex){
            throw new DataAccessException("Couldn't access the database from addUser");
        }
    }

    @Override
    public void addWhitePlayer(String username, int gameID) throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()){
            var statement = "UPDATE gameData SET whiteUsername = ? WHERE gameID = ?";
            try(var ps = conn.prepareStatement(statement)){
                ps.setString(1,username);
                ps.setInt(2,gameID);
                ps.executeUpdate();
            }
        } catch (SQLException ex){
            throw new DataAccessException("Couldn't access the database from addWhitePlayer");
        }
    }

    @Override
    public void addBlackPlayer(String username, int gameID) throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()){
            var statement = "UPDATE gameData SET blackUsername = ? WHERE gameID = ?";
            try(var ps = conn.prepareStatement(statement)){
                ps.setString(1,username);
                ps.setInt(2,gameID);
                ps.executeUpdate();
            }
        } catch (SQLException ex){
            throw new DataAccessException("Couldn't access the database from addWhitePlayer");
        }
    }

    private int generateGameID(){
        return gameIDGenerator.getAndIncrement();
    }

    private String[] createGameDataString(){
        return new String[]{"""
                CREATE TABLE IF NOT EXISTS gameData(
                    `gameID` INT NOT NULL,
                    `whiteUsername` VARCHAR(255),
                    `blackUsername` VARCHAR(255),
                    `gameName` VARCHAR(255) NOT NULL,
                    `game` MEDIUMTEXT NOT NULL,
                    PRIMARY KEY (`gameID`)
                )
                """};
    }

    private void configureDatabase() throws DataAccessException{
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()){
            for (String gameDataString : createGameDataString()) {
                try (var preparedStatement = conn.prepareStatement(gameDataString)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (DataAccessException | SQLException ex){
            throw new DataAccessException("");
        }
    }
}
