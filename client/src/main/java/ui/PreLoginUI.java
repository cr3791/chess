package ui;

import java.util.List;

public class PreLoginUI {

    public PreLoginUI(){}

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
        }
        return state;
    }

    private String help(){
        String registerInfo = "\tregister <USERNAME> <PASSWORD> <EMAIL> - create an account";
        String loginInfo = "\tlogin <USERNAME> <PASSWORD> - sign in as existing user";
        String helpInfo = "\thelp - see possible commands";
        String quitInfo = "\tquit - end program";
        return String.format("%s\n%s\n%s\n%s\n",registerInfo,loginInfo,helpInfo,quitInfo);
    }

    private String quit(){
        return "Thank you for playing!\n";
    }

    private void logIn(){

    }

    private String register(List<String> parameters){


        return "";
    }

}
