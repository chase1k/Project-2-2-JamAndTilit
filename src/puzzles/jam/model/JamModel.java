package puzzles.jam.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class JamModel {
    public static final String LOADED = "loaded";
    public static final String LOADED_FAILED = "loaded_failed";
    public static final String RESET = "reset";
    public static final String SELECTIONOUTOFBOUNDS = "outofbounds";
    public static final String SELECTED = "selected";
    public static final String NOCAR = "no car";
    public static final String MOVED = "Moved";
    public static final String CANTMOVE = "CANTMOVE";
    public static final String HINT = "got hint";
    public static final String NOSOLUTION = "no solution";
    public static final String SOLVED = "Already solved!";




    /** the collection of observers of this model */
    private final List<Observer<JamModel, String>> observers = new LinkedList<>();

    public Board board;

    private ArrayList<Car> cars = new  ArrayList<>();

    private String lastfile;

    private int numcols;
    private int numrows;

    private int[] selection = null;

    private int[] selectedto = null;

    private boolean validselected = false;

    private ArrayList<Configuration> solution;

    private boolean hasMoved = true;

    public JamModel(){
    }

    public String getLastfile() {
        return lastfile;
    }

    public boolean loadBoardFromFile(String filename){
        String msg;
        if(filename.equals(this.lastfile)){
            msg = RESET;
        }
        else{
            msg = LOADED;
        }
        this.lastfile = filename;
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            this.cars = new  ArrayList<>();
            String[] splitsize = reader.readLine().split(" ");

            this.numrows = Integer.parseInt(splitsize[0]);
            this.hasMoved = true;
            this.numcols = Integer.parseInt(splitsize[1]);
            int numcars = Integer.parseInt(reader.readLine());
            for (int i = 0; i < numcars; i++) {
                String[] cardata = reader.readLine().split(" ");
                char Carletter = cardata[0].charAt(0);
                int[] startingcord = {Integer.parseInt(cardata[1]), Integer.parseInt(cardata[2])};
                int[] endingcord = {Integer.parseInt(cardata[3]), Integer.parseInt(cardata[4])};
                boolean isHorizontal = Integer.parseInt(cardata[2]) != Integer.parseInt(cardata[4]);
                this.cars.add(new Car(startingcord, endingcord, isHorizontal, Carletter));
            }
            this.board = new Board(numcols, numrows, this.cars);
            alertObservers(msg);
            return true;
        }
        catch (IOException e){
            alertObservers(LOADED_FAILED);
            return false;
        }

    }


    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<JamModel, String> observer) {
        this.observers.add(observer);
    }

    public void select(int r, int c){
        if((r >= 0 && r < getNumrows()) && (c >= 0 && c < getNumcols()) ){
            if(!validselected){
                selection = new int[]{r, c};
                if(board.getBoard(r, c) != '.'){

                    validselected = true;
                    alertObservers(SELECTED);
                }
                else{
                alertObservers(NOCAR);
                }

            }
            else{
                move(new int[]{r, c});
            }
        }
        else{
            alertObservers(SELECTIONOUTOFBOUNDS);
        }

    }
    public void move(int[] second){
        selectedto = second;
        if(board.move(selection[0], selection[1], selectedto[0], selectedto[1])){
            alertObservers(MOVED);
            validselected = false;
            hasMoved = true;
        }
        else{
            validselected = false;
            alertObservers(CANTMOVE);

        }
    }

    public void getHint(){
        if(this.board.isSolution()){
            alertObservers(SOLVED);
            return;
        }
        if(hasMoved) {
            JamConfig config = new JamConfig(this.cars);
            JamConfig.setNumRows(numrows);
            JamConfig.setNumCols(numcols);
            Solver solver = new Solver();
            this.solution = (ArrayList<Configuration>) solver.solve(config);
            if(this.solution.size() == 0){
                alertObservers(NOSOLUTION);
            }
            this.solution.remove(0);
        }
        hasMoved = false;
        JamConfig nextstep = ( JamConfig)this.solution.get(0);
        this.board = new Board(this.numcols, this.numrows, nextstep.getCars());
        this.cars = nextstep.getCars();
        this.solution.remove(0);
        alertObservers(HINT);


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
     *
     * @return the number of columns of the board
     */
    public int getNumcols() {
        return board.getNumcols();
    }

    /**
     *
     * @return the number of rows of the board
     */
    public int getNumrows() {
        return board.getNumrows();
    }

    /**
     *
     * @return the selection made by the user
     */
    public int[] getSelection() {
        return selection;
    }

    /**
     *
     * @return the second selection made by the user
     */
    public int[] getSelectedto() {
        return selectedto;
    }

}
