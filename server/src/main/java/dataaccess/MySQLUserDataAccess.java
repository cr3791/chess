package dataaccess;

import model.UserData;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.sql.*;

public class MySQLUserDataAccess implements UserDataAccess{

    public MySQLUserDataAccess() throws DataAccessException{
        configureDatabase();
    }

    @Override
    public void clearUserData() throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()){
            var statement = "TRUNCATE TABLE userData";
            try(var ps = conn.prepareStatement(statement)){
                ps.executeUpdate();
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
            }
        } catch (SQLException ex){
            throw new DataAccessException("Couldn't access the database from addUser");
        }
    }

    @Override
    public UserData getUser(UserData userData) throws DataAccessException{
        try(Connection conn = DatabaseManager.getConnection()){
            var statement = "SELECT username, password, email FROM userData WHERE username = ?";
            try(var ps = conn.prepareStatement(statement)){
                ps.setString(1,userData.username());
                var rs = ps.executeQuery();
                if (rs.next()){
                    return new UserData(rs.getString("username"),rs.getString("password"),rs.getString("email"));
                } else {
                    return null;
                }
            }
        } catch (SQLException ex){
            throw new DataAccessException("Couldn't access the database from addUser");
        }
    }

    @Override
    public ArrayList<UserData> getUserDataList() throws DataAccessException{
        ArrayList<UserData> userList= new ArrayList<UserData>();
        try(Connection conn = DatabaseManager.getConnection()){
            var statement = "SELECT username, password, email FROM userData";
            try(var ps = conn.prepareStatement(statement)){
                var rs = ps.executeQuery();
                while(rs.next()){
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    String email = rs.getString("email");
                    userList.add(new UserData(username, password, email));
                }
                return userList;
            }
        } catch (SQLException ex){
            throw new DataAccessException("Couldn't access the database from addUser");
        }
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
