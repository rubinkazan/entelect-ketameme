import java.io.IOException;
import java.util.Scanner;

public class Map1 {

    private static Controller controller;

    public static void main(String[] args) {
        controller=new Controller(new Scanner(System.in));

        controller.readInput();

        while (!controller.checkIfDone()){

            for (Worker worker : controller.workers){

                for (MineFactory mine : controller.mines){

                    if (mine.res > 0){
                        controller.gotoMine(worker.uni_id, mine.id);
                        controller.gotoFactory(worker.uni_id,
                                getClosest(mine.x_coord, mine.y_coord, controller.getFactoriesByResTag(mine.recTag)).id
                        );
                    }

                }

            }

        }

        Worker worker = controller.workers[0];

        for (MineFactory mine : controller.mines){

            while (mine.res > 0){

                controller.gotoMine(worker.uni_id, mine.id);
                controller.gotoFactory(worker.uni_id,
                        controller.getFactoriesByResTag(mine.recTag)[0].id
                );

            }

        }
        try {
            controller.printSolution("map5.output");
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