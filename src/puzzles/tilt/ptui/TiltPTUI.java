package puzzles.tilt.ptui;

import puzzles.common.Observer;
import puzzles.tilt.model.TiltModel;

import java.io.File;
import java.util.Scanner;

/**
 * TiltPTUI class
 */
public class TiltPTUI implements Observer<TiltModel, String> {
    private TiltModel model;

    /**
     * Updates the output based on the input
     * @param model the object that wishes to inform this object
     *                about something that has happened.
     * @param message optional data the server.model can send to the observer
     *
     */
    @Override
    public void update(TiltModel model, String message) {
        if (message.equals("You Won!")){
            System.out.println("Game Won");
            System.exit(0);
        }displayBoard();

        System.out.println("""
                    h(int)              -- hint next move
                    l(oad) filename     -- load new puzzle file
                    t(ilt) {N|S|E|W}    -- tilt the board in the given direction
                    q(uit)              -- quit the game
                    r(eset)             -- reset the current game""");
        System.out.println("Moves: "+model.getMoves()+" Message: "+message);
    }

    /**
     * Prints out board
     */
    private void displayBoard() {
        for (int row = 0; row < model.getDimension(); row++) {
            for (int col = 0; col < model.getDimension(); col++) {
                System.out.print(model.getBoard(row,col)+" ");
            }System.out.println();}
        System.out.println();}

    /**
     * Main for PTUI
     * @param args input file
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TiltPTUI filename");
        }
        TiltPTUI tTUI = new TiltPTUI();
        tTUI.run(args[0]);
    }

    /**
     * Runs the game
     * @param filename name of file
     */
    private void run(String filename) {
        model = new TiltModel(filename);
        model.addObserver(this);
        gameLoop();
    }

    /**
     * Loops the game
     */
    private void gameLoop() {
        boolean gaming = true;
        this.update(model, "Game started");
        Scanner in = new Scanner(System.in);
        while (gaming) {

            String command = in.nextLine();
            String cmd = String.valueOf(command.charAt(0));
            if (command.equals("Q") || command.equals("q")) {
                System.out.println("Quitting");
                gaming = false;
                return;
            } else if (command.equals("R") || command.equals("r")) {
                model.reset();
            } else if (command.equals("H") || command.equals("h")) {
                model.getHint();
            } else if (cmd.equals("L") || cmd.equals("l")) {
                if (command.length() >2) {
                    model.loadBoardFromFile(new File(command.substring(2)), "File loaded");
                }
            }else if (cmd.equals("T") || cmd.equals("t")) {
                if(command.length() > 2) {
                    if (command.charAt(2) == 'W' || command.charAt(2) == 'w') {
                        model.goLeft();
                    } else if (command.charAt(2) == 'E' || command.charAt(2) == 'e') {
                        model.goRight();
                    } else if (command.charAt(2) == 'N' || command.charAt(2) == 'n') {
                        model.goUp();
                    } else if (command.charAt(2) == 'S' || command.charAt(2) == 's') {
                        model.goDown();
                    }
                }else {System.out.println("Command Incorrect");}
            }else {System.out.println("Command Incorrect");}
        }
    }
}
