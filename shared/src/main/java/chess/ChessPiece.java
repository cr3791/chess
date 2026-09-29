package chess;

import javax.management.RuntimeErrorException;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

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
                possible_moves.addAll(king_move(board, myPosition));
                break;
            case QUEEN:
                possible_moves.addAll(bishop_move(board, myPosition));
                possible_moves.addAll(rook_move(board, myPosition));
                break;
            case ROOK:
                possible_moves.addAll(rook_move(board, myPosition));
                break;
            case BISHOP:
                possible_moves.addAll(bishop_move(board, myPosition));
                break;
            case KNIGHT:
                possible_moves.addAll(knight_move(board, myPosition));
                break;
            case PAWN:
                possible_moves.addAll(pawn_move(board, myPosition));
                break;
            default:
                throw new RuntimeException("Not a valid piece");
        }

        return possible_moves;
    }

    private ArrayList<ChessMove> move(ChessBoard board, ChessPosition myPosition, int x, int y, boolean limiter){
        boolean end = limiter;
        ArrayList<ChessMove> possible_moves = new ArrayList<>();
        do{
            ChessPosition newPosition = new ChessPosition(myPosition.getRow()+x, myPosition.getColumn()+y);
            if(within_bounds(newPosition)){
                if(empty_space(board, newPosition)){
                    possible_moves.add(new ChessMove(myPosition,newPosition,null));
                } else {
                    if (enemy_piece(board.getPiece(myPosition), board.getPiece(newPosition))){
                        possible_moves.add(new ChessMove(myPosition,newPosition,null));
                    }
                    end = true;
                }
            } else {
                end = true;
            }
            x = iterate(x);
            y = iterate(y);
        } while (!end);
        return possible_moves;
    }

    private int iterate(int num){
        if (num == 0){
            return 0;
        } else if (num<0) {
            num-=1;
        } else {
            num+=1;
        }
        return num;
    }

    private ArrayList<ChessMove> king_move(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> possible_moves = new ArrayList<>();

        for (int x = -1; x<2; x++){
            for (int y = -1; y<2; y++){
                if (y==0 && x == 0){
                    continue;
                } else {
                    possible_moves.addAll(move(board,myPosition,x,y,true));
                }
            }
        }

        return possible_moves;
    }

    private ArrayList<ChessMove> rook_move(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> possible_moves = new ArrayList<>();

        possible_moves.addAll(move(board, myPosition, 1,0,false));
        possible_moves.addAll(move(board, myPosition, -1,0,false));
        possible_moves.addAll(move(board, myPosition, 0,-1,false));
        possible_moves.addAll(move(board, myPosition, 0,1,false));

        return possible_moves;
    }

    private ArrayList<ChessMove> bishop_move(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> possible_moves = new ArrayList<>();

        possible_moves.addAll(move(board, myPosition, 1,1,false));
        possible_moves.addAll(move(board, myPosition, -1,1,false));
        possible_moves.addAll(move(board, myPosition, 1,-1,false));
        possible_moves.addAll(move(board, myPosition, -1,-1,false));

        return possible_moves;
    }

    private ArrayList<ChessMove> knight_move(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> possible_moves = new ArrayList<>();

        possible_moves.addAll(move(board, myPosition, 1,2,true));
        possible_moves.addAll(move(board, myPosition, -1,2,true));
        possible_moves.addAll(move(board, myPosition, 1,-2,true));
        possible_moves.addAll(move(board, myPosition, -1,-2,true));
        possible_moves.addAll(move(board, myPosition, 2,1,true));
        possible_moves.addAll(move(board, myPosition, 2,-1,true));
        possible_moves.addAll(move(board, myPosition, -2,1,true));
        possible_moves.addAll(move(board, myPosition, -2,-1,true));

        return possible_moves;
    }

    private ArrayList<ChessMove> pawn_move(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> possible_moves = new ArrayList<>();

        switch(pieceColor){
            case BLACK:
                if (check_starting(myPosition)){
                    possible_moves.addAll(pawn_forward(board,myPosition,-1,2));
                } else {
                    possible_moves.addAll(pawn_forward(board,myPosition,-1,1));
                }
                possible_moves.addAll(pawn_attack(board,myPosition,-1,1));
                possible_moves.addAll(pawn_attack(board,myPosition,-1,-1));
                break;
            case WHITE:
                if (check_starting(myPosition)){
                    possible_moves.addAll(pawn_forward(board,myPosition,1,2));
                } else {
                    possible_moves.addAll(pawn_forward(board,myPosition,1,1));
                }
                possible_moves.addAll(pawn_attack(board,myPosition,1,-1));
                possible_moves.addAll(pawn_attack(board,myPosition,1,1));
                break;
        }

        return possible_moves;
    }

    private ArrayList<ChessMove> pawn_forward(ChessBoard board, ChessPosition myPosition, int y, int loop){
        ArrayList<ChessMove> possible_moves = new ArrayList<>();

        for(int i = 0; i<loop; i++){
            ChessPosition newPosition = new ChessPosition(myPosition.getRow()+y, myPosition.getColumn());
            if(within_bounds(newPosition)&&empty_space(board,newPosition)){
                if(check_promotion(newPosition)){
                    possible_moves.addAll(promotion(board, newPosition, myPosition));
                } else {
                    possible_moves.add(new ChessMove(myPosition, newPosition, null));
                }

            }

            if(!empty_space(board, newPosition)){
                i++;
            }

            y = iterate(y);


        }

        return possible_moves;
    }

    private boolean check_pawn_attack(ChessBoard board, ChessPosition newPosition) {
        if(within_bounds(newPosition) && !(empty_space(board, newPosition))){
            if(board.getPiece(newPosition).getTeamColor() != pieceColor){
                return true;
            }
        }
        return false;
    }

    private ArrayList<ChessMove> pawn_attack(ChessBoard board, ChessPosition myPosition, int x, int y){
        ChessPosition newPosition = new ChessPosition(myPosition.getRow()+x, myPosition.getColumn()+y);
        ArrayList<ChessMove> possible_moves = new ArrayList<>();

        if(check_pawn_attack(board, newPosition)){
            if(check_promotion(newPosition)){
                possible_moves.addAll(promotion(board, newPosition, myPosition));
            } else {
                possible_moves.add(new ChessMove(myPosition, newPosition,null));
            }
        }
        return possible_moves;
    }

    private boolean check_starting(ChessPosition myPosition){
        switch(pieceColor){
            case BLACK:
                if (myPosition.getRow()==7){
                    return true;
                }
                break;
            case WHITE:
                if (myPosition.getRow()==2){
                    return true;
                }
                break;
        }
        return false;
    }

    private boolean check_promotion(ChessPosition newPosition){
        switch(pieceColor){
            case BLACK:
                if (newPosition.getRow()==1){
                    return true;
                }
                break;
            case WHITE:
                if (newPosition.getRow()==8){
                    return true;
                }
                break;
        }
        return false;
    }

    private ArrayList<ChessMove> promotion (ChessBoard board, ChessPosition newPosition, ChessPosition myPosition){
        ArrayList<ChessMove> possible_moves = new ArrayList<>();

        if(empty_space(board, newPosition) || check_pawn_attack(board, newPosition)){
            possible_moves.add(new ChessMove(myPosition,newPosition,PieceType.KNIGHT));
            possible_moves.add(new ChessMove(myPosition,newPosition,PieceType.QUEEN));
            possible_moves.add(new ChessMove(myPosition,newPosition,PieceType.ROOK));
            possible_moves.add(new ChessMove(myPosition,newPosition,PieceType.BISHOP));
        }
        return possible_moves;
    }

    private boolean within_bounds (ChessPosition myPosition){
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        if((row > 8) | (row < 1) | (col > 8) | (col < 1)){
            return false;
        }
        return true;
    }

    private boolean empty_space(ChessBoard board, ChessPosition newPosition){
        if (board.getPiece(newPosition)== null){
            return true;
        }
        return false;
    }

    private boolean enemy_piece(ChessPiece oldPiece, ChessPiece enemyPiece){
        if(oldPiece.getTeamColor() == enemyPiece.getTeamColor()){
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

}
