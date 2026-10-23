package dataaccess;
import model.*;

import java.util.ArrayList;

public interface AuthDataAccess {
    void clearAuthData();
    AuthData addAuthData(String username);
    AuthData getAuthData(String authToken);
    void removeAuthData(AuthData authData);
    ArrayList<AuthData> getAuthDataList();
}
