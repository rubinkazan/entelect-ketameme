package entelect;
import java.util.Scanner;
public class Entelect {

    public static void main(String[] args) {
        Controller boss=new Controller();
        Worker first=new Worker(1,2,3);
        
        boss.readInput();
        boss.move(1,3);
        boss.goToMine(1,2);
        boss.goToFactory(1,5);
        
        int mine=boss.getMineByID(5);
        int factory=boss.getFactoryByID(5);
        
        boss.printSolution();
    }
    
}
