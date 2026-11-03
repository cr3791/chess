package dataaccess;

import model.UserData;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.sql.*;

public class MySQLUserDataAccess implements UserDataAccess{
    private final ArrayList<UserData> userDataList = new ArrayList<>();

    public MySQLUserDataAccess() throws DataAccessException{
        configureDatabase();
    }

    @Override
    public void clearUserData() throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()){
            var statement = "TRUNCATE TABLE userData";
            try(var ps = conn.prepareStatement(statement)){
                ps.executeUpdate();
                userDataList.clear();
            }
        } catch (SQLException ex){
            throw new DataAccessException("Couldn't access the database");
        }
    }

    @Override
    public void addUser(UserData userData) throws DataAccessException{
        try(Connection conn = DatabaseManager.getConnection()){
            var statement = "INSERT INTO userData (username, password, email) VALUES (?,?,?)";
            try(var ps = conn.prepareStatement(statement)){
                ps.setString(1,userData.username());
                ps.setString(2,userData.password());
                ps.setString(3,userData.email());
                ps.executeUpdate();
                userDataList.add(userData);
            }
        } catch (SQLException ex){
            throw new DataAccessException("Couldn't access the database from addUser");
        }
    }

    @Override
    public UserData getUser(UserData userData) throws DataAccessException{
        try(Connection conn = DatabaseManager.getConnection()){
            var statement = "SELECT username, password, email FROM userData WHERE username = ? AND password = ? AND email = ?";
            try(var ps = conn.prepareStatement(statement)){
                ps.setString(1,userData.username());
                ps.setString(2,userData.password());
                ps.setString(3,userData.email());
                ps.executeUpdate();
                return getUserFromList(userData);
            }
        } catch (SQLException ex){
            throw new DataAccessException("Couldn't access the database from addUser");
        }
    }

    public UserData getUserFromList(UserData userData){
        for (UserData user : userDataList){
            if (user.username().equals(userData.username())){
                return user;
            }
        }
        return null;
    }

    @Override
    public ArrayList<UserData> getUserDataList(){
        return userDataList;
    }

    private String createUserDataString(){
        return """
                CREATE TABLE IF NOT EXISTS userData(
                    `username` VARCHAR(255) NOT NULL,
                    `password` VARCHAR(255) NOT NULL,
                    `email` VARCHAR(255) NOT NULL,
                    PRIMARY KEY (`username`)
                )
                """;
    }

    private void configureDatabase() throws DataAccessException{
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement(createUserDataString())){
                preparedStatement.executeUpdate();
            }
        } catch (DataAccessException | SQLException ex){
            throw new DataAccessException("");
        }
    }
}
