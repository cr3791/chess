package dataaccess;
import model.*;

import java.util.ArrayList;

public interface AuthDataAccess {
    void clearAuthData() throws DataAccessException;
    AuthData addAuthData(String username) throws DataAccessException;
    AuthData getAuthData(String authToken) throws DataAccessException;
    void removeAuthData(AuthData authData) throws DataAccessException;
    ArrayList<AuthData> getAuthDataList() throws DataAccessException;
}
