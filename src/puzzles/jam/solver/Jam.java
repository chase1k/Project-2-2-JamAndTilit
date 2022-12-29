package puzzles.jam.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.jam.model.Car;
import puzzles.jam.model.JamConfig;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * Solves the Jam game based off of the given file
 * @author Owen
 */
public class Jam {

    /**
     * The main function solves Jam
     * @param args the Jam file to solve
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Jam filename");
        }
        else{
            try(BufferedReader reader = new BufferedReader(new FileReader(args[0]))) {
                System.out.println("File: " + args[0]);
                String[] splitsize = reader.readLine().split(" ");
                int numrows = Integer.parseInt(splitsize[0]);
                int numcols = Integer.parseInt(splitsize[1]);

                int numcars = Integer.parseInt(reader.readLine());
                ArrayList<Car> Cars = new ArrayList<>();
                for (int i = 0; i < numcars; i++) {
                    String[] cardata = reader.readLine().split(" ");
                    char Carletter = cardata[0].charAt(0);
                    int[] startingcord = {Integer.parseInt(cardata[1]), Integer.parseInt(cardata[2])};
                    int[] endingcord = {Integer.parseInt(cardata[3]), Integer.parseInt(cardata[4])};
                    boolean isHorizontal = Integer.parseInt(cardata[2]) != Integer.parseInt(cardata[4]);

                    Cars.add(new Car(startingcord, endingcord, isHorizontal, Carletter));
                }

                Solver solve = new Solver();
                JamConfig startingconig = new JamConfig(Cars);
                JamConfig.setNumRows(numrows);
                JamConfig.setNumCols(numcols);
                startingconig.showgird();
                ArrayList<Configuration> solution = (ArrayList<Configuration>) solve.solve(startingconig);
                System.out.println("Total configs: " + solve.getTotalsolutions());
                System.out.println("Unique configs: " + solve.getUniquesolutions());
                if(solution.size() > 0){
                    for (int i = 0; i < solution.size(); i++) {
                        System.out.println("Step " + i +":");
                        JamConfig solstep = ( JamConfig) solution.get(i);
                        solstep.showgird();
                        System.out.println(" ");
                    }
                }
                else{
                    System.out.println("No solution found");
                }




            }
            catch (IOException e){
                System.out.println("FileNotFoundException: output-file");
            }

        }
    }
}