package dataaccess;

import model.AuthData;
import java.util.ArrayList;
import java.util.UUID;

public class MemoryAuthDataAccess implements AuthDataAccess{
    private final ArrayList<AuthData> authDataList = new ArrayList<>();

    public void clearAuthData(){
        authDataList.clear();
    }

    @Override
    public AuthData addAuthData(String username) {
        String authToken = createToken();
        AuthData authData = new AuthData(authToken, username);
        authDataList.add(authData);
        return authData;
    }

    private String createToken(){
        return UUID.randomUUID().toString();
    }
}
