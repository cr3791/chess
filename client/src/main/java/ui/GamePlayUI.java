package ui;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.UpperCase;

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
        switch (inputs.getFirst()) {
            default:
                //leave
                state.setLogIn(state.getUsername());
                state.setMessage("\nYou have successfully left the game\n\n");
                break;

        }
        return state;
    }


}
