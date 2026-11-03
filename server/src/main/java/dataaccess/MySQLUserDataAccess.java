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
            var statement = "DROP TABLE userData";
            try(var ps = conn.prepareStatement(statement)){
                ps.executeUpdate();
            }
        } catch (SQLException ex){
            throw new DataAccessException("Couldn't access the database");
        }
        userDataList.clear();
    }

    @Override
    public void addUser(UserData userData) {
        userDataList.add(userData);
    }

    @Override
    public UserData getUser(UserData userData){
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
