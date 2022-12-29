package puzzles.water;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Main class for the water buckets puzzle.
 *
 * @author Owen Whitman
 */
public class Water {

    /**
     * Run an instance of the water buckets puzzle.
     *
     * @param args [0]: desired amount of water to be collected;
     *             [1..N]: the capacities of the N available buckets.
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(
                    ("Usage: java Water amount bucket1 bucket2 ...")
            );
        } else {
            ArrayList<Integer> tmpcompacity = new ArrayList<Integer>();
            int[] compacity;
            try{
                int summer = 1;
                while(true){
                    tmpcompacity.add(Integer.valueOf(args[summer]));
                    summer += 1;
                }
            }
            catch (IndexOutOfBoundsException e){
                compacity= new int[tmpcompacity.size()];
                for(int i = 0; i<tmpcompacity.size(); i++){
                    compacity[i] = tmpcompacity.get(i);
                }
            }
            System.out.println("Amount: "+args[0]+", Buckets:"+Arrays.toString(compacity) );
            Solver solve = new Solver();
            int[] startbucket = new int[compacity.length];
            WaterConfig startconfig = new WaterConfig(startbucket);
            WaterConfig.setGoal(Integer.parseInt(args[0]));
            WaterConfig.setCompacaties(compacity);
            ArrayList<Configuration> solution = (ArrayList<Configuration>) solve.solve(startconfig);
            System.out.println("Total configs: " + solve.getTotalsolutions());
            System.out.println("Unique configs: " + solve.getUniquesolutions());
            if(solution.size() > 0){
                for(int i = 0; i < solution.size(); i ++){
                    System.out.println("Step " + i +": " + solution.get(i));
                }}
            else{
                System.out.println("No solution");
            }


        }
    }
}
