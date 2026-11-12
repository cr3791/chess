package ui;

import java.util.List;

public class PreLoginUI {

    public PreLoginUI(){}

    public void eval(List<String> parameters){
        switch (parameters.getFirst()){
            case "help":
                help();
                break;
            case "quit":
                quit();
                break;
            case "login":
                logIn();
                break;
            case "register":
                register();
                break;
        }
    }

    private void help(){}

    private void quit(){
    }

    private void logIn(){
    }

    private void register(){
    }

}
