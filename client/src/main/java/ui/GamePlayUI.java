package ui;

import java.util.List;

public class GamePlayUI {
    private final ServerFacade facade;
    private State state;
    public GamePlayUI(ServerFacade facade){
        this.facade = facade;
        state = new State();
    }

    public State eval(List<String> inputs, State currentState){
        this.state = currentState;
        return state;
    }
}
