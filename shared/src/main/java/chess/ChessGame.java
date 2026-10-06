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

    private final ChessBoard playing_board = new ChessBoard();
    private TeamColor current_team;
    public ChessGame() {
        playing_board.resetBoard();
        current_team = TeamColor.WHITE;
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
        ArrayList<ChessMove> valid_moves = new ArrayList<>();

        if (piece != null) {
            Collection<ChessMove> possible_moves = piece.pieceMoves(playing_board, startPosition);

            for (ChessMove move : possible_moves) {
                ChessBoard temp_board = new ChessBoard(playing_board);

                if (check_move(temp_board, move)) {
                    valid_moves.add(move);
                }
            }

        }
        return valid_moves;
    }

    private boolean check_move(ChessBoard temp_board, ChessMove move) {
        ChessPiece piece = temp_board.getPiece(move.getStartPosition());
        temp_board.addPiece(move.getStartPosition(), null);
        if (move.getPromotionPiece() != null) {
            temp_board.addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
        } else {
            temp_board.addPiece(move.getEndPosition(), piece);
        }


        if(check_Check(temp_board, current_team)){
            return false;
        } else {
            return true;
        }
    }

    private boolean check_Check(ChessBoard board, TeamColor teamColor){
        Collection<ChessMove> possible_moves = attackKing(board, teamColor);

        if(possible_moves.isEmpty()){
            return false;
        }

        return true;
    }

    private Collection<ChessMove> attackKing(ChessBoard board, TeamColor teamColor){
        ChessPosition kingPosition = findKing(board, teamColor);

        Collection<ChessMove> possible_moves = new ArrayList<>();

        for(int i = 1; i<=8; i++){
            for(int j = 1; j<=8; j++){
                ChessPosition position = new ChessPosition(i,j);
                ChessPiece piece = board.getPiece(position);

                if (piece != null && piece.getTeamColor() != teamColor){

                    Collection<ChessMove> piece_move = piece.pieceMoves(board,position);

                    for(ChessMove move : piece_move){
                        if(move.getEndPosition().equals(kingPosition)){
                            possible_moves.add(move);
                        }
                    }

                }
            }
        }
        return possible_moves;
    }


    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if(validMoves(move.getStartPosition()).contains(move) && current_team.equals(playing_board.getPiece(move.getStartPosition()).getTeamColor())) {
            ChessPiece piece = playing_board.getPiece(move.getStartPosition());
            playing_board.addPiece(move.getStartPosition(), null);
            if (move.getPromotionPiece() != null) {
                playing_board.addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
            } else {
                playing_board.addPiece(move.getEndPosition(), piece);
            }

            if (current_team.equals(TeamColor.BLACK)) {
                current_team = TeamColor.WHITE;
            } else {
                current_team = TeamColor.BLACK;
            }

        } else {
            throw new InvalidMoveException("Move is not valid");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return check_Check(playing_board, teamColor);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {

        for(int row = 1; row<=8; row++){
            for(int col = 1; col<=8; col++){
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = playing_board.getPiece(position);

                if (piece != null && piece.getTeamColor().equals(teamColor)){
                    Collection<ChessMove> possible_moves = validMoves(position);

                    if (!possible_moves.isEmpty()){
                        return false;
                    }
                }

            }
        }

        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        ChessPosition kingPosition = findKing(playing_board, teamColor);

        if (kingPosition!=null && !isInCheck(teamColor) && validMoves(kingPosition).isEmpty()){
            return true;
        }

        return false;
    }

    private ChessPosition findKing(ChessBoard temp_board, TeamColor teamColor){
        for(int i = 1; i<=8; i++){
            for(int j = 1; j<=8; j++){
                ChessPosition position = new ChessPosition(i,j);
                ChessPiece piece = temp_board.getPiece(position);

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(playing_board, chessGame.playing_board) && current_team == chessGame.current_team;
    }

    @Override
    public int hashCode() {
        return Objects.hash(playing_board, current_team);
    }
}
