package dataaccess;
import model.*;

public interface AuthDataAccess {
    void clearAuthData();
    AuthData addAuthData(String username);
    AuthData getAuthData(String authToken);
    void removeAuthData(AuthData authData);
}
