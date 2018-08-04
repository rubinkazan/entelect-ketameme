import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class GeorgeAlgo {

    private static Controller controller;

    public static void main(String[] args) {
        controller=new Controller(new Scanner(System.in));

        controller.readInput();

        alg2();

        //alg1();

        try {
            controller.printSolution("map5.output");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static MineFactory[] getMinesNotRes(List<String> ress){

        MineFactory[] testy = controller.getNonEmptyMines();

        int tagMatches = 0;
        for (MineFactory mine : testy)
            if (!ress.contains(mine.resTag)) tagMatches++;

        MineFactory[] matches = new MineFactory[tagMatches];
        int j = 0;
        for (MineFactory mine : testy)
            if (!ress.contains(mine.resTag)){
                matches[j] = mine;
                j++;
            }

        return matches;

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

    private static void alg1(){

        //send each worker to the closest mine with resources and then to the closest factory

        while (!controller.checkIfDone()){

            for (Worker worker : controller.workers){

                MineFactory[] todo = controller.getNonEmptyMines();

                if (todo.length == 0) break;

                MineFactory closestMine = getClosest(worker.x, worker.y, todo);
                controller.gotoMine(worker.uni_id, closestMine.id);

                MineFactory closestFactory = getClosest(worker.x, worker.y, controller.getFactoriesByResTag(closestMine.resTag));
                controller.gotoFactory(worker.uni_id, closestFactory.id);

            }

        }

    }

    private static void alg2(){
        //send each worker to the closest mine with resources, then check what's closest.

        while (!controller.checkIfDone()){

            for (Worker worker : controller.workers){

                MineFactory[] todo = controller.getNonEmptyMines();
                if (todo.length == 0 && worker.items.size() == 0) continue;

                if (worker.items.size() == 0){
                    //goto mine
                    MineFactory closestMine = getClosest(worker.x, worker.y, todo);
                    controller.gotoMine(worker.uni_id, closestMine.id);

                }else if (worker.items.size() == worker.capacity){
                    //goto fac
                    MineFactory closestFactory = getClosest(worker.x, worker.y,
                            controller.getFactoriesByResTags(worker.items)
                    );
                    controller.gotoFactory(worker.uni_id, closestFactory.id);

                }else {
                    //choose

                    MineFactory closestFactory = getClosest(worker.x, worker.y,
                            controller.getFactoriesByResTags(worker.items)
                    );

                    MineFactory[] availableMines = getMinesNotRes(worker.items);
                    if (availableMines.length == 0){
                        controller.gotoFactory(worker.uni_id, closestFactory.id);
                        continue;
                    }

                    MineFactory closestMine = getClosest(worker.x, worker.y, availableMines);

                    int mineDist = controller.calcDistance(worker.x, worker.y, closestMine.x_coord, closestMine.y_coord);
                    int facDist = controller.calcDistance(worker.x, worker.y, closestFactory.x_coord, closestFactory.y_coord);
                    if (mineDist < facDist){
                        controller.gotoMine(worker.uni_id, closestMine.id);
                    }else{
                        controller.gotoFactory(worker.uni_id, closestFactory.id);
                    }

                }


            }

        }
    }

}