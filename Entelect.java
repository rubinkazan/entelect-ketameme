import java.util.Scanner;

public class Entelect {

    public static void main(String[] args) {
        Controller controller=new Controller(new Scanner(System.in));

        controller.readInput();

        Worker worker = controller.workers[0];

        for (MineFactory mine : controller.mines){

            while (mine.res > 0){

                controller.gotoMine(worker.uni_id, mine.id);
                controller.gotoFactory(worker.uni_id,
                        controller.getFactoriesByResTag(mine.recTag)[0].id
                );

            }

        }

        controller.printSolution();
    }

}