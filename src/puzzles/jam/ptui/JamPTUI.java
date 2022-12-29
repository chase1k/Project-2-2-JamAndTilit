package puzzles.jam.ptui;

import puzzles.common.Observer;
import puzzles.jam.model.JamConfig;
import puzzles.jam.model.JamModel;

import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

/**
 * The PTUI for the Jam game
 * @author Owen
 */
public class JamPTUI implements Observer<JamModel, String> {
    private JamModel model;

    private String currentfile;

    private Scanner inputScanner;

    private boolean running;
    private String[] currentfilename;


    /**
     * Changes the puti and displays the change depending on the message
     * @param jamModel the object that wishes to inform this object
     *                about something that has happened.
     * @param message optional data the server.model can send to the observer
     *
     */
    @Override
    public void update(JamModel jamModel, String message) {

        switch (message) {
            case JamModel.LOADED -> {
                currentfile = model.getLastfile();
                currentfilename = currentfile.split("/");
                System.out.println("Loaded: " + currentfilename[currentfilename.length - 1]);
                displayBoard();
            }
            case JamModel.LOADED_FAILED -> {
                System.out.println("Failed to load: " + model.getLastfile());
                displayBoard();
            }
            case JamModel.RESET -> {
                currentfilename = currentfile.split("/");
                System.out.println("Loaded: " + currentfilename[currentfilename.length - 1]);
                displayBoard();
                System.out.println("Puzzle reset!");
                displayBoard();
            }
            case JamModel.SELECTIONOUTOFBOUNDS -> {
                System.out.println("Selection out of bounds");
                displayBoard();
            }
            case JamModel.SELECTED -> {
                System.out.println("Selected (" + model.getSelection()[0] + ", " + model.getSelection()[1] + ")");
                displayBoard();
            }
            case JamModel.NOCAR -> {
                System.out.println("No car at (" + model.getSelection()[0] + ", " + model.getSelection()[1] + ")");
                displayBoard();
            }
            case JamModel.MOVED -> {
                System.out.print("Moved from (" + model.getSelection()[0] + ", " + model.getSelection()[1] + ")");
                System.out.println(" to (" + model.getSelectedto()[0] + ", " + model.getSelectedto()[1] + ")");
                displayBoard();
            }
            case JamModel.CANTMOVE -> {
                System.out.print("Can't move from (" + model.getSelection()[0] + ", " + model.getSelection()[1] + ")");
                System.out.println(" to (" + model.getSelectedto()[0] + ", " + model.getSelectedto()[1] + ")");
                displayBoard();
            }
            case JamModel.HINT -> {
                System.out.println("Next step!");
                displayBoard();
            }
            case JamModel.NOSOLUTION -> {
                System.out.println("No solution");
                displayBoard();
            }
            case JamModel.SOLVED -> {
                System.out.println(JamModel.SOLVED);
                displayBoard();
            }
        }
    }

    /**
     * recevies and parses inputs to change the model
     */
    public void gameLoop() {
        while (running) {
            System.out.print("> ");
            String input = inputScanner.nextLine();
            String[] inputs = input.split(" ");
            if (inputs.length == 1) {
                if (input.startsWith("q")) {
                    break; //TODO might be a quit thingy have to call
                } else if (input.startsWith("r")) {
                    model.loadBoardFromFile(currentfile);
                } else if (input.startsWith("h")) {
                    model.getHint();
                } else {
                    displaymoves();
                    continue;
                }
            }
            else{
                if(inputs[0].startsWith("l")) {
                    model.loadBoardFromFile(inputs[1]);

                } else if (inputs[0].startsWith("s")) {
                    if(inputs.length == 3){
                    model.select(Integer.parseInt(inputs[1]), Integer.parseInt(inputs[2]));}
                    else{
                        displaymoves();
                        continue;
                    }
                }
                else{
                    displaymoves();
                }
            }
        }
    }

    /**
     * Constructor for Jam Ptui
     */
    public JamPTUI(){
        this.model = new JamModel();
        model.addObserver(this);
        this.inputScanner = new Scanner(System.in);
        this.running = true;
    }


    /**
     * Loads the board from the file
     * @param file the file to load from
     */
    public void LoadBoard(String file){
        if(model.loadBoardFromFile(file)){
            this.currentfile = file;
            displaymoves();
        }

        gameLoop();
    }


    /**
     * initializes the Ptui
     * @param args the file to initialize
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Jam PTUI filename");
        }
        else{
            JamPTUI ui = new JamPTUI();
            ui.LoadBoard(args[0]);
        }
    }

    /**
     * Displays the board
     */
    public void displayBoard(){
        System.out.print("   ");
        for (int r = 0; r < this.model.getNumcols(); r++) {
            System.out.print(r + " ");
        }
        System.out.println(" ");

        System.out.print("  ");
        for (int r = 0; r < this.model.getNumcols(); r++) {
            System.out.print("--");
        }
        System.out.println(" ");

        for (int r = 0; r < this.model.getNumrows(); r++) {
            System.out.print(r + "| ");
            for (int c = 0; c < this.model.getNumcols(); c++) {
                System.out.print(this.model.board.getBoard(r,c) + " ");
            }
            System.out.println(" ");
        }
        System.out.println(" ");
    }

    /**
     * Displays the moves the user could make
     */
    private static void displaymoves(){
        System.out.println("h(int)              -- hint next move");
        System.out.println("l(oad) filename     -- load new puzzle file");
        System.out.println("s(elect) r c        -- select cell at r, c");
        System.out.println("q(uit)              -- quit the game");
        System.out.println("r(eset)             -- reset the current game");
    }


}
