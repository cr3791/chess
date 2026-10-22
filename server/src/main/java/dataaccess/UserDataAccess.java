package dataaccess;
import model.UserData;
import java.util.ArrayList;

public interface UserDataAccess {
    void clearUserData();
    void addUser(UserData userData);
    UserData getUser(UserData userData);
    ArrayList<UserData> getUserDataList();
}
