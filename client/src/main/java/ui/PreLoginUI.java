package ui;

import model.AuthData;
import model.UserData;

import java.io.IOException;
import java.util.List;

public class PreLoginUI {
    private final ServerFacade facade;
    private State state;
    public PreLoginUI(ServerFacade facade){
        this.facade = facade;
        this.state = new State();
    }

    public State eval(List<String> parameters, State currentState){
        this.state = currentState;
        switch (parameters.getFirst()){
            case "help":
                state.setMessage(help());
                break;
            case "quit":
                state.setMessage(quit());
                state.quit();
                break;
            case "login":
                state = logIn(parameters);
                break;
            case "register":
                state = register(parameters);
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

    private State logIn(List<String> parameters){
        try {
            String username = parameters.get(1);
            String password = parameters.get(2);

            UserData userData = new UserData(username, password, null);

            String usernameReturn = facade.logIn(userData);
            state.setLogIn(usernameReturn);
            String message = String.format("\nYou have successfully logged in as %s\n", username);
            state.setMessage(message);
        } catch (IndexOutOfBoundsException ex){
            state.setMessage("You have not entered in the correct number of inputs. Please try again!");
        } catch (IOException | InterruptedException ex){
            state.setMessage("Connecting to the server failed.");
        } catch (Throwable ex){
            String message = String.format("\n%s",ex.getMessage());
            state.setMessage(message);
        }
        return state;
    }

    private State register(List<String> parameters){
        try {
            String username = parameters.get(1);
            String password = parameters.get(2);
            String email = parameters.get(3);

            UserData userData = new UserData(username, password, email);

            String usernameReturn = facade.register(userData);
            state.setLogIn(usernameReturn);
            String message = String.format("\nYou have successfully registered as %s\n", username);
            state.setMessage(message);
        } catch (IndexOutOfBoundsException ex){
            state.setMessage("You have not entered in the correct number of inputs. Please try again!");
        } catch (IOException | InterruptedException ex){
            state.setMessage("Connecting to the server failed");
        } catch (Throwable ex){
            String message = String.format("\n%s",ex.getMessage());
            state.setMessage(message);
        }
        return state;
    }

}
