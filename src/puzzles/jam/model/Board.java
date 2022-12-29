package puzzles.jam.model;

import java.util.ArrayList;
import java.util.Arrays;
/**
 * a class to handle the board state and movements for the model class
 * @author Owen Whitman
 */
public class Board {
    private char[][] board;

    private ArrayList<Car> cars;

    private int numcols;
    private int numrows;

    /**
     * Constructor for the board class
     * @param numcols the number of columns the board should have
     * @param numrows the number of rows the board should have
     * @param cars the cars on the board
     */
    public Board(int numcols, int numrows, ArrayList<Car> cars){
        this.numcols = numcols;
        this.numrows = numrows;
        this.board = new char[numrows][numcols];
        this.cars = cars;
        for (int r = 0; r < numrows; r++) {
            for (int c = 0; c < numcols; c++) {
                this.board[r][c] = '.';
            }
        }
        for(Car car: this.cars){
            for (int r = car.getStart()[0]; r <= car.getEnd()[0]; r++) {
                for (int c = car.getStart()[1]; c <= car.getEnd()[1]; c++) {
                    this.board[r][c] = car.getLetter();
                }

            }
        }

    }

    /**
     * move a peice on the board from one position to another
     * @param row1 the row of the piece to move
     * @param col1 the column of the piece to move
     * @param row2 the row to move the piece
     * @param col2 the column to move the piece
     * @return if it was able to be moved or not
     */
    public boolean move(int row1, int col1, int row2, int col2){
        Car cartomove = this.cars.get(0);
        char carletter = getBoard(row1, col1);
        for (Car car: this.cars) {
            if(carletter == car.getLetter()){
                cartomove = car;
            }
        }
        if(getBoard(row2, col2) == carletter){
            return false;
        }
        int[] newstart;
        int[] newend;
        if(cartomove.isHorizontal()){
            if(row1 != row2){
                return false;
            }
            if(col1 > col2){
                int diff = cartomove.getStart()[1] - col2;

                for (int i = 1; i <= diff; i++) {
                    if(this.getBoard(cartomove.getStart()[0], cartomove.getStart()[1] - i) != '.'){
                        return false;
                    }
                }

                newstart = new int[]{cartomove.getStart()[0], cartomove.getStart()[1] - diff};
                newend = new int[]{cartomove.getEnd()[0], cartomove.getEnd()[1] - diff};
            }
            else{

                int diff =  col2- cartomove.getEnd()[1];

                for (int i = 1; i <= diff; i++) {
                    if(this.getBoard(cartomove.getStart()[0], cartomove.getEnd()[1] + i) != '.'){
                        return false;
                    }
                }
                newstart = new int[]{cartomove.getStart()[0], cartomove.getStart()[1] + diff};
                newend = new int[]{cartomove.getEnd()[0], cartomove.getEnd()[1] + diff};
            }

        }
        else{
            if(col1 != col2){
                return false;
            }
            if(row1 > row2){
                int diff = cartomove.getStart()[0] - row2;

                for (int i = 1; i <= diff; i++) {
                    if(this.getBoard(cartomove.getStart()[0]-i, cartomove.getStart()[1]) != '.'){
                        return false;
                    }
                }

                newstart = new int[]{cartomove.getStart()[0]-diff, cartomove.getStart()[1]};
                newend = new int[]{cartomove.getEnd()[0]-diff, cartomove.getEnd()[1]};
            }
            else{
                int diff = row2 - cartomove.getEnd()[0];

                for (int i = 1; i <= diff; i++) {
                    if(this.getBoard(cartomove.getEnd()[0]+i, cartomove.getEnd()[1]) != '.'){
                        return false;
                    }
                }

                newstart = new int[]{cartomove.getStart()[0]+diff, cartomove.getStart()[1]};
                newend = new int[]{cartomove.getEnd()[0]+diff, cartomove.getEnd()[1]};
            }
        }
        for (int r = cartomove.getStart()[0]; r <= cartomove.getEnd()[0]; r++) {
            for (int c = cartomove.getStart()[1]; c <= cartomove.getEnd()[1]; c++) {
                this.board[r][c] = '.';
            }
        }

        cartomove.setStart(newstart);
        cartomove.setEnd(newend);

        for (int r = cartomove.getStart()[0]; r <= cartomove.getEnd()[0]; r++) {
            for (int c = cartomove.getStart()[1]; c <= cartomove.getEnd()[1]; c++) {
                this.board[r][c] = cartomove.getLetter();
            }
        }


        return true;
    }

    /**
     * get a piece of the board at the given position
     * @param r the row to get
     * @param c the column to get
     * @return the value of that board at the place
     */
    public char getBoard(int r, int c){
        return this.board[r][c];
    }

    /**
     *
     * @return if the car is at the goal
     */
    public boolean isSolution(){
        Car goalcar = this.cars.get(this.cars.size()-1);
        for (int i = 0; i < numrows; i++) {
            if(goalcar.isOccupied(new int[]{i, numcols-1})) {
                return true;
            }
        }
        return false;
    }



    /**
     *
     * @return the number of columns of the board
     */
    public int getNumcols(){
        return numcols;
    }

    /**
     *
     * @return the number of rows of the board
     */
    public int getNumrows(){
        return numrows;
    }
}
