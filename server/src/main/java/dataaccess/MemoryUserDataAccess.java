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
    public UserData addUser(UserData userData) {
        try{
            checkUsernameTaken(userData.username());
            userDataList.add(userData);
            return userData;
        } catch (AlreadyTakenException ex){
            throw new AlreadyTakenException("Username already taken");
        }
    }

    private void checkUsernameTaken(String username){
        for (UserData user : userDataList){
            if (user.username().equals(username)){
                throw new AlreadyTakenException("Username already taken");
            }
        }
    }
}
