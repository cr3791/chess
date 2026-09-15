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

        switch(piece.getPieceType()){
            case KING:
                king_moves(myPosition, possible_moves, board, piece);
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
        /*
        if(piece.getPieceType() == PieceType.BISHOP){
            return List.of(new ChessMove(new ChessPosition(5,4), new ChessPosition(1,8), null));
        */
        return possible_moves;
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
}
