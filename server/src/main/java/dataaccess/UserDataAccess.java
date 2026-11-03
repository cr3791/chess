package dataaccess;
import model.UserData;
import java.util.ArrayList;

public interface UserDataAccess {
    void clearUserData() throws DataAccessException;
    void addUser(UserData userData) throws DataAccessException;
    UserData getUser(UserData userData) throws DataAccessException;
    ArrayList<UserData> getUserDataList() throws DataAccessException;
}
