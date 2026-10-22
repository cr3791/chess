package dataaccess;
import model.UserData;

public interface UserDataAccess {
    void clearUserData();
    UserData addUser(UserData userData);
}
