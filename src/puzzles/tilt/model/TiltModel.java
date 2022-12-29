package puzzles.tilt.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class TiltModel {
    /** the collection of observers of this model */
    private final List<Observer<TiltModel, String>> observers = new LinkedList<>();
    /** the current configuration */
    private TiltConfig currentConfig;
    private File currentFile;
    public char[][] board;
    private int moves;
    /**
     * The view calls this to add itself as an observer.
     * @param observer the view
     */
    public void addObserver(Observer<TiltModel, String> observer) {
        observers.add(observer);
    }
    public TiltModel(String filepath){
        loadBoardFromFile(new File(filepath), "Game Begun");
    }
    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(String data) {
        for (var observer : observers) {
            observer.update(this, data);
        }
    }

    /**
     * Simple Getter
     * @return board size
     */
    public int getDimension(){return board.length;}
    /**
     * Loads the inputted board into the model
     * @param file File given
     * @param message Message about file
     */
    public void loadBoardFromFile(File file, String message) {
        moves = 0;
        try{
            Scanner in = new Scanner(file);
            int DIM = Integer.parseInt(in.nextLine());
            board = new char[DIM][DIM];

            for (int row = 0; row < DIM; row++) {
                String line = in.nextLine();
                String[] thing = line.split(" ");
                for (int col = 0; col < DIM; col++){

                    board[row][col] = thing[col].charAt(0);
                }
            }
            moves = 0;
            currentFile = file;
            currentConfig = new TiltConfig(board);
        } catch (FileNotFoundException e) {
            message = "File Not Loaded";
        }
        alertObservers(message);
    }

    /**
     * Tilts model to the left
     */
    public void goLeft(){
        moves++;
        char[][] lefty = currentConfig.moveLeft();
        if (lefty == null){
            alertObservers("Watch out for the blue slider! Sorry :(");
            reset();
            return;}

        currentConfig = new TiltConfig(lefty, currentConfig,'W');
        board = currentConfig.getOGrid();
        if (currentConfig.isSolution()){alertObservers("You Won!");}
        else if (currentConfig.currentBees() < currentConfig.getNumBlue()) {
            alertObservers("Watch out for the blue slider! Sorry :(");
            reset();}else{
            alertObservers("Moved Left");
        }}

    /**
     * Tilts model to the right
     */
    public void goRight(){
        moves++;
        char[][] righty = currentConfig.moveRight();
        if (righty == null){
            alertObservers("Watch out for the blue slider! Sorry :(");
            reset();
            return;}

        currentConfig = new TiltConfig(righty, currentConfig,'E');
        board = currentConfig.getOGrid();
        if (currentConfig.isSolution()){alertObservers("You Won!");}
        else if (currentConfig.currentBees() < currentConfig.getNumBlue()) {
            alertObservers("Watch out for the blue slider! Sorry :(");
            reset();}else{
            alertObservers("Moved Right");
        }}

    /**
     * Tilts model up
     */
    public void goUp(){
        moves++;
        char[][] uppie = currentConfig.moveUP();
        if (uppie == null){
            alertObservers("Watch out for the blue slider! Sorry :(");
            reset();
            return;}

        currentConfig = new TiltConfig(uppie, currentConfig,'N');
        board = currentConfig.getOGrid();
        if (currentConfig.isSolution()){alertObservers("You Won!");}
        else if (currentConfig.currentBees() < currentConfig.getNumBlue()) {
            alertObservers("Watch out for the blue slider! Sorry :(");
            reset();}else{
            alertObservers("Moved Up");
        }}

    /**
     * Tilts model down
     */
    public void goDown(){
        moves++;
        char[][] downer = currentConfig.moveDown();
        if (downer == null){
            alertObservers("Watch out for the blue slider! Sorry :(");
            reset();
            return;}

        currentConfig = new TiltConfig(downer, currentConfig,'S');
        board = currentConfig.getOGrid();
        if (currentConfig.isSolution()){alertObservers("You Won!");}
        else if (currentConfig.currentBees() < currentConfig.getNumBlue()) {
            alertObservers("Watch out for the blue slider! Sorry :(");
            reset();}else{
            alertObservers("Moved Down");
        }}

    /**
     * Resets board by loading original file, sends message aswell
     */
    public void reset(){loadBoardFromFile(currentFile,"Game reset");}

    /**
     * Gets next step to solve for current board
     */
    public void getHint() {
        Solver solve = new Solver();
        ArrayList<Configuration> hint = (ArrayList<Configuration>) solve.solve(new TiltConfig(board));

        if (hint != null){
            TiltConfig tint = (TiltConfig) hint.get(1);
            currentConfig = tint;
            board = currentConfig.getOGrid();

            alertObservers("Hint: "+tint.getPath().get(1));
            if (currentConfig.isSolution()){alertObservers("You Won!");}
        }else{
            alertObservers("No hint found");
        }
    }

    /**
     * Simple getter
     * @return current moves
     */
    public int getMoves() {return moves;}

    /**
     * gets board value at index
     * @param row row of choice
     * @param col column of choice
     * @return returns value of index at board
     */
    public char getBoard(int row, int col) {return currentConfig.getOGrid()[row][col];}
}
