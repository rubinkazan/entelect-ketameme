import java.io.IOException;
import java.util.Scanner;

public class GeorgeAlgo {

    private static Controller controller;

    public static void main(String[] args) {
        controller=new Controller(new Scanner(System.in));

        controller.readInput();

        //send each worker to the closest mine with resources and then to the closest factory

        while (!controller.checkIfDone()){

            for (Worker worker : controller.workers){

                MineFactory[] todo = controller.getNonEmptyMines();

                if (todo.length == 0) break;

                MineFactory closestMine = getClosest(worker.x, worker.y, todo);
                controller.gotoMine(worker.uni_id, closestMine.id);

                MineFactory closestFactory = getClosest(worker.x, worker.y, controller.getFactoriesByResTag(closestMine.recTag));
                controller.gotoFactory(worker.uni_id, closestFactory.id);

            }

        }

        try {
            controller.printSolution("map1.output");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static MineFactory getClosest(int x, int y, MineFactory[] places){

        MineFactory closest = null;
        int closestDist = -1;
        for (MineFactory place : places){

            int dist = controller.calcDistance(x,y, place.x_coord, place.y_coord);
            if (closestDist == -1 || dist < closestDist) {
                closestDist = dist;
                closest = place;
            }

        }

        return closest;

    }

}