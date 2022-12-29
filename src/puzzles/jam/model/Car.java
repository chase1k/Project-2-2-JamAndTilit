package puzzles.jam.model;

import java.util.Arrays;
import java.util.Objects;

/**
 * The Car objects used in the game
 * @author Owen Whitman
 */
public class Car implements Cloneable{
    private int[] start;
    private int[] end;

    private boolean horizontal;

    private char letter;

    /**
     * Constructor
     * @param start the position of the head of the car (row, column)
     * @param end the position of the tail of the car (row, column)
     * @param horizontal if the car moves horizontally
     * @param letter the letter of the car
     */
    public Car(int[] start, int[] end, boolean horizontal, char letter){
        this.start = start;
        this.end = end;
        this.horizontal = horizontal;
        this.letter = letter;
    }

    public boolean isOccupied(int[] cord){
        return (this.start[0] <= cord[0] && cord[0] <= this.end[0]) && (this.start[1] <= cord[1] && cord[1] <= this.end[1]);
    }

    /**
     * compare to see if the given object is the same object
     * @param o the object to compare
     * @return if the given object is the same object
     */
    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;}

        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        //System.out.println(Arrays.toString(this.start) + " "+ Arrays.toString(car.start) + " " + Arrays.toString(this.end) + " "+ Arrays.toString(car.end));
        return car.getLetter() == this.letter && Arrays.equals(this.start, car.start) && Arrays.equals(this.end, car.end);
    }

    /**
     *
     * @return the hash code of the car
     */
    @Override
    public int hashCode() {
        return Objects.hash(letter);
    }

    /**
     *
     * @return a string with information on the car
     */
    @Override
    public String toString() {
        return "Car{letter: "+getLetter() +" start: " + Arrays.toString(this.start) + " end: " + Arrays.toString(this.end) + " isHorizontal: " + this.isHorizontal() + "}";
    }

    /**
     *
     * @return a copy of the car
     */
    @Override
    public Car clone() {
        try {
            return (Car) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    /**
     *
     * @return if the car moves horizontally
     */
    public boolean isHorizontal(){
        return horizontal;
    }

    /**
     *
     * @return the letter of the car
     */
    public char getLetter() {
        return letter;
    }

    /**
     *
     * @return the tail of the car
     */
    public int[] getEnd() {
        return end;
    }

    /**
     *
     * @return the head of the car
     */
    public int[] getStart() {
        return start;
    }

    /**
     *
     * @param start the new head of the car
     */
    public void setStart(int[] start) {
        this.start = start;
    }

    /**
     *
     * @param end the new tail of the car
     */
    public void setEnd(int[] end) {
        this.end = end;
    }
}
