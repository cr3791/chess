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

    private boolean hasMoved;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
        hasMoved = false;
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
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();

        switch(piece.getPieceType()){
            case KING:
                possibleMoves.addAll(kingMove(board, myPosition));
                break;
            case QUEEN:
                possibleMoves.addAll(bishopMove(board, myPosition));
                possibleMoves.addAll(rookMove(board, myPosition));
                break;
            case ROOK:
                possibleMoves.addAll(rookMove(board, myPosition));
                break;
            case BISHOP:
                possibleMoves.addAll(bishopMove(board, myPosition));
                break;
            case KNIGHT:
                possibleMoves.addAll(knightMove(board, myPosition));
                break;
            case PAWN:
                possibleMoves.addAll(pawnMove(board, myPosition));
                break;
            default:
                throw new RuntimeException("Not a valid piece");
        }

        return possibleMoves;
    }

    private ArrayList<ChessMove> move(ChessBoard board, ChessPosition myPosition, int x, int y, boolean limiter){
        boolean end = limiter;
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        do{
            ChessPosition newPosition = new ChessPosition(myPosition.getRow()+x, myPosition.getColumn()+y);
            if(withinBounds(newPosition)){
                if(emptySpace(board, newPosition)){
                    possibleMoves.add(new ChessMove(myPosition,newPosition,null));
                } else {
                    if (enemyPiece(board.getPiece(myPosition), board.getPiece(newPosition))){
                        possibleMoves.add(new ChessMove(myPosition,newPosition,null));
                    }
                    end = true;
                }
            } else {
                end = true;
            }
            x = iterate(x);
            y = iterate(y);
        } while (!end);
        return possibleMoves;
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

    private ArrayList<ChessMove> kingMove(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();

        for (int x = -1; x<2; x++){
            for (int y = -1; y<2; y++){
                if (y==0 && x == 0){
                    continue;
                } else {
                    possibleMoves.addAll(move(board,myPosition,x,y,true));
                }
            }
        }

        return possibleMoves;
    }

    private ArrayList<ChessMove> rookMove(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();

        possibleMoves.addAll(move(board, myPosition, 1,0,false));
        possibleMoves.addAll(move(board, myPosition, -1,0,false));
        possibleMoves.addAll(move(board, myPosition, 0,-1,false));
        possibleMoves.addAll(move(board, myPosition, 0,1,false));

        return possibleMoves;
    }

    private ArrayList<ChessMove> bishopMove(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();

        possibleMoves.addAll(move(board, myPosition, 1,1,false));
        possibleMoves.addAll(move(board, myPosition, -1,1,false));
        possibleMoves.addAll(move(board, myPosition, 1,-1,false));
        possibleMoves.addAll(move(board, myPosition, -1,-1,false));

        return possibleMoves;
    }

    private ArrayList<ChessMove> knightMove(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();

        possibleMoves.addAll(move(board, myPosition, 1,2,true));
        possibleMoves.addAll(move(board, myPosition, -1,2,true));
        possibleMoves.addAll(move(board, myPosition, 1,-2,true));
        possibleMoves.addAll(move(board, myPosition, -1,-2,true));
        possibleMoves.addAll(move(board, myPosition, 2,1,true));
        possibleMoves.addAll(move(board, myPosition, 2,-1,true));
        possibleMoves.addAll(move(board, myPosition, -2,1,true));
        possibleMoves.addAll(move(board, myPosition, -2,-1,true));

        return possibleMoves;
    }

    private ArrayList<ChessMove> pawnMove(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();

        switch(pieceColor){
            case BLACK:
                if (checkStarting(myPosition)){
                    possibleMoves.addAll(pawnForward(board,myPosition,-1,2));
                } else {
                    possibleMoves.addAll(pawnForward(board,myPosition,-1,1));
                }
                possibleMoves.addAll(pawnAttack(board,myPosition,-1,1));
                possibleMoves.addAll(pawnAttack(board,myPosition,-1,-1));
                break;
            case WHITE:
                if (checkStarting(myPosition)){
                    possibleMoves.addAll(pawnForward(board,myPosition,1,2));
                } else {
                    possibleMoves.addAll(pawnForward(board,myPosition,1,1));
                }
                possibleMoves.addAll(pawnAttack(board,myPosition,1,-1));
                possibleMoves.addAll(pawnAttack(board,myPosition,1,1));
                break;
        }

        return possibleMoves;
    }

    private ArrayList<ChessMove> pawnForward(ChessBoard board, ChessPosition myPosition, int y, int loop){
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();

        for(int i = 0; i<loop; i++){
            ChessPosition newPosition = new ChessPosition(myPosition.getRow()+y, myPosition.getColumn());
            if(withinBounds(newPosition)&&emptySpace(board,newPosition)){
                if(checkPromotion(newPosition)){
                    possibleMoves.addAll(promotion(board, newPosition, myPosition));
                } else {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                }

            }

            if(!emptySpace(board, newPosition)){
                i++;
            }

            y = iterate(y);


        }

        return possibleMoves;
    }

    private boolean checkPawnAttack(ChessBoard board, ChessPosition newPosition) {
        if(withinBounds(newPosition) && !(emptySpace(board, newPosition))){
            if(board.getPiece(newPosition).getTeamColor() != pieceColor){
                return true;
            }
        }
        return false;
    }

    private ArrayList<ChessMove> pawnAttack(ChessBoard board, ChessPosition myPosition, int x, int y){
        ChessPosition newPosition = new ChessPosition(myPosition.getRow()+x, myPosition.getColumn()+y);
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();

        if(checkPawnAttack(board, newPosition)){
            if(checkPromotion(newPosition)){
                possibleMoves.addAll(promotion(board, newPosition, myPosition));
            } else {
                possibleMoves.add(new ChessMove(myPosition, newPosition,null));
            }
        }
        return possibleMoves;
    }

    private boolean checkStarting(ChessPosition myPosition){
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

    private boolean checkPromotion(ChessPosition newPosition){
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
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();

        if(emptySpace(board, newPosition) || checkPawnAttack(board, newPosition)){
            possibleMoves.add(new ChessMove(myPosition,newPosition,PieceType.KNIGHT));
            possibleMoves.add(new ChessMove(myPosition,newPosition,PieceType.QUEEN));
            possibleMoves.add(new ChessMove(myPosition,newPosition,PieceType.ROOK));
            possibleMoves.add(new ChessMove(myPosition,newPosition,PieceType.BISHOP));
        }
        return possibleMoves;
    }

    private boolean withinBounds (ChessPosition myPosition){
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        if((row > 8) | (row < 1) | (col > 8) | (col < 1)){
            return false;
        }
        return true;
    }

    private boolean emptySpace(ChessBoard board, ChessPosition newPosition){
        if (board.getPiece(newPosition)== null){
            return true;
        }
        return false;
    }

    private boolean enemyPiece(ChessPiece oldPiece, ChessPiece enemyPiece){
        if(oldPiece.getTeamColor() == enemyPiece.getTeamColor()){
            return false;
        }
        return true;
    }

    public void setHasMoved(boolean set){
        hasMoved = set;
    }

    public boolean getHasMoved(){
        return hasMoved;
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
