package puzzles.strings;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Main class for the strings puzzle.
 *
 * @author Owen Whitman
 */
public class Strings {
    /**
     * Run an instance of the strings puzzle.
     *
     * @param args [0]: the starting string;
     *             [1]: the finish string.
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(("Usage: java Strings start finish"));
        } else {
            System.out.println("Start: "+args[0]+", End: "+ args[1]);
            Solver solve = new Solver();
            StringsConfig firstconfig = new StringsConfig(args[0]);
            firstconfig.setStart(args[0]);
            firstconfig.setEnd(args[1]);
            ArrayList<Configuration> solution = (ArrayList<Configuration>) solve.solve(firstconfig);

            System.out.println("Total configs: " + solve.getTotalsolutions());
            System.out.println("Unique configs: " + solve.getUniquesolutions());
            if(solution.size() > 0){
                for(int i = 0; i < solution.size(); i ++){
                    System.out.println("Step " + i+": " + solution.get(i));
                }}
            else{
                System.out.println("No solution");
            }
        }
    }
}
