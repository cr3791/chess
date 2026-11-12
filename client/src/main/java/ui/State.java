package ui;

public class State {
    String state;
    boolean gamePlay;
    boolean quit;

    public State(){
        state = "Logged Out";
        gamePlay = false;
        quit = false;
    }

    public String getState(){
        return state;
    }

    public boolean isGamePlay() {
        return gamePlay;
    }

    public void setGamePlay(boolean active){
        gamePlay = active;
    }

    public void setLogIn(){
        state = "Logged In";
    }

    public void setLogOut(){
        state = "Logged Out";
    }

    public void quit(){
        quit = true;
    }

    public boolean isQuit(){
        return quit;
    }
}
