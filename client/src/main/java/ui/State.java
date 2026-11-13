package ui;

import model.AuthData;

public class State {
    private String state;
    private boolean loggedIn;
    private boolean quit;
    private String username;
    private String message;

    public State(){
        state = "Logged Out";
        loggedIn = false;
        quit = false;
        message = "";
        username = null;
    }

    public String getState(){
        return state;
    }

    public void setLogIn(String username){
        this.username = username;
        loggedIn = true;
        state = "Logged In";
    }

    public void setLogOut(){
        loggedIn = false;
        username = null;
        state = "Logged Out";
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
}
