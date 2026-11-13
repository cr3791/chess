package ui;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CentralUI {
    private ServerFacade facade;
    private PostLoginUI postUI;
    private PreLoginUI preUI;
    private GamePlayUI gpUI;
    private State state;

    public CentralUI(){
        facade = new ServerFacade();
        postUI = new PostLoginUI(facade);
        preUI = new PreLoginUI(facade);
        gpUI = new GamePlayUI(facade);
        state = new State();
    }

    public void run(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Welcome to 240 Chess, future Grandmaster. Type Help to get started\n\n");

        while(true){
            System.out.printf("{%s} >>> ",state.getState());
            String line = scanner.nextLine();

            try{
                eval(line);
                if(state.isQuit()){
                    break;
                }
            } catch (RuntimeException ex){
                System.out.print("You Failure");
            }
        }
    }

    private void eval(String line){
        List<String> input = new ArrayList<>();
        input.addAll(Arrays.asList(line.toLowerCase().split(" ")));
        if (input.isEmpty()){
            input.addFirst("help");
        }
        switch (state.getState()){
            case "Logged In":
                state = postUI.eval(input, state);
            case "Logged Out":
                state = preUI.eval(input, state);
            case "In Game":
                state = gpUI.eval(input, state);
        }
    }
}
