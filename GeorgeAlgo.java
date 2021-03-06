import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class GeorgeAlgo {

    private static Controller controller;
    private static Random rand;

    public static void main(String[] args) {
        controller=new Controller(new Scanner(System.in));
        rand = new Random();

        controller.readInput();

        //alg1();
        //alg2();
        //alg3();
        alg4();

        try {
            controller.printSolution("map1.output");
        }catch (IOException e){
            e.printStackTrace();
        }

        System.out.println("Score: " + controller.calculateScore());
    }

    private static MineFactory chooseRandom(MineFactory[] options){
        return options[rand.nextInt(options.length)];
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

    private static void alg3(){

        //brute force
        long score = 0;

        while (score <= 28172) {

            controller.reset();

            while (!controller.checkIfDone()) {

                for (Worker worker : controller.workers) {

                    MineFactory[] todo = controller.getNonEmptyMines();
                    if (todo.length == 0 && worker.items.size() == 0) continue;

                    if (worker.items.size() == 0) {
                        //goto mine
                        MineFactory closestMine = getClosest(worker.x, worker.y, todo);
                        controller.gotoMine(worker.uni_id, closestMine.id);

                    } else if (worker.items.size() == worker.capacity) {
                        //goto fac
                        MineFactory closestFactory = getClosest(worker.x, worker.y,
                                controller.getFactoriesByResTags(worker.items)
                        );
                        controller.gotoFactory(worker.uni_id, closestFactory.id);

                    } else {
                        //choose

                        MineFactory factory = chooseRandom(controller.getFactoriesByResTags(worker.items));

                        if (rand.nextInt(1) == 1) {

                            controller.gotoFactory(worker.uni_id,
                                    factory.id
                            );

                        } else {

                            MineFactory[] availableMines = getMinesNotRes(worker.items);
                            if (availableMines.length == 0) {
                                controller.gotoFactory(worker.uni_id, factory.id);
                                continue;
                            }

                            controller.gotoMine(worker.uni_id,
                                    chooseRandom(availableMines).id
                            );

                        }

                    }


                }

            }

            score = controller.calculateScore();

        }

    }

    private static void alg4(){
        //send each worker to the closest mine with resources, then check what's closest.

        while (!controller.checkIfDone()){

            for (Worker worker : controller.workers){

                MineFactory[] todo = controller.getNonEmptyMines();
                if (todo.length == 0 && worker.items.size() == 0) continue;

                if (worker.items.size() == 0){
                    worker.loading = true;
                    //goto mine
                    MineFactory closestMine = getClosest(worker.x, worker.y, todo);
                    controller.gotoMine(worker.uni_id, closestMine.id);

                }else if (worker.items.size() == worker.capacity){
                    worker.loading = false;
                    //goto fac
                    MineFactory closestFactory = getClosest(worker.x, worker.y,
                            controller.getFactoriesByResTags(worker.items)
                    );
                    controller.gotoFactory(worker.uni_id, closestFactory.id);

                }else {
                    //continue

                    MineFactory closestFactory = getClosest(worker.x, worker.y,
                            controller.getFactoriesByResTags(worker.items)
                    );

                    if (worker.loading){

                        MineFactory[] availableMines = getMinesNotRes(worker.items);
                        if (availableMines.length == 0){
                            worker.loading = false;
                            controller.gotoFactory(worker.uni_id, closestFactory.id);
                            continue;
                        }

                        MineFactory closestMine = getClosest(worker.x, worker.y, availableMines);
                        controller.gotoMine(worker.uni_id, closestMine.id);

                    }else{

                        controller.gotoFactory(worker.uni_id, closestFactory.id);

                    }

                }


            }

        }
    }

}