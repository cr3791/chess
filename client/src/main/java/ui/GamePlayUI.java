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

    private String drawWhiteBoard(ChessBoard board){
        StringBuilder drawnBoard = new StringBuilder();
        for(int row = 1; row<=8; row++){
            for (int col = 1; col <= 8; col++){
                ChessPiece piece = board.getPiece(new ChessPosition(row,col));
                if(piece != null){
                    String pieceName = getPieceName(piece);
                    drawnBoard.append("     ");
                } else {
                    drawnBoard.append(EscapeSequences.EMPTY);
                }
            }
        }
        return drawnBoard.toString();
    }

    private String getPieceName(ChessPiece piece){
        String type = piece.getPieceType().toString();
        String color = piece.getTeamColor().toString();
        String pieceName = String.format("%s_%s",color,type);
        return pieceName.toUpperCase();
    }
}
