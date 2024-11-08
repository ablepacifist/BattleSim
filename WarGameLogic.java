import javax.swing.JFrame;

public class WarGameLogic extends JFrame {
    public interface GameOutput {
        void print(String message);
    }

    public enum State {
        betweenTurns, choose_move, choose_target, takeTurn, NONE
    }

    private Set.NodeInterface currentUnit;
    private int maxMove;
    private boolean battleBool;
    private int turnCounter = 0;
    private State currentState = State.betweenTurns;

    public WarGameLogic(int width, int height) {
            }

    // Initialize the first turn
    private void initTakeTurn() {
        currentUnit = getBattle().getTurnOrder().getTopNode().getLink();
        if (currentUnit == getBattle().getTurnOrder().getLastNode()) {
            currentUnit = getBattle().getTurnOrder().getTop();
        }
        battleBool = false;
        maxMove = currentUnit.getMovementSpeed();
    }

    // Move to the next turn
    public void nextTurn() {
        currentUnit = currentUnit.getLink();
        battleBool = false;
        if (currentUnit == getBattle().getTurnOrder().getLastNode()) {
            currentState = State.betweenTurns;
            currentUnit = getBattle().getTurnOrder().getTop();
        }
        maxMove = currentUnit.getMovementSpeed();
        getBattle().getBattleFeild().setCurrentUnit(currentUnit.getUnit());
    }

    // Process a command for the current unit
    public void processCommand(String command) {
        System.out.println("Processing command: " + command + " | Current State: " + currentState);

        switch (currentState) {
            case betweenTurns:
                if (command.equalsIgnoreCase("battle") || command.equalsIgnoreCase("s")) {
                    if (getBattle() != null) {
                        initTakeTurn();
                    }
                    currentState = State.takeTurn;
                    System.out.println("Transition to takeTurn state");
                    takeTurn();
                } else if (command.equalsIgnoreCase("h") || command.equalsIgnoreCase("help")) {
                    help();
                } else if (command.equalsIgnoreCase("save") || command.equalsIgnoreCase("end")) {
                    saveGame();
                } else {
                    printToGui("Invalid command. Type h for help");
                }
                break;
            case takeTurn:
                if (command.equalsIgnoreCase("t") || command.equalsIgnoreCase("target")) {
                    if (!battleBool) {
                        currentState = State.choose_target;
                        printToGui("Who is the target? (Use unit name):");
                    } else {
                        printToGui("Only one attack per turn");
                    }
                } else if (command.equalsIgnoreCase("m") || command.equalsIgnoreCase("move")) {
                    handleMoveCommand(command);
                } else if (command.equalsIgnoreCase("s")) {
                    if (!battleBool && currentUnit.getUnit().getTarget() != null) {
                        printToGui("Did " + currentUnit.getUnit().dealDamage(currentUnit.getUnit().getTarget())
                                + " damage!");
                        battleBool = true;
                    }
                    nextTurn();
                    takeTurn();
                } else if (command.equalsIgnoreCase("h")) {
                    helpTurn();
                } else if (command.equalsIgnoreCase("end")) {
                    nextTurn();
                    takeTurn();
                } else {
                    printToGui("Invalid command. Try again");
                }
                break;
            case choose_move:
                handleMoveCommand(command);
                break;
            case choose_target:
                handleTargetCommand(command);
                break;
        }

        System.out.println("Command processed, new state: " + currentState);
    }

    private void handleMoveCommand(String command) {
        String[] toDo = command.split(",");
        Set.NodeInterface next = this.currentUnit;

        if (toDo.length < 2) {
            printToGui("To where? (row, col) Unit's movement speed: " + maxMove);
            this.currentState = State.choose_move;
            return;
        }

        try {
            int newY = Integer.parseInt(toDo[1].trim());
            int newX = Integer.parseInt(toDo[0].trim());
            int yDif = Math.abs(next.getUnit().getY() - newY);
            int xDif = Math.abs(next.getUnit().getX() - newX);
            if ((yDif * WarGame.CELLSIZE) + (xDif * WarGame.CELLSIZE) > maxMove) {
                printToGui("Can't move that far");
            } else if (newX < 1 || newY < 1 || newX > WarGame.MAXX || newY > WarGame.MAXY) {
                printToGui("Out of bounds");
            } else {
                next.getUnit().setX(newX);
                next.getUnit().setY(newY);
                next.getUnit().setTarget(null);
                maxMove -= ((yDif + xDif) * WarGame.CELLSIZE);
                printToGui("Unit moved to (" + newX + ", " + newY + ")");

                checkTargetRange();
            }
        } catch (Exception e) {
            printToGui("Needs to be two numbers");
        }

        this.currentState = State.takeTurn;
    }

    private void handleTargetCommand(String target_name) {
        Unit target = getBattle().searchByName(target_name);
        if (target != null) {
            int dist = calculateDistance(currentUnit.getUnit(), target);
            if (dist <= currentUnit.getUnit().getRange() / WarGame.CELLSIZE) {
                System.out.println("Correct target");
                printToGui("Did " + currentUnit.getUnit().dealDamage(target) + " damage!");
                currentUnit.getUnit().setTarget(target);
                battleBool = true;
            } else {
                printToGui("Target is out of range");
            }
        } else {
            printToGui("Not a target");
        }
        this.currentState = State.takeTurn;
    }

    public void takeTurn() {
        if (currentUnit == null) {
            System.out.println("Error: currentUnit is null in takeTurn()");
            return;
        }

        System.out.println("Processing turn for: " + currentUnit.getUnit().getName());
        if (currentUnit == getBattle().getTurnOrder().getTopNode()) {
            printToGui("End of turn");
            turnCounter++;
        } else if (currentUnit.getUnit().getCurrentHealth() <= 0) {
            printToGui(currentUnit.getUnit().getName() + " bled out all over the battlefield");
        } else {
            printToGui("Enter command for unit " + currentUnit.getUnit().getName() + " (move, target, skip, etc.):");
        }
    }

    private void checkTargetRange() {
        if (currentUnit.getUnit().getTarget() != null) {
            Unit theTarget = currentUnit.getUnit().getTarget();
            if ((calculateDistance(currentUnit.getUnit(), theTarget) * WarGame.CELLSIZE) > currentUnit.getUnit()
                    .getRange()) {
                currentUnit.getUnit().setTarget(null);
            }
        }
        Set attackers = getBattle().getTurnOrder().hasTarget(currentUnit.getUnit());
        Set.NodeInterface nextAttacker = attackers.getTopNode().getLink();
        while (nextAttacker != attackers.getLastNode()) {
            if ((calculateDistance(nextAttacker.getUnit(), currentUnit.getUnit()) * WarGame.CELLSIZE) > nextAttacker
                    .getUnit().getRange()) {
                getBattle().getTurnOrder().getUnitFromName(nextAttacker.getUnit().getName()).setTarget(null);
            }
            nextAttacker = nextAttacker.getLink();
        }
    }

    private void saveGame() {
        FileIO fileHandler = new FileIO();
        fileHandler.saveOne(getFileTwo(), getBattle().getA1());
        fileHandler.saveOne(getFileOne(), getBattle().getA2());
        System.out.println("Data saved to:" + getFileOne() + " , " + getFileTwo());
    }

    public Field getBattleField() {
        return this.getBattle().getBattleFeild();
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public void helpTurn() {
         printToGui("\n type 't' to pick a target and attack\n type 'm' to move\n type 'h' for help\n type 's' to skip turn and keep current target\n");
    }

    public void help() {
        printToGui(
                "\nRULES: max unit can be 10k, all units must be given a name, and number that is unique to that unit\n");
        printToGui(
                "List of commands: h - for help,\n end - for end game (also should save into a txt file),\n battle - to start fight in appropriate turn order\n");
    }

    public static int calculateDistance(Unit unit1, Unit unit2) {
        int dx = unit2.getX() - unit1.getX();
        int dy = unit2.getY() - unit1.getY();
        return (int) Math.sqrt(dx * dx + dy * dy);
    }

    public void printToGui(String message) {
        System.out.println(message);
    }

    public Battle getBattle() {
        return new Battle(WarGame.CELLSIZE);
    }

    public String getFileOne() {
        return "ally.txt";
    }

    public String getFileTwo() {
        return "enemy.txt";
    }
}
