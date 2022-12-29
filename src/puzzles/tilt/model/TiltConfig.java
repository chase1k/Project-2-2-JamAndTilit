package puzzles.tilt.model;

import puzzles.common.solver.Configuration;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Tilt configuration for everything
 */
public class TiltConfig implements Configuration {
    private char[][] OGrid;
    private static int DIM;
    private LinkedList<Character> moves;
    private LinkedList<char[][]> path;
    private static int numBlue;

    /**
     * Tilt Constructor for new root
     * @param gridPlane input board
     */
    public TiltConfig(char[][] gridPlane){
        DIM = gridPlane.length;
        OGrid = new char[DIM][DIM];
        for (int row = 0; row < DIM; row++) {
            for (int col = 0; col < DIM; col++) {
                OGrid[row][col] = gridPlane[row][col];
            }
        }
        numBlue = currentBees();
        moves = new LinkedList<>();
        moves.add('S');
        path = new LinkedList<>();
        path.add(OGrid);
    }

    /**
     * Tilt constructor for new configs with predecessors
     * @param gridPlane grid to copy
     * @param successor the successive config
     * @param prevMove the previous move made
     */
    public TiltConfig(char[][] gridPlane, TiltConfig successor, char prevMove){
        OGrid = new char[DIM][DIM];
        for (int row = 0; row < DIM; row++) {
            for (int col = 0; col < DIM; col++) {
                OGrid[row][col] = gridPlane[row][col];
            }
        }
        path = new LinkedList<>();
        path.addAll(successor.path);
        path.add(OGrid);
        moves = new LinkedList<>();
        moves.addAll(successor.moves);
        moves.add(prevMove);
    }

    /**
     * Checks if board has any Green Discs left
     * @return true if solution, false if not
     */
    @Override
    public boolean isSolution() {
        for (int row = 0; row < OGrid.length; row++) {
            for (int col = 0; col < OGrid[row].length; col++) {
                if (OGrid[row][col] == 'G'){
                    return false;
                }}}
        return true;
    }

    /**
     * Gets possible solutions from current config
     * @return Collection of possible configs
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        Collection<Configuration> neighbors = new LinkedList<>();

        char[][] lBoard = moveLeft();
        if (lBoard != null){
            TiltConfig left = new TiltConfig(lBoard,this,'W');
            if(left.OGrid != null && !neighbors.contains(left)){
                neighbors.add(left);}
        }

        char[][] rBoard = moveRight();
        if (rBoard != null){
            TiltConfig right = new TiltConfig(rBoard,this,'E');
            if(right.OGrid != null && !neighbors.contains(right)){
                neighbors.add(right);}
        }

        char[][] uBoard = moveUP();
        if (uBoard != null){
            TiltConfig up = new TiltConfig(uBoard,this,'N');
            if(up.OGrid != null && !neighbors.contains(up)){
                neighbors.add(up);}
        }

        char[][] dBoard = moveDown();
        if (dBoard != null){
            TiltConfig down = new TiltConfig(dBoard,this,'S');
            if(down.OGrid != null && !neighbors.contains(down)){
                neighbors.add(down);}
        }

        return neighbors;
    }

    /**
     * Tests tilting left
     * @return board
     */
    public char[][] moveLeft() {
        char[][] leftGrid = new char[DIM][DIM];
        char[][] grid = new char[DIM][DIM];

        for (int row = 0; row < DIM; row++) {
            for (int col = 0; col < DIM; col++) {
                grid[row][col] = OGrid[row][col];
            }
        }

        for (int row = 0; row < DIM; row++) {
            leftGrid[row][0] = grid[row][0];
        }

        for (int moves = 0; moves < DIM; moves++) {
            boolean movable = false;
            for (int col = 1; col < DIM; col++) {
                for (int row = 0; row < DIM; row++) {

                    if (grid[row][col] == 'G' || grid[row][col] == 'B') {
                        if (leftGrid[row][col - 1] == '.') {
                            leftGrid[row][col - 1] = grid[row][col];
                            leftGrid[row][col] = '.';
                            grid[row][col - 1] = grid[row][col];
                            grid[row][col] = '.';
                            if (col >= 2 && (leftGrid[row][col-2] == '.' || leftGrid[row][col-2] == 'O')){
                                movable = true;
                            }
                        }
                        if (leftGrid[row][col - 1] == 'O') {
                            if (grid[row][col] == 'B'){
                                return null;
                            }
                            grid[row][col] = '.';
                        }
                    }}

                for (int row = 0; row < DIM; row++) {
                    leftGrid[row][col] = grid[row][col];
                }
            }
            if (!movable){return leftGrid;}
        }
        return leftGrid;
    }

    /**
     * Tests tilting up
     * @return board
     */
    public char[][] moveUP() {
        char[][] upGrid = new char[DIM][DIM];
        char[][] grid = new char[DIM][DIM];

        for (int row = 0; row < DIM; row++) {
            for (int col = 0; col < DIM; col++) {
                grid[row][col] = OGrid[row][col];
            }
        }
        for (int col = 0; col < DIM; col++) {
            upGrid[0][col] = grid[0][col];
        }

        for (int moves = 0; moves < DIM; moves++) {
            boolean movable = false;

            for (int row = 1; row < DIM; row++) {
                for (int col = 0; col < DIM; col++) {

                    if (grid[row][col] == 'G' || grid[row][col] == 'B') {
                        if (upGrid[row - 1][col] == '.') {
                            upGrid[row - 1][col] = grid[row][col];
                            upGrid[row][col] = '.';
                            grid[row - 1][col] = grid[row][col];
                            grid[row][col] = '.';
                            if (row >= 2 && (upGrid[row-2][col] == '.' || upGrid[row-2][col] == 'O')){
                                movable = true;
                            }
                        }
                        if (upGrid[row - 1][col] == 'O') {
                            if (grid[row][col] == 'B'){
                                return null;
                            }
                            grid[row][col] = '.';
                        }
                    }}

                for (int col = 0; col < DIM; col++) {
                    upGrid[row][col] = grid[row][col];
                }
            }
            if (!movable){return upGrid;}
        }
        return upGrid;
    }

    /**
     * Tests tiltin down
     * @return board
     */
    public char[][] moveDown() {
        char[][] downGrid = new char[DIM][DIM];
        char[][] grid = new char[DIM][DIM];

        for (int row = 0; row < DIM; row++) {
            for (int col = 0; col < DIM; col++) {
                grid[row][col] = OGrid[row][col];
            }
        }

        for (int col = 0; col < DIM; col++) {
            downGrid[DIM-1][col] = grid[DIM-1][col];
        }

        for (int moves = 0; moves < DIM; moves++) {
            boolean movable = false;
            for (int row = DIM-2; row >= 0; row--) {
                for (int col = 0; col < DIM; col++) {

                    if (grid[row][col] == 'G' || grid[row][col] == 'B') {
                        if (downGrid[row + 1][col] == '.') {
                            downGrid[row + 1][col] = grid[row][col];
                            downGrid[row][col] = '.';
                            grid[row + 1][col] = grid[row][col];
                            grid[row][col] = '.';
                            if (row < DIM-2 && (downGrid[row+2][col] == '.' || downGrid[row+2][col] == 'O')){
                                movable = true;
                            }
                        }
                        if (downGrid[row + 1][col] == 'O') {
                            if (grid[row][col] == 'B'){
                                return null;
                            }
                            grid[row][col] = '.';
                        }
                    }}
                for (int col = 0; col < DIM; col++) {
                    downGrid[row][col] = grid[row][col];
                }
            }
            if (!movable){return downGrid;}
        }
        return downGrid;
    }

    /**
     * Tests tilting right
     * @return board
     */
    public char[][] moveRight() {
        char[][] rightGrid = new char[DIM][DIM];
        char[][] grid = new char[DIM][DIM];

        for (int row = 0; row < DIM; row++) {
            for (int col = 0; col < DIM; col++) {
                grid[row][col] = OGrid[row][col];
            }
        }

        for (int row = 0; row < DIM; row++) {
            rightGrid[row][DIM-1] = grid[row][DIM-1];
        }

        for (int moves = 0; moves < DIM; moves++) {
            boolean movable = false;

            for (int col = DIM-2; col >= 0; col--) {
                for (int row = 0; row < DIM; row++) {

                    if (grid[row][col] == 'G' || grid[row][col] == 'B') {
                        if (rightGrid[row][col + 1] == '.') {
                            rightGrid[row][col + 1] = grid[row][col];
                            rightGrid[row][col] = '.';
                            grid[row][col + 1] = grid[row][col];
                            grid[row][col] = '.';
                            if (col < DIM-2 && (rightGrid[row][col+2] == '.' || rightGrid[row][col+2] == 'O')){
                                movable = true;
                            }
                        }
                        if (rightGrid[row][col + 1] == 'O') {
                            if (grid[row][col] == 'B'){
                                return null;
                            }
                            grid[row][col] = '.';
                        }
                    }}
                for (int row = 0; row < DIM; row++) {
                    rightGrid[row][col] = grid[row][col];
                }
            }
            if (!movable){return rightGrid;}
        }
        return rightGrid;
    }

    /**
     * Gets amount of Blue discs on board
     * @return num of blue discs
     */
    public int currentBees(){
        int Bees=0;
        for (int row = 0; row < DIM; row++) {
            for (int col = 0; col < DIM; col++) {
                if (OGrid[row][col] == 'B'){
                    Bees++;
                }}}
        return Bees;
    }

    /**
     * Gets original num of blues
     * @return num of blue discs
     */
    public int getNumBlue() {return numBlue;}

    /**
     * Tests if configs are equivalint
     * @param other other config
     * @return returns true if boards are equal
     */
    @Override
    public boolean equals(Object other) {
        if(this == other) return true;
        if (other == null || other.getClass() != getClass()) return false;
        TiltConfig oth = (TiltConfig) other;
        return Arrays.deepEquals(OGrid,oth.OGrid);
    }

    /**
     * Gets board
     * @return grid
     */
    public char[][] getOGrid() {return OGrid;}

    /**
     * Hashes board
     * @return int of hash
     */
    @Override
    public int hashCode() {
        return Arrays.deepHashCode(OGrid);
    }

    /**
     * toString
     * @return String representation of board
     */
    @Override
    public String toString() {
        String board = "";
        for (int row = 0; row < DIM; row++) {
            for (int col = 0; col < DIM; col++) {
                board += OGrid[row][col] + " ";
            }
            board += "\n";
        }return board;
    }

    /**
     * Gets moves
     * @return LinkedList of moves
     */
    public LinkedList getPath() {return moves;}
}
