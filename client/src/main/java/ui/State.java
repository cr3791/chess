package ui;

public class State {
    String state;
    boolean loggedIn;
    boolean quit;

    public State(){
        state = "Logged Out";
        loggedIn = false;
        quit = false;
    }

    public String getState(){
        return state;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLogIn(){
        loggedIn = true;
        state = "Logged In";
    }

    public void setLogOut(){
        loggedIn = false;
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
}
