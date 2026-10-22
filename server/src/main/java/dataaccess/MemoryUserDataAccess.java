package dataaccess;

import model.UserData;
import java.util.ArrayList;

public class MemoryUserDataAccess implements UserDataAccess{
    private final ArrayList<UserData> userDataList = new ArrayList<>();

    @Override
    public void clearUserData() {
        userDataList.clear();
    }

    @Override
    public void addUser(UserData userData) {
        userDataList.add(userData);
    }

    public UserData getUser(UserData userData){
        for (UserData user : userDataList){
            if (user.username().equals(userData.username())){
                return user;
            }
        }
        return null;
    }
}
