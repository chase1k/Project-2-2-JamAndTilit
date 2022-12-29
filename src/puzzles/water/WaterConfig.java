package puzzles.water;

import puzzles.common.solver.Configuration;
import puzzles.strings.StringsConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * A configuration to be used for the water config puzzle
 * @Author Owen
 */
public class WaterConfig implements Configuration {

    static int[] compacaties;
    int[] current;
    static int goal;


    /**
     * Contructs a new water config
     * @param current the water of the water config instance
     */
    WaterConfig(int[] current){
        this.current = current;
    }

    public static void setCompacaties(int[] compacaties) {
        WaterConfig.compacaties = compacaties;
    }

    public static void setGoal(int goal) {
        WaterConfig.goal = goal;
    }

    @Override
    public boolean isSolution() {
        for(int bucket: current){
            if(bucket == goal){
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WaterConfig that = (WaterConfig) o;
        //System.out.println(Arrays.toString(current) +" " + Arrays.toString(that.current) + " HERE " + Arrays.equals(current, that.current));
        return Arrays.equals(current, that.current);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(current);
    }

    @Override
    public String toString() {
        return Arrays.toString(current);
    }

    @Override
    public Collection<Configuration> getNeighbors() {
        ArrayList<Configuration> neighbors = new ArrayList<>();
        for(int i = 0; i<this.current.length; i++){

            int[] fillbucket = Arrays.copyOf(current,current.length);
            fillbucket[i] = compacaties[i];
            WaterConfig filledneighbor = new WaterConfig(fillbucket);
            neighbors.add(filledneighbor);

            if(current[i] != 0){
                int[] dumpbucket = Arrays.copyOf(current,current.length);
                dumpbucket[i] = 0;
                WaterConfig dumpneighbor = new WaterConfig(dumpbucket);
                neighbors.add(dumpneighbor);
            }

            for(int j = 0; j<this.current.length; j++){
                if(i == j || current[i] == 0){
                    continue;
                }
                int[] givebucket = Arrays.copyOf(current,current.length);
                givebucket[j] = current[i] + current[j];
                givebucket[i] = 0;
                if(givebucket[j] > compacaties[j]){
                    givebucket[i] = givebucket[j] - compacaties[j];
                    givebucket[j] = compacaties[j];
                }
                WaterConfig giveneighbor = new WaterConfig(givebucket);
                neighbors.add(giveneighbor);
            }

        }
        return neighbors;
    }




}

