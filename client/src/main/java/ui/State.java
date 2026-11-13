package ui;

import model.AuthData;

public class State {
    String state;
    boolean loggedIn;
    boolean quit;
    private AuthData authData = null;
    String message;

    public State(){
        state = "Logged Out";
        loggedIn = false;
        quit = false;
        message = "";
    }

    public String getState(){
        return state;
    }

    public void setLogIn(AuthData authData){
        this.authData = authData;
        loggedIn = true;
        state = "Logged In";
    }

    public void setLogOut(){
        loggedIn = false;
        authData = null;
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
}
