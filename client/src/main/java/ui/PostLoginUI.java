package ui;

import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.List;

public class PostLoginUI {
    private final ServerFacade facade;
    private State state;

    public PostLoginUI(ServerFacade facade){
        this.facade = facade;
        this.state = new State();
    }

    public State eval(List<String> parameters, State currentState){
        this.state = currentState;
        switch (parameters.getFirst()){
            case "help":
                state.setMessage(help());
                break;
            case "logout":
                state = logOut();
                break;
            case "create":
                createGame(parameters);
                break;
            case "list":
                listGames();
                break;
            case "join":
                System.out.println(playGame());
                state.setGamePlay();
                break;
            case "observe":
                System.out.println(observeGame());
                state.setGamePlay();
                break;
            default:
                System.out.println("That command is not recognized. Try typing 'Help' to see valid commands\n\n");
        }
        return state;
    }

    private String help(){
        String create = "\n\tcreate <GAME NAME> - create an new game";
        String list = "\n\tlist - see all games";
        String join = "\n\tjoin <GAME ID> [WHITE|BLACK] - join an new game as white or black";
        String observe = "\n\tobserve <GAME ID> - watch chess match without playing";
        String logout = "\n\tlogout - sign out from account";
        String help = "\n\thelp - see possible commands";
        return String.format("%s%s%s%s%s%s\n\n",create,list,join,observe,logout,help);
    }

    private State logOut(){
        try {
            facade.logOut();
            String message = String.format("\n%s has been successfully logged out\n\n",state.getUsername());
            state.setMessage(message);
            state.setLogOut();
            return state;
        } catch (Throwable ex){
            String message = String.format("\n%s",ex.getMessage());
            state.setMessage(message);
        }
        return state;
    }

    private void createGame(List<String> parameters){
        try{
            String gameName = "";
            for(int i = 1; i<parameters.size(); i++){
                gameName += parameters.get(i);
                if (i!=parameters.size()-1){
                    gameName = gameName + " ";
                }
            }
            int gameID = facade.createGame(gameName);
            String message = String.format("\nYou have successfully created a game!\n\tHere is your game ID: %d\n\n",gameID);
            state.setMessage(message);
        } catch (IndexOutOfBoundsException ex){
            state.setMessage("You have not entered in the correct number of inputs. Please try again!");
        } catch (Throwable ex){
            String message = String.format("\n%s",ex.getMessage());
            state.setMessage(message);
        }
    }

    private void listGames(){
        try {
            ArrayList<GameData> games = facade.getGameList();
            String message = "\nHere is the list of games!\n\nGame List:";
            StringBuilder gameList = new StringBuilder();
            if (!games.isEmpty()) {
                for (GameData game : games) {
                    gameList.append(String.format("\n\tNAME: %s (ID: %d) --> ", game.gameName(), game.gameID()));
                    gameList.append(String.format("White Player: %s | ", game.whiteUsername()));
                    gameList.append(String.format("Black Player: %s", game.blackUsername()));
                }
            } else {
                gameList.append("\n\tThere are no active games currently. Use 'create' to add one!");
            }
            gameList.append("\n\n");
            message += gameList.toString();
            state.setMessage(message);
        } catch (Throwable ex){
            String message = String.format("\n%s",ex.getMessage());
            state.setMessage(message);
        }
    }

    private String playGame(){
        return "";
    }

    private String observeGame(){
        return "";
    }
}
