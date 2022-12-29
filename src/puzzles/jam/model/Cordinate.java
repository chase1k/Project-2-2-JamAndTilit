package puzzles.jam.model;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents an x y column used for get neighbors so a hashset can be used
 * @author owen
 */
public class Cordinate {

    private int r;
    private int c;

    /**
     * Constructor
     * @param r the row of the cord obj
     * @param c the column of the cord obj
     */
    public Cordinate(int r, int c){
        this.r = r;
        this.c = c;

    }

    /**
     *
     * @return the obj row
     */
    public int getR() {
        return r;
    }

    /**
     *
     * @return the obj column
     */
    public int getC() {
        return c;
    }

    /**
     *
     * @param obj obj to compare to
     * @return if the object is the same
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Cordinate otherCord) {
            return otherCord.getR() == r && otherCord.getC() == c;
        }
        return false;
    }

    /**
     *
     * @return a hash based of the objects row and column
     */
    @Override
    public int hashCode() {
        return Objects.hash(r, c);
    }
}
