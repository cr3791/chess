package chess;

import javax.management.RuntimeErrorException;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    ChessGame.TeamColor pieceColor;
    ChessPiece.PieceType type;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);

        ArrayList<ChessMove> possible_moves = new ArrayList<>();
        moves(myPosition, possible_moves, board, piece);

        return possible_moves;
    }

    private void moves(ChessPosition myPosition, ArrayList<ChessMove> possible_moves, ChessBoard board, ChessPiece piece) {

        switch(piece.getPieceType()){
            case KING:
                break;
            case QUEEN:
                break;
            case ROOK:
                break;
            case BISHOP:
                break;
            case KNIGHT:
                break;
            case PAWN:
                break;
            default:
                throw new RuntimeException("Not a valid piece");
        }

    }

    private boolean within_bounds (ChessPosition myPosition){
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        if((row > 8) | (row < 1) | (col > 8) | (col < 1)){
            return false;
        }
        return true;
    }

    private boolean empty_space(ChessPiece piece){
        if (piece== null){
            return true;
        }
        return false;
    }

    private boolean enemy_piece(ChessPiece old_piece, ChessPiece new_piece){
        if(old_piece.getTeamColor() == new_piece.getTeamColor()){
            return false;
        }
        return true;
    }
/*
    //sideways
    private void forwards(boolean valid_move, boolean blocked, ChessPosition myPosition, ChessBoard board, ChessPiece piece, ArrayList<ChessMove> possible_moves){
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        boolean f;
        do{
            col +=1;
            ChessPosition newPosition = new ChessPosition(row,col);
            f = check_valid_move(valid_move, blocked, newPosition, board, piece);
            if(f){
                possible_moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }while(f & !blocked);
        valid_move = true;
        blocked = false;
    }

    private void backwards(boolean valid_move, boolean blocked, ChessPosition myPosition, ChessBoard board, ChessPiece piece, ArrayList<ChessMove> possible_moves){
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        do{
            col -=1;
            ChessPosition newPosition = new ChessPosition(row,col);
            check_valid_move(valid_move, blocked, newPosition, board, piece);
            if(valid_move){
                possible_moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }while(valid_move & !blocked);
        valid_move = true;
        blocked = false;
    }

    private void move_right(boolean valid_move, boolean blocked, ChessPosition myPosition, ChessBoard board, ChessPiece piece, ArrayList<ChessMove> possible_moves){
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        do{
            row +=1;
            ChessPosition newPosition = new ChessPosition(row,col);
            check_valid_move(valid_move, blocked, newPosition, board, piece);
            if(valid_move){
                possible_moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }while(valid_move & !blocked);
        valid_move = true;
        blocked = false;
    }

    private void move_left(boolean valid_move, boolean blocked, ChessPosition myPosition, ChessBoard board, ChessPiece piece, ArrayList<ChessMove> possible_moves){
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        do{
            row -=1;
            ChessPosition newPosition = new ChessPosition(row,col);
            check_valid_move(valid_move, blocked, newPosition, board, piece);
            if(valid_move){
                possible_moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }while(valid_move & !blocked);
        valid_move = true;
        blocked = false;
    }

    private void king_moves(boolean valid_move, ChessPosition myPosition, ArrayList<ChessMove> possible_moves, ChessBoard board, ChessPiece piece){
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        for (int i = -1; i<2; i++){
            for (int j = -1; j<2; j++){
                ChessPosition newPosition = new ChessPosition(row+i,col+j);

                check_valid_move(valid_move, false, newPosition, board, piece);
                if(valid_move && newPosition != myPosition){
                    possible_moves.add(new ChessMove(myPosition, newPosition, null));
                }

            }
        }
    }

    private boolean check_valid_move(ChessPosition newPosition, ChessBoard board, ChessPiece piece){
        if (newPosition.check_valid_position()){
            ChessPiece check_piece = board.getPiece(newPosition);

            if(check_piece == null){
                return true;
            } else if (check_piece.getTeamColor() != piece.getTeamColor()){
                return true;
            }
        }
        return false;
    }

    private void king_moves(ChessPosition myPosition, ArrayList<ChessMove> possible_moves, ChessBoard board, ChessPiece piece){
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        for (int i = -1; i<2; i++){
            for (int j = -1; j<2; j++){
                ChessPosition newPosition = new ChessPosition(row+i,col+j);

                if(check_valid_move(newPosition, board, piece) && newPosition != myPosition){
                    possible_moves.add(new ChessMove(myPosition, newPosition, null));
                }

            }
        }
    }

    private void rook_moves(ChessPosition myPosition, ArrayList<ChessMove> possible_moves, ChessBoard board, ChessPiece piece){
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        do{

        }while()

        if(check_valid_move(newPosition, board, piece) && newPosition != myPosition){
            possible_moves.add(new ChessMove(myPosition, newPosition, null));
        }

    }

    private boolean check_valid_move(boolean valid_move, boolean blocked, ChessPosition newPosition,  ChessBoard board, ChessPiece piece){
        if (newPosition.check_valid_position() && !blocked){
            ChessPiece check_piece = board.getPiece(newPosition);

            if(check_piece == null){
                valid_move = true;
            } else if (check_piece.getTeamColor() != piece.getTeamColor()){
                valid_move = true;
                blocked = true;
            } else {
                valid_move = false;
                blocked = true;
            }
        }
        valid_move = false;
        blocked = true;

        return valid_move;
    }

     */
}
