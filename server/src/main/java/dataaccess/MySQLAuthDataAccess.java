package dataaccess;

import model.AuthData;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class MySQLAuthDataAccess implements AuthDataAccess {

    public MySQLAuthDataAccess() throws DataAccessException{
        configureDatabase();
    }

    public void clearAuthData() throws DataAccessException{
        try(Connection conn = DatabaseManager.getConnection()){
            var statement = "TRUNCATE TABLE authData";
            try(var ps = conn.prepareStatement(statement)){
                ps.executeUpdate();
            }
        } catch (SQLException ex){
            throw new DataAccessException("Couldn't access the database");
        }
    }

    @Override
    public AuthData addAuthData(String username) throws DataAccessException{
        try(Connection conn = DatabaseManager.getConnection()){
            String authToken = createToken();
            var statement = "INSERT INTO authData (username, authToken) VALUES (?,?)";
            try(var ps = conn.prepareStatement(statement)){
                ps.setString(1,username);
                ps.setString(2,authToken);
                ps.executeUpdate();
                return new AuthData(username, authToken);
            }
        } catch (SQLException ex){
            throw new DataAccessException("Couldn't access the database from addAuth");
        }
    }

    @Override
    public AuthData getAuthData(String authToken) throws DataAccessException{
        try(Connection conn = DatabaseManager.getConnection()){
            var statement = "SELECT username, authToken FROM authData WHERE authToken = ?";
            try(var ps = conn.prepareStatement(statement)){
                ps.setString(1,authToken);
                var rs = ps.executeQuery();
                if (rs.next()){
                    return new AuthData(rs.getString("username"),rs.getString("authToken"));
                } else {
                    return null;
                }
            }
        } catch (SQLException ex){
            throw new DataAccessException("Couldn't access the database from getAuthData");
        }
    }

    @Override
    public void removeAuthData(AuthData authData) throws DataAccessException {
        String authToken = authData.authToken();
        try(Connection conn = DatabaseManager.getConnection()){
            var statement = "DELETE FROM authData WHERE authToken = ?";
            try(var ps = conn.prepareStatement(statement)){
                ps.setString(1,authToken);
                ps.executeUpdate();
            }
        } catch (SQLException ex){
            throw new DataAccessException("Couldn't access the database from removeAuthData");
        }
    }

    @Override
    public ArrayList<AuthData> getAuthDataList() throws DataAccessException {
        ArrayList<AuthData> authList= new ArrayList<>();
        try(Connection conn = DatabaseManager.getConnection()){
            var statement = "SELECT username, authToken FROM authData";
            try(var ps = conn.prepareStatement(statement)){
                var rs = ps.executeQuery();

                while(rs.next()){
                    String username = rs.getString("username");
                    String authToken = rs.getString("authToken");
                    authList.add(new AuthData(username, authToken));
                }
                return authList;
            }
        } catch (SQLException ex){
            throw new DataAccessException("Couldn't access the database from authList");
        }
    }

    private String createToken(){
        return UUID.randomUUID().toString();
    }

    private String createAuthDataString(){
        return """
                CREATE TABLE IF NOT EXISTS authData(
                    `username` VARCHAR(255) NOT NULL,
                    `authToken` VARCHAR(255) NOT NULL PRIMARY KEY
                )
                """;
    }

    private void configureDatabase() throws DataAccessException{
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement(createAuthDataString())){
                preparedStatement.executeUpdate();
            }
        } catch (DataAccessException | SQLException ex){
            throw new DataAccessException("");
        }
    }
}
