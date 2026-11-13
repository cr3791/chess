package ui;

import model.UserData;

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
            case "createGame":
                System.out.println(createGame());
                break;
            case "listGames":
                System.out.println(listGames());
                break;
            case "playGame":
                System.out.println(playGame());
                state.setGamePlay();
                break;
            case "observeGame":
                System.out.println(observeGame());
                state.setGamePlay();
                break;
            default:
                System.out.println("That command is not recognized. Try typing 'Help' to see valid commands\n");
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
        String quit = "\n\tquit - end program";
        return String.format("%s%s%s%s%s%s%s\n\n",create,list,join,observe,logout,help,quit);
    }

    private State logOut(){
        try {
            facade.logOut();
            String message = String.format("\n%s has been successfully logged out\n",state.getUsername());
            state.setMessage(message);
            state.setLogOut();
            return state;
        } catch (Throwable ex){
            String message = String.format("\n%s",ex.getMessage());
            state.setMessage(message);
        }
        return state;
    }

    private String createGame(){
        return "";
    }

    private String listGames(){
        return "";
    }

    private String playGame(){
        return "";
    }

    private String observeGame(){
        return "";
    }
}
