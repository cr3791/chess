package dataaccess;

import model.UserData;
import java.util.ArrayList;

public class MemoryUserDataAccess implements UserDataAccess{
    private final ArrayList<UserData> userData = new ArrayList<>();

    @Override
    public void clearUserData() {
        userData.clear();
    }
}
