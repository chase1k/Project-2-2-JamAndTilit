package puzzles.strings;

import puzzles.common.solver.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * The configuration used for the strings puzzle
 *
 * @author Owen Whitman
 */
public class StringsConfig implements Configuration {

    static String start;
    static String end;

    String current;

    /**
     * Contructs a new string config
     * @param current the string of the string config instance
     */
    StringsConfig(String current){
        this.current = current;
    }

    /**
     * used to check if the bfs arrived at the end
     * @return if the current string is the solution
     */
    @Override
    public boolean isSolution() {
        return this.current.equals(end);
    }

    /**
     * sets the static start
     * @param s the stating value of all String configs
     */
    public void setStart(String s){
        start = s;
    }
    /**
     * sets the static end
     * @param e the end goal of all String configs
     */
    public void setEnd(String e){
        end = e;
    }

    /**
     * Used to easially find neigbors
     * @param c the character to find the previous letter of
     * @return the character that is next in the alaphbet
     */
    private char getNextChar(char c) {
        return (char)( ( ( ( (int) c - (int)'A') + 1) % 26) + (int) 'A') ;
    }
    /**
     * Used to easially find neigbors
     * @param c the character to find the previous letter of
     * @return the character that is previous in the alaphbet
     */
    private char getPrevChar(char c) {
        int newletter = ( ( ( (int) c - (int)'A') - 1) % 26);
        if(newletter < 0){
            newletter = 25;
        }
        return (char)(  newletter+ (int) 'A') ;
    }

    /**
     *
     * @param o the object to check
     * @return if the value of the objects are the same
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringsConfig that = (StringsConfig) o;
        return current.equals(that.current);
    }

    /**
     *
     * @return a hash code based off of the unique string config value, current
     */
    @Override
    public int hashCode() {
        return Objects.hash(current);
    }

    /**
     *
     * @return the current string
     */
    @Override
    public String toString() {
        return current;
    }

    /**
     * Returns the neighbors of current string config to be used for bfs
     * @return an array list with all the neighbors
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        ArrayList<Configuration> neighbors = new ArrayList<>();
        for(int i = 0; i<this.current.length(); i++){
            char letter = this.current.charAt(i);
            String wordbef = this.current.substring(0, i) + getPrevChar(letter) +
                    this.current.substring(i+ 1);
            String wordaft = this.current.substring(0, i) + getNextChar(letter) +
                    this.current.substring(i+ 1);
            neighbors.add(new StringsConfig(wordbef));
            neighbors.add(new StringsConfig(wordaft));
        }


        return neighbors;
    }
}
