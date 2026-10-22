package model;
import com.google.gson.*;

public record AuthData(String username, String authToken){

    public String toString(){
        return new Gson().toJson(this);
    }
}
