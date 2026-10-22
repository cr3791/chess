package dataaccess;

import model.AuthData;
import java.util.ArrayList;

public class MemoryAuthDataAccess implements AuthDataAccess{
    private final ArrayList<AuthData> authData = new ArrayList<>();

    public void clearAuthData(){
        authData.clear();
    }
}
