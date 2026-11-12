package ui;

import model.UserData;

import java.util.List;

public class PreLoginUI {
    private ServerFacade facade;
    private String serverURL;
    public PreLoginUI(ServerFacade facade){
        this.facade = facade;
    }

    public State eval(List<String> parameters, State currentState){
        State state = currentState;
        switch (parameters.getFirst()){
            case "help":
                System.out.print(help());
                break;
            case "quit":
                System.out.print(quit());
                state.quit();
                break;
            case "login":
                logIn();
                state.setLogIn();
                break;
            case "register":
                System.out.println(register(parameters));
                state.setLogIn();
                break;
            default:
                System.out.println("That command is not recognized. Try typing 'Help' to see valid commands\n");
        }
        return state;
    }

    private String help(){
        String registerInfo = "\tregister <USERNAME> <PASSWORD> <EMAIL> - create an account";
        String loginInfo = "\tlogin <USERNAME> <PASSWORD> - sign in as existing user";
        String helpInfo = "\thelp - see possible commands";
        String quitInfo = "\tquit - end program";
        return String.format("\n%s\n%s\n%s\n%s\n\n",registerInfo,loginInfo,helpInfo,quitInfo);
    }

    private String quit(){
        return "\nThank you for using 240 Chess!\n";
    }

    private void logIn(){

    }

    private String register(List<String> parameters){
        String username = parameters.get(1);
        String password = parameters.get(2);
        String email = parameters.get(3);

        UserData userData = new UserData(username, password, email);

        facade.register(userData);

        return String.format("\nYou have successfully registered as %s\n",username);
    }

}
