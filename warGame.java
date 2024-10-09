import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFrame;

public class warGame {

    public static void main(String[] args) {
        battle hills = new battle();
        boolean end = false;
        int turnCounter = 0;
        String fileNameOne = "";
        String fileNameTwo = "";
        // prepare the input
        Scanner input = new Scanner(System.in);
        System.out.println("input 2 file names(separated by a comma ,):");
        String filenameString[] = input.nextLine().split(",");
        // make sure there is no errors in file name
        try {
            hills = new battle(filenameString[0], filenameString[1]);
            fileNameOne = filenameString[0];
            fileNameTwo = filenameString[1];
        } catch (IOException e) {
            // call battle with no args here??
        }
        // draw the board:
        // Enter command prompt area
        System.out.println("welcome to war! \n please enter comand(h for help)");
        while (!end) {// go until flag is true
            String comand[] = input.nextLine().split(" ");
            if (comand[0].equalsIgnoreCase("h")) {// print help
                warGame.help();
            } else if (comand[0].equalsIgnoreCase("battle") || comand[0].equalsIgnoreCase("s")) {// start the turn
                warGame.takeTurn(hills, input, fileNameOne, fileNameTwo);
                endTurn(hills);
                turnCounter++;
                System.out.println("end of turn:" + turnCounter);
            } else if (comand[0].equalsIgnoreCase("end")) {// end the game and save
                fileIO fileHandler = new fileIO();

                // Save armyOne to first file
                fileHandler.saveOne(fileNameTwo, hills.getA1());
            
                // Save armyTwo to second file
                fileHandler.saveOne(fileNameOne, hills.getA2());
            
                end = true;
            } else if (comand[0].equalsIgnoreCase("init")) {// for now init is not needed
            } else if (comand[0].equalsIgnoreCase("print")) {
                display(hills);
            }
        }
    }

    private static void display(battle hills) {
                // Display the field
                JFrame frame = new JFrame("Board");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                feild board = new feild(500, 500, set.Union(hills.getA1(), hills.getA2())); // Create a 500x500 board
                frame.add(board);
                frame.pack();
                frame.setVisible(true);
    }

    // just prints the help
    public static void help() {
        System.out.println();
        System.out.println(
                "RULES:\n max unit can be 10k, all units must be given a name,\n and number that is unique to that unit"); // \n
                                                                                                                           // print
                                                                                                                           // -
                                                                                                                           // will
                                                                                                                           // print
                                                                                                                           // out
                                                                                                                           // the
                                                                                                                           // current
                                                                                                                           // feild
        System.out.println(
                "list of comands:\n h - for help \n end - for end game(also should save into a txt file)\n battle - to start fight in appropriate turn order\n");
    }

    // go through turn order and ask for input on what that unit did:
    // print stats: hp, type, range, speed, etc
    // ask did move(beguining and end of turn)?
    // attack what target
    public static void takeTurn(battle iniBattle, Scanner input, String fileNameOne, String fileNameTwo) {
        // Get the new proper order
        set battleOrder = iniBattle.turnOrder();
        set.NodeInterface next = battleOrder.getTopNode();

        // Go through the list one by one and make their turn
        while (next != null && next.getLink() != battleOrder.getLast()) {
            // Get basic info
            next = next.getLink(); // The next item in the order
            next.print();
            boolean endTurn = false;
            boolean battleBool = false;
            int maxMove = next.getMovementSpeed();

            while (!endTurn) {
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
                    if (toDo.length < 2) { // Need to get how much moved by
                        System.out.println("By how much (in feet)\nUnit's movement speed: " + maxMove);
                        toDo = input.nextLine().split(" ");
                        try {
                            if (maxMove >= Integer.parseInt(toDo[0])) {
                                double a = (Integer.parseInt(toDo[0]));
                                double b = maxMove;
                                next.getUnit().changeExhausted(-(a / b)); // If full move units are exhausted
                                maxMove = maxMove - Integer.parseInt(toDo[0]);
                                next.getUnit().setTarget(null);

                                // Check if moved out of range of anyone
                                set outOfRange = battleOrder.hasTarget(next.getUnit());
                                set.NodeInterface askAbout = outOfRange.getTopNode();
                                askAbout = askAbout.getLink();
                                while (askAbout != outOfRange.getLast()) {
                                    // Ask user if current unit is still in range. Must change this later
                                    System.out.println("Is " + next.getUnit().getName() + " still in range of "
                                            + askAbout.getUnit().getName() + " ?(y/n):");
                                    toDo = input.nextLine().split(" ");
                                    if (toDo[0].equalsIgnoreCase("yes") || toDo[0].equalsIgnoreCase("y")) {
                                        // Do nothing
                                    } else {
                                        // Change target to no one
                                        askAbout.getUnit().setTarget(null);
                                    }
                                    askAbout = askAbout.getLink();
                                }
                            } else {
                                System.out.println("Can't move that far");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Needs to be a number >:(\n");
                        }
                    }
                } else if (toDo[0].equalsIgnoreCase("t")) { // Target enemy and deal damage
                    if (!battleBool) {
                        System.out.println("Who is the target? (Use unit name):");
                        toDo = input.nextLine().split(" ");
                        unit target = battleOrder.getUnitFromName(toDo[0]);
                        if (target != null) {
                            System.out.println("correct target");
                            next.getUnit().dealDamage(target);
                            battleBool = true;
                            next.getUnit().setTarget(target);
                        } else{
                            System.out.println("not a target");
                        }
                    } else {
                        System.out.println("Only one attack per turn");
                    }
                } else if (toDo[0].equalsIgnoreCase("end")) { // End turn early
                    endTurn = true;
                } else if (toDo[0].equalsIgnoreCase("print")) {
                    // iniBattle.getBattleFeild().printFeild(); 
                    // display(initBattle);
                }
            }
        }
    }

    public static void endTurn(battle battleInit) {
        set battleOrder = battleInit.turnOrder();
        set.NodeInterface next = battleOrder.getTopNode();
        while (next.getLink() != battleOrder.getLast()) {
            next = next.getLink();
            next.getUnit().changeExhausted(0.5);
        }
    }
}
