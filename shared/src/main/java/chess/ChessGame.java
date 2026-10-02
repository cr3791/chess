package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private final ChessBoard playing_board = new ChessBoard();
    private TeamColor current_team;
    public ChessGame() {
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return current_team;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        current_team = team;
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
        ChessPiece piece = playing_board.getPiece(startPosition);

        if (piece == null){
            return null;
        }

        Collection<ChessMove> possible_moves = piece.pieceMoves(playing_board,startPosition);

        return possible_moves;
    }



    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        ChessPosition kingPosition = findKing(teamColor);

        if(kingPosition!=null && !isInCheck(teamColor) && validMoves(kingPosition).isEmpty()){
            return true;
        }

        return false;
    }

    private ChessPosition findKing(TeamColor teamColor){
        for(int i = 1; i<=8; i++){
            for(int j = 1; j<=8; j++){
                ChessPosition position = new ChessPosition(i,j);
                ChessPiece piece = playing_board.getPiece(position);

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

                playing_board.addPiece(position, piece);
            }
        }
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return playing_board;
    }



    /*
    ChessPosition startPosition = move.getStartPosition();
        Collection<ChessMove> possible_moves = validMoves(startPosition);
        ChessPiece piece = playing_board[startPosition.getRow()-1][startPosition.getColumn()-1];

        if(possible_moves.contains(move)){
            playing_board[startPosition.getRow()-1][startPosition.getColumn()-1] = null;
        }

        public Collection<ChessMove> kingAttack(Collection<ChessMove> king_moves, ChessPosition kingPosition){
        TeamColor king_color = playing_board.getPiece(kingPosition).pieceColor;
        Collection<ChessMove> possible_moves = new ArrayList<ChessMove>();

        for(int i = 1; i<=8; i++){
            for(int j = 1; j<=8; j++){
                ChessPosition position = new ChessPosition(i,j);
                ChessPiece piece = playing_board.getPiece(position);

                if(piece!=null && piece.getTeamColor()!=king_color){
                    possible_moves = piece.pieceMoves(playing_board,position);
                    for (ChessMove move : possible_moves){
                        king_moves.remove(move);
                    }
                }
            }
        }

        return king_moves;
    }




     */
}
