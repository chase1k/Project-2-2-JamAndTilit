package puzzles.jam.model;


import puzzles.common.solver.Configuration;

import java.util.*;

/**
 * the configuration class that works with jam. To be used with solver to get the solution useing BFS
 * author: Owen Whitman
 */
public class JamConfig implements Configuration{

    static int numRows;
    static int numCols;

    private HashSet<Cordinate> occupiedSpaces;
    private ArrayList<Car> Cars;

    /**
     * Constructor
     * @param Cars the cars in the Jam configuration
     */
    public JamConfig(ArrayList<Car> Cars){
        this.Cars = Cars;
    }

    /**
     *
     * @return if the current jamconfig is the solution
     */
    @Override
    public boolean isSolution() {
        Car goalcar = Cars.get(Cars.size()-1);
        for (int i = 0; i < numRows; i++) {
            if(goalcar.isOccupied(new int[]{i, numCols-1})) {
                return true;
            }
        }
        return false;
    }


    /**
     *
     * @return all the possible next moves the jam config could take
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        ArrayList<Configuration> neighbors = new ArrayList<>();
        occupiedSpaces = new HashSet<>();


        for(Car car: this.Cars){
            for (int r = car.getStart()[0]; r <= car.getEnd()[0]; r++) {
                for (int c = car.getStart()[1]; c <= car.getEnd()[1]; c++) {
                    occupiedSpaces.add(new Cordinate(r, c));
                }
            }
        }


        for (Car car:this.Cars) {
            if(!car.isHorizontal()){
                boolean[] vertOccu = isVertOccupied(car.getStart(), car.getEnd());

                if(vertOccu[0]){
                    neighbors.add(movecarvertically(car, 1));
                }
                if(vertOccu[1]){
                    neighbors.add(movecarvertically(car, -1));
                }

            }
            else{
                boolean[] hoizOccu = isHoizontalOccupied(car.getStart(), car.getEnd());

                if(hoizOccu[0]){
                    neighbors.add(movecarhorizontaly(car, 1));
                }
                if(hoizOccu[1]){
                    neighbors.add(movecarhorizontaly(car, -1));
                }
            }

        }

        return neighbors;
    }

    /**
     * a helper for get neighbors, moves the car horizontally
     * @param car the car to move
     * @param amount the amount to move the car by
     * @return a jam config if you moved the car horizontally
     */
    private JamConfig movecarhorizontaly(Car car, int amount){
        ArrayList<Car> newcars = new ArrayList<>();
        for (Car copycars:this.Cars) {
            if(copycars != car){
                newcars.add(copycars.clone());
            }
            else {
                int[] newstart = copycars.getStart().clone();
                newstart[1] += amount;
                int[] newend = copycars.getEnd().clone();
                newend[1] += amount;
                newcars.add(new Car(newstart, newend, copycars.isHorizontal(), copycars.getLetter()));
            }
        }
        return new JamConfig(newcars);
    }

    /**
     * a helper for get neighbors, moves the car vertically
     * @param car the car to move
     * @param amount the amount to move the car by
     * @return a jam config if you moved the car vertically
     */
    private JamConfig movecarvertically(Car car, int amount){
        ArrayList<Car> newcars = new ArrayList<>();
        for (Car copycars :this.Cars) {
            if(copycars != car){
                newcars.add(copycars.clone());
            }
            else {
                int[] newstart = copycars.getStart().clone();
                newstart[0] += amount;
                int[] newend = copycars.getEnd().clone();
                newend[0] += amount;
                Car newcar = new Car(newstart, newend, copycars.isHorizontal(), copycars.getLetter());
                newcars.add(newcar);
            }
        }
        JamConfig sol = new JamConfig(newcars);
        return sol;
    }

    /**
     * helper function for get neighbors to see if the given car should be moved
     * @param start the head of the car to check
     * @param end the tail of the car to check
     * @return if it's possible to move forwards and backwards
     */
    private boolean[] isHoizontalOccupied(int[]start, int[] end) {
        boolean[] occupied = new boolean[]{end[1] + 1 < numCols, start[1] - 1 >= 0};
        if(!occupied[0] && !occupied[1]){
            return occupied;
        }
        if (occupied[0]) {
            occupied[0] = !occupiedSpaces.contains(new Cordinate(end[0], end[1]+1));
        }
        if(occupied[1]){
            occupied[1] = !occupiedSpaces.contains(new Cordinate(start[0], start[1]-1));
        }
        return occupied;
    }

    /**
     * helper function for get neighbors to see if the given car should be moved
     * @param start the head of the car to check
     * @param end the tail of the car to check
     * @return if it is possible to move up and down
     */
    private boolean[] isVertOccupied(int[]start, int[] end) {
        boolean[] occupied = new boolean[]{end[0] + 1 < numRows, start[0] -1 >= 0};

        if(!occupied[0] && !occupied[1]){
            return occupied;
        }
        if (occupied[0]) {
            occupied[0] = !occupiedSpaces.contains(new Cordinate(end[0] + 1, end[1]));
        }
        if(occupied[1]){
            occupied[1] = !occupiedSpaces.contains(new Cordinate(start[0] -1 , start[1]));
        }
        return occupied;
    }


    /**
     *
     * @return the cars in the JamConfig
     */
    public ArrayList<Car> getCars() {
        return Cars;
    }

    /**
     *
     * @param numCols the new number of cols the jam config has
     */
    public static void setNumCols(int numCols) {
        JamConfig.numCols = numCols;
    }

    /**
     *
     * @param numRows the new number of rows the jam config has
     */
    public static void setNumRows(int numRows) {
        JamConfig.numRows = numRows;
    }

    /**
     * compare a object with a jam config to see if they are the same or not
     * @param other the object to compare to
     * @return if the other object is the same as the jam config
     */
    @Override
    public boolean equals(Object other) {
        if(other instanceof JamConfig otherJamConfig){
            ArrayList<Car> otherCars = otherJamConfig.getCars();
            if(otherCars.size() == Cars.size()){
                for (int i = 0; i < Cars.size(); i++) {
                    if(!Cars.get(i).equals(otherCars.get(i))){ //ONLY WORKS IF PERSERVE ORDER
                        return false;
                    }
                }
                return true;
            }
        }
            return false;
    }

    /**
     *
     * @return a hashcode of the JamConfig using its cars
     */
    @Override
    public int hashCode() {
        return Objects.hash(Cars);
    }

    /**
     * Displays the grid of the JamConfig car
     */
    public void showgird(){
        char[][] grid = new char[numRows][numCols];
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                grid[r][c] = '.';
            }
        }
        for(Car car: this.Cars){
            for (int r = car.getStart()[0]; r <= car.getEnd()[0]; r++) {
                for (int c = car.getStart()[1]; c <= car.getEnd()[1]; c++) {
                    grid[r][c] = car.getLetter();
                }

            }
        }
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                System.out.print(grid[r][c] + " ");
            }
            System.out.println(" ");
        }
    }

    /**
     *
     * @return a string with information of all the cars in the jam config
     */
    @Override
    public String toString() {
        String out = "Jam Config with Cars: ";
        for (Car car :getCars()) {
            out += " " + car.toString();

        }
        return out;
    }


}
