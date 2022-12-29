package puzzles.tilt.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.tilt.model.TiltConfig;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Main Tilt class
 */
public class Tilt {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java Tilt filename");
        } else {
            System.out.println("File: " + args[0]);

            char[][] tiltBoard;
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(args[0]))) {
                int DIM = Integer.parseInt(bufferedReader.readLine());
                tiltBoard = new char[DIM][DIM];

                for (int row = 0; row < DIM; row++) {
                    String[] line = bufferedReader.readLine().split("\\s+");
                    for (int col = 0; col < DIM; col++) {

                        tiltBoard[row][col] = line[col].charAt(0);
                        System.out.print(line[col].charAt(0)+" ");
                    }
                    System.out.println();
                }
            }

            TiltConfig startConfig = new TiltConfig(tiltBoard);
            Solver solve = new Solver();
            ArrayList<Configuration> solution = (ArrayList<Configuration>) solve.solve(startConfig);

            System.out.println("Total configs: " + solve.getTotalsolutions());
            System.out.println("Unique configs: " + solve.getUniquesolutions());

            if(solution.size() < 1){
                System.out.println("No solution");
                System.exit(0);
            }
            for (int i = 0; i < solution.size(); i++) {
                System.out.println("Step " + i + ": ");
                System.out.println(solution.get(i).toString());
            }
        }
    }
}
