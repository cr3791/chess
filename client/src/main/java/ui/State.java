package ui;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;

public class State {
    private String state;
    private boolean loggedIn;
    private boolean quit;
    private String username;
    private String message;
    private GameData currentGame;

    public State(){
        state = "Logged Out";
        loggedIn = false;
        quit = false;
        message = "";
        username = null;
        currentGame = null;
    }

    public String getState(){
        return state;
    }

    public void setLogIn(String username){
        this.username = username;
        loggedIn = true;
        state = "Logged In";
        currentGame = null;
    }

    public void setLogOut(){
        loggedIn = false;
        username = null;
        state = "Logged Out";
        currentGame = null;
    }

    public void setGamePlay(){
        state = "In Game";
    }

    public void quit(){
        quit = true;
    }

    public boolean isQuit(){
        return quit;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }

    public String getUsername(){
        return username;
    }

    public void setGame(GameData game){
        this.currentGame = game;
    }

    public GameData getGameData(){
        return currentGame;
    }

    public String drawGame(){
        ChessBoard board = currentGame.game().getBoard();
        String[][] drawBoard = new String[10][10];

        for(int row = 0; row < 10; row++){
            for(int col = 0; col < 10; col++){
                if(row == 0 | row == 9 | col == 0 | col == 9){
                    drawBoard[row][col] = addBorder(row,col);
                } else if(board.getPiece(new ChessPosition(row,col))!= null){
                    drawBoard[row][col] = addPiece(row,col,board);
                } else {
                    drawBoard[row][col] = addBackground(row,col);
                }
            }
        }

        String color = checkTeam();
        if(color.equals("BLACK")){
            drawBoard = flipBoard(drawBoard);
        }
        return buildBoard(drawBoard);
    }

    private String[][] flipBoard(String[][] boardPieces){
        for(int row = 0; row < 5; row++){
            for(int col = 0; col < 10; col++){
                String save = boardPieces[row][col];
                boardPieces[row][col] = boardPieces[9-row][9-col];
                boardPieces[9-row][9-col] = save;
            }
        }
        return boardPieces;
    }

    private String buildBoard(String[][] boardPieces){
        StringBuilder build = new StringBuilder();
        for(int i = 9; i >=0; i--){
            for(int j = 0; j<10; j++){
                build.append(boardPieces[i][j]);
                if(j == 9){
                    build.append(String.format("%s\n",EscapeSequences.RESET_BG_COLOR));
                }
            }
        }
        return build + "\n" + EscapeSequences.RESET_TEXT_COLOR + EscapeSequences.RESET_TEXT_BOLD_FAINT;
    }

    private String addBorder(int row, int col){
        String square = "";
        if (row == 0 | row == 9){
            switch (col){
                case 1:
                    square = " a ";
                    break;
                case 2:
                    square = " b ";
                    break;
                case 3:
                    square = " c ";
                    break;
                case 4:
                    square = " d ";
                    break;
                case 5:
                    square = " e ";
                    break;
                case 6:
                    square = " f ";
                    break;
                case 7:
                    square = " g ";
                    break;
                case 8:
                    square = " h ";
                    break;
                default:
                    square = String.format(EscapeSequences.EMPTY);
                    break;
            }
        } else {
            switch (row) {
                case 1:
                    square = " 1 ";
                    break;
                case 2:
                    square = " 2 ";
                    break;
                case 3:
                    square = " 3 ";
                    break;
                case 4:
                    square = " 4 ";
                    break;
                case 5:
                    square = " 5 ";
                    break;
                case 6:
                    square = " 6 ";
                    break;
                case 7:
                    square = " 7 ";
                    break;
                case 8:
                    square = " 8 ";
                    break;
                default:
                    square = String.format(EscapeSequences.EMPTY);
                    break;
            }
        }
        return String.format("%s%s%s",EscapeSequences.SET_BG_MAROON,EscapeSequences.SET_TEXT_COLOR_BLACK,square);
    }

    private String addPiece(int row, int col, ChessBoard board){
        ChessPiece piece = board.getPiece(new ChessPosition(row,col));
        String square = "";
        switch(piece.getTeamColor()){
            case BLACK:
                square = getBlackPiece(piece.getPieceType());
                square = String.format("%s%s",EscapeSequences.SET_TEXT_COLOR_BLACK,square);
                break;
            case WHITE:
                square = getWhitePiece(piece.getPieceType());
                square = String.format("%s%s",EscapeSequences.SET_TEXT_COLOR_WHITE,square);
                break;
        }

        if((row+col)%2==0){
            square = String.format("%s%s",EscapeSequences.SET_BG_COLOR_DARK_GREY,square);
        } else {
            square = String.format("%s%s",EscapeSequences.SET_BG_COLOR_LIGHT_GREY,square);
        }
        return square;
    }

    private String addBackground(int row, int col){
        String square = EscapeSequences.EMPTY;
        if((row+col)%2==0){
            square = String.format("%s%s",EscapeSequences.SET_BG_COLOR_DARK_GREY,square);
        } else {
            square = String.format("%s%s",EscapeSequences.SET_BG_COLOR_LIGHT_GREY,square);
        }
        return square;
    }

    private String getBlackPiece(ChessPiece.PieceType type){
        return switch (type) {
            case KING -> EscapeSequences.BLACK_KING;
            case QUEEN -> EscapeSequences.BLACK_QUEEN;
            case BISHOP -> EscapeSequences.BLACK_BISHOP;
            case KNIGHT -> EscapeSequences.BLACK_KNIGHT;
            case ROOK -> EscapeSequences.BLACK_ROOK;
            case PAWN -> EscapeSequences.BLACK_PAWN;
        };
    }

    private String getWhitePiece(ChessPiece.PieceType type){
        return switch (type) {
            case KING -> EscapeSequences.WHITE_KING;
            case QUEEN -> EscapeSequences.WHITE_QUEEN;
            case BISHOP -> EscapeSequences.WHITE_BISHOP;
            case KNIGHT -> EscapeSequences.WHITE_KNIGHT;
            case ROOK -> EscapeSequences.WHITE_ROOK;
            case PAWN -> EscapeSequences.WHITE_PAWN;
        };
    }

    private String checkTeam(){
        if(username.equals(currentGame.blackUsername())) {
            return "BLACK";
        } else if (username.equals(currentGame.whiteUsername())){
            return "WHITE";
        }
        return "";
    }
}
