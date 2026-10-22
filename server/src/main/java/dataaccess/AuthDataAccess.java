package dataaccess;
import model.*;

public interface AuthDataAccess {
    void clearAuthData();
    AuthData addAuthData(String username);
}
