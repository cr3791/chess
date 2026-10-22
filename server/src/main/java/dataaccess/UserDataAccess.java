package dataaccess;
import model.UserData;

public interface UserDataAccess {
    void clearUserData();
    UserData getUser(UserData userData);
    void addUser(UserData userData);
}
