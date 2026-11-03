package dataaccess;

import model.AuthData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class MySQLAuthDataAccess implements AuthDataAccess {
    private final ArrayList<AuthData> authDataList = new ArrayList<>();

    public MySQLAuthDataAccess() throws DataAccessException{
        configureDatabase();
    }

    public void clearAuthData() throws DataAccessException{
        try(Connection conn = DatabaseManager.getConnection()){
            var statement = "DROP TABLE authData";
            try(var ps = conn.prepareStatement(statement)){
                ps.executeUpdate();
            }
        } catch (SQLException ex){
            throw new DataAccessException("Couldn't access the database");
        }
        authDataList.clear();
    }

    @Override
    public AuthData addAuthData(String username) {
        String authToken = createToken();
        AuthData authData = new AuthData(username, authToken);
        authDataList.add(authData);
        return authData;
    }

    @Override
    public AuthData getAuthData(String authToken){
        for(AuthData authData : authDataList){
            if (authData.authToken().equals(authToken)){
                return authData;
            }
        }
        return null;
    }

    @Override
    public void removeAuthData(AuthData authData) {
        authDataList.remove(authData);
    }

    @Override
    public ArrayList<AuthData> getAuthDataList() {
        return authDataList;
    }

    private String createToken(){
        return UUID.randomUUID().toString();
    }

    private String createAuthDataString(){
        return """
                CREATE TABLE IF NOT EXISTS authData(
                    `username` VARCHAR(255) NOT NULL,
                    `authToken` VARCHAR(255) NOT NULL,
                    PRIMARY KEY(`username`),
                    KEY(`authToken`)
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
