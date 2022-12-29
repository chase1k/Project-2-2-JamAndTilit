/**
 * Class that performs bfs to solve puzzles
 * @Author Owen
 */
package puzzles.common.solver;


import java.util.*;

public class Solver{
    private int uniquesolutions = 0;
    private int totalsolutions = 0;

    LinkedList<Configuration> queue = new LinkedList<>();
    Map<Configuration, Configuration> map = new HashMap<>();

    /**
     * Performs a breath first search with neighbors and solution depending on the configuration
     * @param start the starting note of the map
     * @return the path it takes to get from the start to the end
     */
    public Collection<Configuration> solve(Configuration start){
        map.put(start, null);
        Configuration dequeue = start;
        totalsolutions += 1;
        uniquesolutions += 1;

        while(!dequeue.isSolution()){
            for (Configuration neighbor: dequeue.getNeighbors()) {
                totalsolutions += 1;
                if(!map.containsKey(neighbor)){
                    uniquesolutions += 1;
                    queue.add(neighbor);
                    map.put(neighbor,dequeue);
                }
            }
            if(queue.size() == 0){
                return new ArrayList<>();
            }
            else{
                dequeue = queue.remove(0);}
        }

        ArrayList<Configuration> order = new ArrayList<>();
        order.add(dequeue);
        while(!order.get(0).equals(start)){
            order.add(0, map.get(order.get(0)));
        }
        return order;

    }

    /**
     *
     * @return the number of total solution
     */
    public int getTotalsolutions(){
        return totalsolutions;
    }

    /**
     *
     * @return the number of unique solutions
     */
    public int getUniquesolutions(){
        return uniquesolutions;
    }


}