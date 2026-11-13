package ui;

import model.AuthData;
import model.GameData;

public class State {
    private String state;
    private boolean loggedIn;
    private boolean quit;
    private String username;
    private String message;
    private GameData currentGame;

    public State(){
        state = "Logged Out";
        loggedIn = false;
        quit = false;
        message = "";
        username = null;
        currentGame = null;
    }

    public String getState(){
        return state;
    }

    public void setLogIn(String username){
        this.username = username;
        loggedIn = true;
        state = "Logged In";
        currentGame = null;
    }

    public void setLogOut(){
        loggedIn = false;
        username = null;
        state = "Logged Out";
        currentGame = null;
    }

    public void setGamePlay(){
        state = "In Game";
    }

    public void quit(){
        quit = true;
    }

    public boolean isQuit(){
        return quit;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }

    public String getUsername(){
        return username;
    }

    public void setGame(GameData game){
        this.currentGame = game;
    }

    public GameData getGameData(){
        return currentGame;
    }
}
