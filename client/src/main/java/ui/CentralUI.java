package ui;


import java.util.Scanner;

public class CentralUI {
    private PostLoginUI postUI;
    private PreLoginUI preUI;
    private GamePlayUI gpUI;
    private State state;

    public CentralUI(){
        postUI = new PostLoginUI();
        preUI = new PreLoginUI("");
        gpUI = new GamePlayUI();
        state.setLogOut();
    }

    public void run(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Welcome to 240 Chess, future Grandmaster. Type Help to get started\n\n");
        var result = "";

        while(true){
            System.out.printf("{%s} >>> ",state.getState());
            String line = scanner.nextLine();

            try{
                eval(line);
            } catch (RuntimeException ex){
                System.out.print("You Failure");
            }
        }
    }

    private void eval(String line){
        switch (state.getState()){
            case "Logged In":

            case "Logged Out":
        }
    }


}
