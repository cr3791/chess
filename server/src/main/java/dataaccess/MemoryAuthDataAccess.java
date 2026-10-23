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
}
