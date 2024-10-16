import java.io.IOException;
import java.util.Scanner;

public class WarGame {
    final static int CELLSIZE = 50; // each cell is 50ft
    final static int WIDTH = 600;
    final static int HEIGHT = 600;
    final static int MAXX = WIDTH / CELLSIZE;
    final static int MAXY = HEIGHT / CELLSIZE;

    public static void main(String[] args) {
        Battle hills = null;
        boolean end = false;
        int turnCounter = 0;
        String fileNameOne = "";
        String fileNameTwo = "";
        // initalize:
        UnitConfig config = new UnitConfig();
        // Prepare the input
        boolean haveFile = false;
        Scanner input = new Scanner(System.in);
        while (!haveFile) {
            System.out.println("Input 2 file names (separated by a comma ,):");
            String filenameString[] = input.nextLine().split(",");

            try {
                hills = new Battle(filenameString[0], filenameString[1], CELLSIZE);
                fileNameOne = filenameString[0];
                fileNameTwo = filenameString[1];
                haveFile = true;
            } catch (IOException e) {
                System.out.println("Error loading files. Please try again.");
            }
        }
        // init GUI
        WarGameGUI gui = new WarGameGUI(hills, 600, 600);
        // Enter command prompt area
        System.out.println("welcome to war! \n please enter comand(h for help)");
        while (!end) {// go until flag is true
            if(checkForWin(hills.getA1())){
                System.out.println("YOU WIN... but at what cost");
            }else if(checkForWin(hills.getA2())){
                System.out.println("YOU LOOSE! git gud skrub");
            }
            String comand[] = input.nextLine().split(" ");
            if (comand[0].equalsIgnoreCase("h")) {// print help
                WarGame.help();
            } else if (comand[0].equalsIgnoreCase("battle") || comand[0].equalsIgnoreCase("s")) {// start the turn
                WarGame.takeTurn(hills, input, fileNameOne, fileNameTwo, gui);
                endTurn(hills);
                turnCounter++;
                System.out.println("end of turn:" + turnCounter);
            } else if (comand[0].equalsIgnoreCase("end")) {// end the game and save
                FileIO fileHandler = new FileIO();
                gui.closeField();
                // Save armyOne to first file
                fileHandler.saveOne(fileNameTwo, hills.getA1());

                // Save armyTwo to second file
                fileHandler.saveOne(fileNameOne, hills.getA2());

                end = true;
            } else if (comand[0].equalsIgnoreCase("init")) {// initalize from .ini

            } else if (comand[0].equalsIgnoreCase("print")) {

            }
        }
    }

// returns true if given army is all dead
    private static boolean checkForWin(Set a1) {
        Set.NodeInterface next = a1.getTopNode().getLink();
        boolean someOneIsAlive = false;
        while(next != a1.getLastNode()){
            if(next.getUnit().getCurrentHealth()>0){
                someOneIsAlive = true;
            }
            next = next.getLink();
        }
        return !someOneIsAlive;

    }


    // just prints the help
    public static void help() {
        System.out.println();
        System.out.println(
                "RULES:\n max unit can be 10k, all units must be given a name,\n and number that is unique to that unit"); 
        System.out.println(
                "list of comands:\n h - for help \n end - for end game(also should save into a txt file)\n battle - to start fight in appropriate turn order\n");
    }

    // go through turn order and ask for input on what that unit did:
    // print stats: hp, type, range, speed, etc
    // ask did move(beguining and end of turn)?
    // attack what target
    public static void takeTurn(Battle iniBattle, Scanner input, String fileNameOne, String fileNameTwo,
            WarGameGUI gui) {
        // Get the new proper order
        Set battleOrder = iniBattle.turnOrder();
        Set.NodeInterface next = battleOrder.getTopNode();

        // Go through the list one by one and make their turn
        while (next != null && next.getLink() != battleOrder.getLast()) {
            // Get basic info
            next = next.getLink(); // The next item in the order
            next.print();
            boolean endTurn = false;
            boolean battleBool = false;
            int maxMove = next.getMovementSpeed();

            while (!endTurn) {
                //check if unit is already dead
                if (next.getUnit().getCurrentHealth() <= 0) {
                    System.err.println(next.getUnit().getName() +" bleed out all over the battle feild");
                    gui.updateField();
                    endTurn = true;
                } else {
                    // List all the things we can do
                    String toDo[] = input.nextLine().split(" ");
                    if (toDo[0].equalsIgnoreCase("s")) { // Skip turn
                        if (!battleBool && next.getUnit().getTarget() != null) {
                            // If there still is a target, this will attack it
                            next.getUnit().dealDamage(next.getUnit().getTarget());
                            battleBool = true;
                        }
                        endTurn = true;
                    } else if (toDo[0].equalsIgnoreCase("h")) { // Help
                        System.out.println(
                                "s- skip/end turn,\nmove-move current unit,\nt- change target(if applicable)\nend - only use if you don't want to attack\n");
                    } else if (toDo[0].equalsIgnoreCase("move") || toDo[0].equalsIgnoreCase("m")) { // Begin with a move
                        move(toDo,input,next,maxMove,gui);
                    } else if (toDo[0].equalsIgnoreCase("t")) { // Target enemy and deal damage
                        if (!battleBool) {
                            System.out.println("Who is the target? (Use unit name):");
                            toDo = input.nextLine().split(" ");
                            Unit target = battleOrder.getUnitFromName(toDo[0]);
                            if (target != null) {
                                // check range:
                                int dist = calculateDistance(next.getUnit(), target);
                                if (dist <= next.getUnit().getRange() / CELLSIZE) {
                                    System.out.println("correct target");
                                    next.getUnit().dealDamage(target);
                                    battleBool = true;
                                    next.getUnit().setTarget(target);
                                    gui.updateField();
                                } else {
                                    System.out.println("Target is out of range");
                                }
                            } else {
                                System.out.println("not a target");
                            }
                        } else {
                            System.out.println("Only one attack per turn");
                        }
                    } else if (toDo[0].equalsIgnoreCase("end")) { // End turn early
                        endTurn = true;
                    } else if (toDo[0].equalsIgnoreCase("print")) {

                    }
                }
            }
        }
    }

    private static void move(String[] toDo, Scanner input, Set.NodeInterface next, int maxMove, WarGameGUI gui) {
        if (toDo.length < 2) { // Need to get how much moved by
            System.out.println("to where?(row,col)\nUnit's movement speed: " + maxMove);
            toDo = input.nextLine().split(",");
            try { // Each square is 50ft
                int newY = Integer.parseInt(toDo[1]);
                int newX = Integer.parseInt(toDo[0]);

                int yDif = Math.abs(next.getUnit().getY() - newY);
                int xDif = Math.abs(next.getUnit().getX() - newX);

                if ((yDif * CELLSIZE) + (xDif * CELLSIZE) > maxMove) {
                    System.out.println("Can't move that far");
                } else if (newX < 1 || newY < 1 || newX > MAXX || newY > MAXY) {
                    System.out.println("Out of bounds");
                } else {
                    next.getUnit().setX(newX);
                    next.getUnit().setY(newY);
                    // change target to null in case they are out of bounds
                    next.getUnit().setTarget(null);
                    gui.updateField();
                    maxMove = maxMove - ((yDif + xDif) * CELLSIZE);
                    System.out.println("Unit moved to (" + newX + ", " + newY + ")");
                }
            } catch (Exception e) {
                System.out.println("Needs to be two numbers number >:(\n");
            }
        }
    }

    public static void endTurn(Battle battleInit) {
        Set battleOrder = battleInit.turnOrder();
        Set.NodeInterface next = battleOrder.getTopNode();
        while (next.getLink() != battleOrder.getLast()) {
            next = next.getLink();
            next.getUnit().changeExhausted(0.5);
        }
    }

    public static int calculateDistance(Unit unit1, Unit unit2) {
        int dx = unit2.getX() - unit1.getX();
        int dy = unit2.getY() - unit1.getY();
        return (int) Math.sqrt(dx * dx + dy * dy);
    }
}
