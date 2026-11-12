package ui;

public class State {
    String state;
    boolean gamePlay;
    public State(){
        state = "Logged Out";
        gamePlay = false;
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
}
