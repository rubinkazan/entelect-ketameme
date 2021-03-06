import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Controller {

    private Scanner in;
    public int mapWidth;
    public int mapHeight;
    public Worker[] workers;
    private List<Move> moves;
    public MineFactory[] mines;
    private int[] minesInit;
    public MineFactory[] factories;
    public int budget;

    private class Move{
        int workerID, destinationID;
        Move(int workerID, int destinationID){
            this.workerID = workerID;
            this.destinationID = destinationID;
        }
    }

    public void reset(){
        moves.clear();
        for (Worker w : workers){
            w.x = 0; w.y = 0;
        }
        for (int i = 0; i < minesInit.length; i++)
            mines[i].resCount = minesInit[i];
    }

    public Controller(Scanner scanner){

        in = scanner;
        moves = new ArrayList<Move>();

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

        int minesCount = in.nextInt();
        mines = new MineFactory[minesCount];
        minesInit = new int[minesCount];
        factories = new MineFactory[in.nextInt()];
        budget = in.nextInt();
        in.nextLine();

        for (int i = 0; i < mines.length; i++){
            int index = in.nextInt();
            String resourceTag = in.next();
            int x = in.nextInt();
            int y = in.nextInt();
            int resources = in.nextInt();
            mines[i] = new MineFactory(index, resourceTag, x, y, resources);
            minesInit[i] = resources;

        }

        for (int i = 0; i < factories.length; i++){
            int index = in.nextInt();
            String resourceTag = in.next();
            int x = in.nextInt();
            int y = in.nextInt();
            factories[i] = new MineFactory(index, resourceTag, x, y, -1);

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
            if (worker.uni_id == workerID)
                return worker;

        return null;

    }

    public MineFactory getPlaceById(int id){

        for (MineFactory mine : mines)
            if (mine.id == id)
                return mine;

        for (MineFactory factory : factories)
            if (factory.id == id)
                return factory;

        return null;

    }

    public void gotoMine(int workerID, int mineID){

        moves.add(new Move(workerID, mineID));
        Worker worker = getWorkerById(workerID);
        MineFactory mine = getPlaceById(mineID);
        worker.x = mine.x_coord;
        worker.y = mine.y_coord;

        mine.resCount--;
        worker.items.add(mine.resTag);

    }

    public void gotoFactory(int workerID, int factoryID){

        moves.add(new Move(workerID, factoryID));
        Worker worker = getWorkerById(workerID);
        MineFactory factory = getPlaceById(factoryID);
        worker.x = factory.x_coord;
        worker.y = factory.y_coord;

        worker.items.remove(factory.resTag);

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

    public void printSolution(String fileName) throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

        for (int capOut = 1; capOut <=5; capOut += 2) {

            for (Worker worker : getWorkersByCapacity(capOut)) {
                String line = capToStr(capOut) + "|";
                Move[] movesForWorker = getMovesByWorker(worker.uni_id);
                if (movesForWorker.length == 0) {
                    System.out.println(line);
                    writer.write(line+"\n");
                    continue;
                }

                for (Move move : movesForWorker)
                    line += move.destinationID + ",";
                line = line.substring(0, line.length() - 1); //get rid of last comma
                System.out.println(line);
                writer.write(line+"\n");
            }

        }

        writer.close();

    }

    public long calculateScore(){

        long dist = 0;
        for (Worker worker : workers){

            worker.x = 0; worker.y = 0;

            for (Move move : getMovesByWorker(worker.uni_id)){
                MineFactory dest = getPlaceById(move.destinationID);
                dist += calcDistance(worker.x, worker.y, dest.x_coord, dest.y_coord);
                worker.x = dest.x_coord; worker.y = dest.y_coord;
            }

        }

        return budget-dist;

    }

    public boolean checkIfDone(){

        for (MineFactory mine : mines) {
            if (mine.resCount > 0)
                return false;
        }
        for (Worker worker : workers){
            if (worker.items.size() > 0)
                return false;
        }

        return true;
    }

    public int calcDistance(int x1, int y1, int x2, int y2){

        return Math.abs(x1-x2) + Math.abs(y1-y2);

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
        return null;
    }

    public MineFactory[] getFactoriesByResTags(List<String> tags){

        int tagMatches = 0;
        for (MineFactory factory : factories)
            if (tags.contains(factory.resTag)) tagMatches++;

        MineFactory[] matches = new MineFactory[tagMatches];
        int j = 0;
        for (MineFactory factory : factories)
            if (tags.contains(factory.resTag)){
                matches[j] = factory;
                j++;
            }

        return matches;

    }

    public MineFactory[] getFactoriesByResTag(String tag){

        int tagMatches = 0;
        for (MineFactory factory : factories)
            if (factory.resTag.equalsIgnoreCase(tag)) tagMatches++;

        MineFactory[] matches = new MineFactory[tagMatches];
        int j = 0;
        for (MineFactory factory : factories)
            if (factory.resTag.equalsIgnoreCase(tag)){
                matches[j] = factory;
                j++;
            }

        return matches;

    }

    public MineFactory[] getNonEmptyMines(){

        int nonEmpties = 0;
        for (MineFactory mine : mines)
            if (mine.resCount > 0) nonEmpties++;

        MineFactory[] nonEmptyMines = new MineFactory[nonEmpties];
        int j = 0;
        for (MineFactory mine : mines) {
            if (mine.resCount > 0)
                nonEmptyMines[j++] = mine;
        }

        return nonEmptyMines;

    }

}
