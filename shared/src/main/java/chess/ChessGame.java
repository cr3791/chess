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

    private ChessMove lastMove;

    public ChessGame() {
        playing_board.resetBoard();
        current_team = TeamColor.WHITE;
        lastMove = null;
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

            if(check_EnPassant(startPosition)){
                if(piece.getTeamColor()==TeamColor.BLACK){
                    ChessPosition endPosition = new ChessPosition(lastMove.getEndPosition().getRow()-1, lastMove.getEndPosition().getColumn());
                    valid_moves.add(new ChessMove(startPosition, endPosition, null));
                } else {
                    ChessPosition endPosition = new ChessPosition(lastMove.getEndPosition().getRow()+1, lastMove.getEndPosition().getColumn());
                    valid_moves.add(new ChessMove(startPosition, endPosition, null));
                }
            }

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


        if(check_Check(temp_board, piece.getTeamColor())){
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
            if(moveIsEnPassant(move)){
                ChessPiece piece = playing_board.getPiece(move.getStartPosition());
                playing_board.addPiece(lastMove.getEndPosition(),null);
                playing_board.addPiece(move.getStartPosition(),null);
                playing_board.addPiece(move.getEndPosition(), piece);
            }else{
                ChessPiece piece = playing_board.getPiece(move.getStartPosition());
                playing_board.addPiece(move.getStartPosition(), null);
                if (move.getPromotionPiece() != null) {
                    playing_board.addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
                } else {
                    playing_board.addPiece(move.getEndPosition(), piece);
                }
            }

            playing_board.getPiece(move.getEndPosition()).setHas_moved(true);
            lastMove = move;
            if (current_team.equals(TeamColor.BLACK)) {
                current_team = TeamColor.WHITE;
            } else {
                current_team = TeamColor.BLACK;
            }

        } else {
            throw new InvalidMoveException("Move is not valid");
        }
    }

    private boolean moveIsEnPassant(ChessMove move){
        if(check_EnPassant(move.getStartPosition())){
            if(playing_board.getPiece(move.getStartPosition()).getTeamColor() == TeamColor.BLACK){
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
        return check_Check(playing_board, teamColor);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return !can_Move(teamColor) && isInCheck(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */

    private boolean check_EnPassant(ChessPosition currentPosition){
        if(lastMove!=null){
            ChessPiece.PieceType lastMovePiece = playing_board.getPiece(lastMove.getEndPosition()).getPieceType();
            ChessPiece.PieceType currentPiece = playing_board.getPiece(currentPosition).getPieceType();

            if (lastMovePiece.equals(ChessPiece.PieceType.PAWN) && currentPiece.equals(ChessPiece.PieceType.PAWN) && playing_board.getPiece(currentPosition).getTeamColor() != playing_board.getPiece(lastMove.getEndPosition()).getTeamColor()) {
                return checkEnPassantColumnsAndRows(lastMove.getEndPosition(), currentPosition);
            }
        }
        return false;
    }

    private boolean checkEnPassantColumnsAndRows(ChessPosition lastPosition,ChessPosition currentPosition){
        if(playing_board.getPiece(lastPosition).getTeamColor()==TeamColor.WHITE){
            if(lastPosition.getRow()==4 && (currentPosition.getColumn()==lastPosition.getColumn()-1 || currentPosition.getColumn()==lastPosition.getColumn()+1)){
                return true;
            }
        } else {
            if(lastPosition.getRow()==5 && (currentPosition.getColumn()==lastPosition.getColumn()-1 || currentPosition.getColumn()==lastPosition.getColumn()+1)){
                return true;
            }
        }
        return false;
    }

    private boolean can_Move(TeamColor teamColor){
        for(int row = 1; row<=8; row++){
            for(int col = 1; col<=8; col++){
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = playing_board.getPiece(position);

                if (piece != null && piece.getTeamColor().equals(teamColor)){
                    Collection<ChessMove> possible_moves = validMoves(position);

                    if (!possible_moves.isEmpty()){
                        return true;
                    }
                }

            }
        }
        return false;
    }

    public boolean isInStalemate(TeamColor teamColor) {
        return !can_Move(teamColor) && !isInCheck(teamColor);
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

    /*

    private boolean moveIsCastling(ChessMove move){
        if(check_Castling(move.getStartPosition())){
            if(isValidCastleRook(move.getStartPosition()) || isValidCastleKing(move.getStartPosition())){
                return isValidCastleEnd(move.getStartPosition(),move.getEndPosition());
            }
        }
        return false;
    }

    private boolean isValidCastleEnd(ChessPosition startPosition, ChessPosition endPosition){

        if(startPosition.getColumn()<5){
            return endPosition.getColumn()==4 && endPosition.getRow() == startPosition.getRow();
        } else if (startPosition.getColumn()>5) {
            return endPosition.getColumn()==6 && endPosition.getRow() == startPosition.getRow();
        } else {
            if (endPosition.getRow() == startPosition.getRow()) {
                return endPosition.getColumn() == 3 || endPosition.getColumn() == 7;
            }
        }
        return false;
    }

    private boolean isValidCastleRook(ChessPosition startPosition){
        ChessPiece piece = playing_board.getPiece(startPosition);

        return piece != null && piece.getPieceType().equals(ChessPiece.PieceType.ROOK) && !piece.get_has_moved();
    }

    private boolean isValidCastleKing(ChessPosition startPosition){
        ChessPiece piece = playing_board.getPiece(startPosition);

        return piece != null && piece.getPieceType().equals(ChessPiece.PieceType.KING) && !piece.get_has_moved();
    }


    private boolean check_Castling(ChessPosition currentPosition){
        if (isValidCastleKing(currentPosition)){
            ChessPosition rightRook = new ChessPosition(currentPosition.getRow(), 8);
            ChessPosition leftRook = new ChessPosition(currentPosition.getRow(), 1);

            if(isValidCastleRook(rightRook) || isValidCastleRook(leftRook)){
                return nothingBetween(currentPosition,rightRook) || nothingBetween(currentPosition,leftRook);
            }
        } else if (isValidCastleRook(currentPosition)){
            ChessPosition king = new ChessPosition(currentPosition.getRow(), 5);
            if(isValidCastleKing(king)){
                return nothingBetween(currentPosition,king);
            }
        }

        return false;
    }

    private boolean nothingBetween(ChessPosition startPosition, ChessPosition endPosition){
        if(startPosition.getColumn()>endPosition.getColumn()){
            for(int i=startPosition.getColumn()-1; i>endPosition.getColumn();i--){
                if(playing_board.getPiece(new ChessPosition(startPosition.getRow(), i))!=null){
                    return false;
                }
            }
        } else {
            for(int i=startPosition.getColumn()+1; i<endPosition.getColumn();i++){
                if(playing_board.getPiece(new ChessPosition(startPosition.getRow(), i))!=null){
                    return false;
                }
            }
        }
        return true;
    }

    private Collection<ChessMove> castleMove(TeamColor teamColor, ChessPosition startPosition){
        Collection<ChessMove> possible_moves = new ArrayList<ChessMove>();

        if (isValidCastleKing(startPosition)){
            ChessPosition rightRook = new ChessPosition(startPosition.getRow(), 8);
            ChessPosition leftRook = new ChessPosition(startPosition.getRow(), 1);

            if(isValidCastleRook(rightRook) && nothingBetween(startPosition, rightRook)){
                possible_moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow(), 7), null));
            }
            if(isValidCastleRook(leftRook) && nothingBetween(startPosition, leftRook)){
                possible_moves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow(),3), null));
            }
        } else if (isValidCastleRook(startPosition)){
            if(startPosition.getColumn()<5){
                possible_moves.add(new ChessMove(startPosition,new ChessPosition(startPosition.getRow(), 4),null));
            } else {
                possible_moves.add(new ChessMove(startPosition,new ChessPosition(startPosition.getRow(), 6),null));
            }

        }

        return possible_moves;
    }

else if (moveIsCastling(move)){
                ChessPiece piece = playing_board.getPiece(move.getStartPosition());
                ChessPiece save_piece = playing_board.getPiece(move.getEndPosition());

                if(isValidCastleKing(move.getStartPosition())){
                    if(move.getEndPosition().getColumn()>5){
                        ChessPosition rookPosition = new ChessPosition(move.getStartPosition().getRow(),8);
                        ChessPiece rook = playing_board.getPiece(rookPosition);
                        playing_board.addPiece(rookPosition,null);

                        playing_board.addPiece(new ChessPosition(move.getStartPosition().getRow(),6),rook);
                        playing_board.getPiece(new ChessPosition(move.getStartPosition().getRow(),6)).setHas_moved(true);
                    } else {
                        ChessPosition rookPosition = new ChessPosition(move.getStartPosition().getRow(),1);
                        ChessPiece rook = playing_board.getPiece(rookPosition);
                        playing_board.addPiece(rookPosition,null);

                        playing_board.addPiece(new ChessPosition(move.getStartPosition().getRow(),4),rook);
                        playing_board.getPiece(new ChessPosition(move.getStartPosition().getRow(),4)).setHas_moved(true);
                    }
                } else {
                    ChessPosition kingPosition = new ChessPosition(move.getStartPosition().getRow(),5);
                    ChessPiece king = playing_board.getPiece(kingPosition);
                    playing_board.addPiece(kingPosition,null);

                    if(move.getEndPosition().getColumn()>5){
                        playing_board.addPiece(new ChessPosition(move.getStartPosition().getRow(),7),king);
                        playing_board.getPiece(new ChessPosition(move.getStartPosition().getRow(),7)).setHas_moved(true);
                    }else{
                        playing_board.addPiece(new ChessPosition(move.getStartPosition().getRow(),3),king);
                        playing_board.getPiece(new ChessPosition(move.getStartPosition().getRow(),3)).setHas_moved(true);
                    }
                }
                playing_board.addPiece(move.getStartPosition(), null);
                playing_board.addPiece(move.getEndPosition(), piece);



     */
}
