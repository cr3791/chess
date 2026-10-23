package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private final ChessBoard playingBoard = new ChessBoard();
    private TeamColor currentTeam;

    private ChessMove lastMove;

    public ChessGame() {
        playingBoard.resetBoard();
        currentTeam = TeamColor.WHITE;
        lastMove = null;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTeam;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTeam = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = playingBoard.getPiece(startPosition);
        ArrayList<ChessMove> validMoves = new ArrayList<>();

        if (piece != null) {
            Collection<ChessMove> possibleMoves = piece.pieceMoves(playingBoard, startPosition);

            if(checkEnPassant(startPosition)){
                if(piece.getTeamColor()==TeamColor.BLACK){
                    ChessPosition endPosition = new ChessPosition(lastMove.getEndPosition().getRow()-1, lastMove.getEndPosition().getColumn());
                    validMoves.add(new ChessMove(startPosition, endPosition, null));
                } else {
                    ChessPosition endPosition = new ChessPosition(lastMove.getEndPosition().getRow()+1, lastMove.getEndPosition().getColumn());
                    validMoves.add(new ChessMove(startPosition, endPosition, null));
                }
            }

            for (ChessMove move : possibleMoves) {
                ChessBoard tempBoard = new ChessBoard(playingBoard);

                if (checkMove(tempBoard, move)) {
                    validMoves.add(move);
                }
            }
        }
        return validMoves;
    }

    private boolean checkMove(ChessBoard tempBoard, ChessMove move) {
        ChessPiece piece = tempBoard.getPiece(move.getStartPosition());
        tempBoard.addPiece(move.getStartPosition(), null);
        if (move.getPromotionPiece() != null) {
            tempBoard.addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
        } else {
            tempBoard.addPiece(move.getEndPosition(), piece);
        }


        if(checkCheck(tempBoard, piece.getTeamColor())){
            return false;
        } else {
            return true;
        }
    }

    private boolean checkCheck(ChessBoard board, TeamColor teamColor){
        Collection<ChessMove> possibleMoves = attackKing(board, teamColor);

        if(possibleMoves.isEmpty()){
            return false;
        }

        return true;
    }

    private Collection<ChessMove> attackKing(ChessBoard board, TeamColor teamColor){
        ChessPosition kingPosition = findKing(board, teamColor);

        Collection<ChessMove> possibleMoves = new ArrayList<>();

        for(int i = 1; i<=8; i++){
            for(int j = 1; j<=8; j++){
                ChessPosition position = new ChessPosition(i,j);
                ChessPiece piece = board.getPiece(position);

                if (piece != null && piece.getTeamColor() != teamColor){
                    possibleMoves.addAll(kingAttackMoves(position, board, kingPosition));

                }
            }
        }
        return possibleMoves;
    }

    private Collection<ChessMove> kingAttackMoves(ChessPosition piecePosition, ChessBoard board, ChessPosition kingPosition){
        Collection<ChessMove> kingAttackMoveList = new ArrayList<>();
        ChessPiece piece = board.getPiece(piecePosition);
        Collection<ChessMove> pieceMove = piece.pieceMoves(board,piecePosition);
        for(ChessMove move : pieceMove){
            if(move.getEndPosition().equals(kingPosition)){
                kingAttackMoveList.add(move);
            }
        }
        return kingAttackMoveList;
    }


    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if(validMoves(move.getStartPosition()).contains(move) && currentTeam.equals(playingBoard.getPiece(move.getStartPosition()).getTeamColor())) {
            if(moveIsEnPassant(move)){
                ChessPiece piece = playingBoard.getPiece(move.getStartPosition());
                playingBoard.addPiece(lastMove.getEndPosition(),null);
                playingBoard.addPiece(move.getStartPosition(),null);
                playingBoard.addPiece(move.getEndPosition(), piece);
            }else{
                ChessPiece piece = playingBoard.getPiece(move.getStartPosition());
                playingBoard.addPiece(move.getStartPosition(), null);
                if (move.getPromotionPiece() != null) {
                    playingBoard.addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
                } else {
                    playingBoard.addPiece(move.getEndPosition(), piece);
                }
            }

            playingBoard.getPiece(move.getEndPosition()).setHasMoved(true);
            lastMove = move;
            if (currentTeam.equals(TeamColor.BLACK)) {
                currentTeam = TeamColor.WHITE;
            } else {
                currentTeam = TeamColor.BLACK;
            }

        } else {
            throw new InvalidMoveException("Move is not valid");
        }
    }

    private boolean moveIsEnPassant(ChessMove move){
        if(checkEnPassant(move.getStartPosition())){
            if(playingBoard.getPiece(move.getStartPosition()).getTeamColor() == TeamColor.BLACK){
                return move.getEndPosition().getRow() == 3 && move.getEndPosition().getColumn() == lastMove.getEndPosition().getColumn();
            } else {
                return move.getEndPosition().getRow() == 6 && move.getEndPosition().getColumn() == lastMove.getEndPosition().getColumn();
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return checkCheck(playingBoard, teamColor);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return !canMove(teamColor) && isInCheck(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @return True if the specified team is in stalemate, otherwise false
     */

    private boolean checkEnPassant(ChessPosition currentPosition){
        if(lastMove!=null){
            ChessPiece.PieceType lastMovePiece = playingBoard.getPiece(lastMove.getEndPosition()).getPieceType();
            ChessPiece.PieceType currentPiece = playingBoard.getPiece(currentPosition).getPieceType();

            boolean checkBothPawns = (lastMovePiece.equals(ChessPiece.PieceType.PAWN) && currentPiece.equals(ChessPiece.PieceType.PAWN));
            boolean checkDifferentTeamColor = checkDifferentTeamColor(playingBoard.getPiece(currentPosition), playingBoard.getPiece(lastMove.getEndPosition()));

            if (checkBothPawns && checkDifferentTeamColor) {
                return checkEnPassantColumnsAndRows(lastMove.getEndPosition(), currentPosition);
            }
        }
        return false;
    }

    private boolean checkDifferentTeamColor(ChessPiece piece1, ChessPiece piece2){
        return piece1.getTeamColor() != piece2.getTeamColor();
    }

    private boolean checkEnPassantColumnsAndRows(ChessPosition lastPosition,ChessPosition currentPosition){
        if(playingBoard.getPiece(lastPosition).getTeamColor()==TeamColor.WHITE){
            boolean whiteFirstCheck = (lastPosition.getRow()==4 && (currentPosition.getColumn()==lastPosition.getColumn()-1));
            boolean whiteSecondCheck = (currentPosition.getColumn()==lastPosition.getColumn()+1);
            if( whiteFirstCheck || whiteSecondCheck){
                return true;
            }
        } else {
            boolean blackFirstCheck = (lastPosition.getRow()==5 && (currentPosition.getColumn()==lastPosition.getColumn()-1));
            boolean blackSecondCheck = (currentPosition.getColumn()==lastPosition.getColumn()+1);
            if(blackFirstCheck || blackSecondCheck){
                return true;
            }
        }
        return false;
    }

    private boolean canMove(TeamColor teamColor){
        for(int row = 1; row<=8; row++){
            for(int col = 1; col<=8; col++){
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = playingBoard.getPiece(position);

                if (piece != null && piece.getTeamColor().equals(teamColor)){
                    Collection<ChessMove> possibleMoves = validMoves(position);

                    if (!possibleMoves.isEmpty()){
                        return true;
                    }
                }

            }
        }
        return false;
    }

    public boolean isInStalemate(TeamColor teamColor) {
        return !canMove(teamColor) && !isInCheck(teamColor);
    }

    private ChessPosition findKing(ChessBoard tempBoard, TeamColor teamColor){
        for(int i = 1; i<=8; i++){
            for(int j = 1; j<=8; j++){
                ChessPosition position = new ChessPosition(i,j);
                ChessPiece piece = tempBoard.getPiece(position);

                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor){
                    return position;
                }
            }
        }
        return null;
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */

    public void setBoard(ChessBoard board) {
        for(int i = 1; i<=8; i++){
            for(int j = 1; j<=8; j++){
                ChessPosition position = new ChessPosition(i,j);
                ChessPiece piece = board.getPiece(position);

                playingBoard.addPiece(position, piece);
            }
        }
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return playingBoard;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(playingBoard, chessGame.playingBoard) && currentTeam == chessGame.currentTeam;
    }

    @Override
    public int hashCode() {
        return Objects.hash(playingBoard, currentTeam);
    }
}
