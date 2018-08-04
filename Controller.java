import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Controller {

    private Scanner in;
    public int mapWidth;
    public int mapHeight;
    public Worker[] workers;
    private List<Move> moves;
    public Mine[] mines;
    public Factory[] factories;
    public int budget;

    private class Move{
        int workerID, destinationID;
        Move(int workerID, int destinationID){
            this.workerID = workerID;
            this.destinationID = destinationID;
        }
    }

    public Controller(Scanner scanner){

        in = scanner;
        moves = new ArrayList<Move>;

    }

    public void readInput(){

        mapHeight = in.nextInt();
        mapWidth = in.nextInt();
        int miners = in.nextInt();
        int excavators = in.nextInt();
        int haulers = in.nextInt();
        workers = new Worker[miners+excavators+haulers];
        int workerID = 0;
        for (int m = 0; m < miners; m++) {
            workers[m] = new Worker(workerID, 1);
            workerID++;
        }
        for (int e = miners; e < miners+excavators; e++) {
            workers[e] = new Worker(workerID, 3);
            workerID++;
        }
        for (int h = miners+excavators; h < workers.length; h++) {
            workers[h] = new Worker(workerID, 5);
            workerID++;
        }

        mines = new Mine[in.nextInt()];
        factories = new Factory[in.nextInt()];
        budget = in.nextInt();
        in.nextLine();

        for (int i = 0; i < mines.length; i++){
            int index = in.nextInt();
            String resourceTag = in.next();
            int x = in.nextInt();
            int y = in.nextInt();
            int resources = in.nextInt();
            mines[i] = new Mine(index, resourceTag, x, y, resources);

        }

        for (int i = 0; i < factories.length; i++){
            int index = in.nextInt();
            String resourceTag = in.next();
            int x = in.nextInt();
            int y = in.nextInt();
            factories[i] = new Factory(index, resourceTag, x, y);

        }

    }

    public Worker[] getWorkersByCapacity(int cap){

        int capMatches = 0;
        for (Worker worker : workers)
            if (worker.capacity == cap) capMatches++;

        Worker[] matches = new Worker[capMatches];
        int j = 0;
        for (Worker worker : workers)
            if (worker.capacity == cap){
                matches[j] = worker;
                j++;
            }

        return matches;

    }

    public Worker getWorkerById(int workerID){

        for (Worker worker : workers)
            if (worker.id == workerID)
                return worker;

    }

    public Factory getFactoryById(int factoryID){

        for (Factory factory : factories)
            if (factory.id == factoryID)
                return factory;

    }

    public Mine getMineById(int mineID){

        for (Mine mine : mines)
            if (mine.id == mineID)
                return mine;

    }

    public void gotoMine(int workerID, int mineID){

        moves.add(new Move(workerID, mineID));

    }

    public void gotoFactory(int workerID, int factoryID){

        moves.add(new Move(workerID, factoryID));

    }

    private Move[] getMovesByWorker(int workerID){

        int matches = 0;

        for (Move move : moves)
            if (move.workerID == workerID)
                matches++;

        Move[] matchedMoves = new Move[matches];

        int j = 0;
        for (Move move : moves) {
            if (move.workerID == workerID) {
                matchedMoves[j] = move;
                j++;
            }
        }

        return matchedMoves;

    }

    public void printSolution(){

        for (int capOut = 1; capOut <=5; capOut += 2) {

            for (Worker worker : getWorkersByCapacity(capOut)) {
                String line = capToStr(capOut) + "|";
                for (Move move : getMovesByWorker(worker.uni_id))
                    line += move.destinationID + ",";
                line = line.substring(line.length() - 2); //get rid of last comma
                System.out.println(line);
            }

        }

    }

    private String capToStr(int cap){
        switch (cap){
            case 1:
                return "M";
            case 3:
                return "E";
            case 5:
                return "H";
        }
    }

}
