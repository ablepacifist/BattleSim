import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Scanner;

public class WarGameGUI extends JFrame {
    private enum State {
        betweenTurns, choose_move, choose_target, takeTurn, NONE
    }

    private State currentState = State.NONE;
    final static int CELLSIZE = 50; // each cell is 50ft
    private int width;
    private int height;
    private Field battleField;
    private Battle battle;
    private int turnCounter = 0;
    private String fileNameOne = "";
    private String fileNameTwo = "";
    private JTextField inputField;
    private JTextArea outputArea;
    private String userInput;
    private Timer timer;
    // remember to initalize these two in taketurn():
    private Set.NodeInterface currentUnit;
    private int maxMove;
    private boolean battleBool;

    public WarGameGUI(Battle battle, int width, int height) {
        this.width = width;
        this.height = height;
        setupGUI();
        this.battle = battle;
        this.battleField = battle.getBattleFeild();
        add(battleField, BorderLayout.CENTER); // ????
        initTakeTurn();
        startGame();
    }

    public WarGameGUI(int width, int height) {
        this.width = width;
        this.height = height;
        setupGUI();
        initGUI();
        initTakeTurn();
        startGame();
    }


    private void setupGUI() {
        setTitle("War Game");
        setSize(width, height); // Adjust size as needed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        inputField = new JTextField();
        outputArea = new JTextArea();
        outputArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setPreferredSize(new Dimension(width, 80));
        add(scrollPane, BorderLayout.NORTH);
        JScrollPane fieldScrollPane = new JScrollPane(battleField);
        add(fieldScrollPane,BorderLayout.CENTER);
        add(inputField, BorderLayout.SOUTH);

        setVisible(true);
        // Set focus back to input field
        inputField.requestFocusInWindow();
        // add arrow keys to scroll the feild: }
    }
    public void updateField() {
        // Scroll the output area to the bottom after processing a command 
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
        battleField.repaint();
        revalidate(); // Refresh the JFrame to ensure new component is displayed
    }

    public void closeField() {
        this.dispose(); // Close the window
    }

    private void initGUI() {
        String message = "Input 2 file names (separated by a comma ,):";
        JTextField textField = new JTextField(20);
    
        JPanel panel = new JPanel();
        panel.add(new JLabel(message));
        panel.add(textField);
    
        int result = JOptionPane.showConfirmDialog(null, panel, "File Input", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            userInput = textField.getText();
            processFileInput();
        } else {
            outputArea.append("Input cancelled.\n");
        }
    
        if (battleField != null) {
            JScrollPane fieldScrollPane = new JScrollPane(battleField);
            add(fieldScrollPane, BorderLayout.CENTER);
    
            InputMap inputMap = battleField.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            ActionMap actionMap = battleField.getActionMap();
    
            // Up arrow key
            inputMap.put(KeyStroke.getKeyStroke("UP"), "panUp");
            actionMap.put("panUp", new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    JViewport viewport = fieldScrollPane.getViewport();
                    Point viewPosition = viewport.getViewPosition();
                    viewPosition.translate(0, -CELLSIZE); // Adjust translation size
                    battleField.scrollRectToVisible(new Rectangle(viewPosition, viewport.getSize()));
                }
            });
    
            // Down arrow key
            inputMap.put(KeyStroke.getKeyStroke("DOWN"), "panDown");
            actionMap.put("panDown", new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    JViewport viewport = fieldScrollPane.getViewport();
                    Point viewPosition = viewport.getViewPosition();
                    viewPosition.translate(0, CELLSIZE); // Adjust translation size
                    battleField.scrollRectToVisible(new Rectangle(viewPosition, viewport.getSize()));
                }
            });
    
            // Left arrow key
            inputMap.put(KeyStroke.getKeyStroke("LEFT"), "panLeft");
            actionMap.put("panLeft", new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    JViewport viewport = fieldScrollPane.getViewport();
                    Point viewPosition = viewport.getViewPosition();
                    viewPosition.translate(-CELLSIZE, 0); // Adjust translation size
                    battleField.scrollRectToVisible(new Rectangle(viewPosition, viewport.getSize()));
                }
            });
    
            // Right arrow key
            inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "panRight");
            actionMap.put("panRight", new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    JViewport viewport = fieldScrollPane.getViewport();
                    Point viewPosition = viewport.getViewPosition();
                    viewPosition.translate(CELLSIZE, 0); // Adjust translation size
                    battleField.scrollRectToVisible(new Rectangle(viewPosition, viewport.getSize()));
                }
            });
        } else {
            System.out.println("battleField is null during initGUI.");
        }
    }
    

    private void processFileInput() {
        String[] filenameString = userInput.split(",");
        
        try {
            Battle hills2 = new Battle(filenameString[0], filenameString[1], CELLSIZE);
            fileNameOne = filenameString[0];
            fileNameTwo = filenameString[1];
            this.battle = hills2;
            this.battleField = hills2.getBattleFeild();
            outputArea.append("Files loaded successfully.\n");
            add(battleField, BorderLayout.CENTER);
            revalidate(); // Refresh the JFrame to ensure new component is displayed
            repaint();
            userInput = null;
        } catch (IOException e) {
            outputArea.append("Error loading files. Please try again.\n");
            initGUI(); // prompt again if there's an error
        }
    }

    public void startGame() {
        outputArea.append("Welcome to war! Please enter command (h for help)\n");
        this.currentState = State.betweenTurns;
    
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateField();
            }
        });
        timer.start();
    
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                synchronized (WarGameGUI.this) {
                    userInput = inputField.getText().trim();
                    inputField.setText("");
                    processCommand(userInput);
                    if (!userInput.isEmpty()) {
                        WarGameGUI.this.notify();
                    }
                    // Set focus back to input field
            inputField.requestFocusInWindow();
                }
            }
        });
    }
    
    
    private void initTakeTurn() {
        currentUnit = battle.getTurnOrder().getTopNode().getLink();
                //check if valid:
                if(currentUnit == battle.getTurnOrder().getLastNode()){
                    currentState = State.betweenTurns;
                    currentUnit = battle.getTurnOrder().getTop();
                 }
                 //initalize:
                 battleBool=false;
                 maxMove = currentUnit.getMovementSpeed();

    }
    

    // process the user's input
    private void processCommand(String command) {
        System.out.println("Processing command: " + command + " | Current State: " + currentState);
    
        switch (currentState) {
            case betweenTurns:
                if (command.equalsIgnoreCase("battle") || command.equalsIgnoreCase("s")) {
                    currentState = State.takeTurn;
                    System.out.println("Transition to takeTurn state");
                    takeTurn();
                } else if (command.equalsIgnoreCase("h") || command.equalsIgnoreCase("help")) {
                    help();
                } else if (command.equalsIgnoreCase("save") || command.equalsIgnoreCase("end")) {
                    FileIO fileHandler = new FileIO();
                    closeField();
                    fileHandler.saveOne(fileNameTwo, battle.getA1());
                    fileHandler.saveOne(fileNameOne, battle.getA2());
                    outputArea.append("Data saved\n");
                } else {
                    outputArea.append("Invalid command. Type h for help\n");
                }
                break;
            case takeTurn:
                if (command.equalsIgnoreCase("t") || command.equalsIgnoreCase("target")) {
                    if (!battleBool) {
                        currentState = State.choose_target;
                        outputArea.append("Who is the target? (Use unit name):\n");
                    } else {
                        outputArea.append("Only one attack per turn\n");
                    }
                } else if (command.equalsIgnoreCase("m") || command.equalsIgnoreCase("move")) {
                    handleMoveCommand(command);
                } else if (command.equalsIgnoreCase("s")) {
                    if (!battleBool && currentUnit.getUnit().getTarget() != null) {
                        outputArea.append("did "+currentUnit.getUnit().dealDamage(currentUnit.getUnit().getTarget())+ " damage! \n");
                        //currentUnit.getUnit().dealDamage(currentUnit.getUnit().getTarget());
                        this.battleBool = true;
                    }
                    nextTurn();
                    takeTurn();
                } else if (command.equalsIgnoreCase("h")) {
                    helpTurn(outputArea);
                }else if(command.equalsIgnoreCase("end")){
                    nextTurn();
                    takeTurn();  
                } else {
                    outputArea.append("Invalid command. Try again\n");
                }
                break;
            case choose_move:
                handleMoveCommand(command);
                break;
            case choose_target:
                handleTargetCommand(command);
                break;
        }
    
        //userInput = null; // Reset user input to wait for new input
        System.out.println("Command processed, new state: " + currentState);
    }

    private void handleMoveCommand(String command) {
        updateField();
        String[] toDo = command.split(",");
        Set.NodeInterface next = this.currentUnit;

        if (toDo.length < 2) {
            outputArea.append("To where? (row, col) Unit's movement speed: " + maxMove + "\n");
            this.currentState = State.choose_move;
            return;
        }

        try {
            int newY = Integer.parseInt(toDo[1].trim());
            int newX = Integer.parseInt(toDo[0].trim());
            int yDif = Math.abs(next.getUnit().getY() - newY);
            int xDif = Math.abs(next.getUnit().getX() - newX);
            if ((yDif * CELLSIZE) + (xDif * CELLSIZE) > maxMove) {
                outputArea.append("Can't move that far\n");
            } else if (newX < 1 || newY < 1 || newX > width / CELLSIZE || newY > height / CELLSIZE) {
                outputArea.append("Out of bounds\n");
            } else {
                next.getUnit().setX(newX);
                next.getUnit().setY(newY);
                next.getUnit().setTarget(null);
                updateField();
                this.maxMove = maxMove - ((yDif + xDif) * CELLSIZE);
                outputArea.append("Unit moved to (" + newX + ", " + newY + ")\n");
                //now check if this unit's target is out of range
                if(currentUnit.getUnit().getTarget() != null){
                    Unit theTarget = currentUnit.getUnit().getTarget();
                    if((calculateDistance(currentUnit.getUnit(), theTarget)*CELLSIZE) >currentUnit.getUnit().getRange()){
                        // target is now out of range
                        currentUnit.getUnit().setTarget(null);
                    }
                }
                //now check if there is any units who are targeting this unit is out of range
                Set attackers = battle.getTurnOrder().hasTarget(currentUnit.getUnit());
                Set.NodeInterface nextAttacker =  attackers.getTopNode().getLink();
                while(nextAttacker != attackers.getLastNode()){
                    if((calculateDistance(nextAttacker.getUnit(), currentUnit.getUnit())*CELLSIZE) > nextAttacker.getUnit().getRange()){
                        // current unit is out of range. set attacker to null
                        battle.getTurnOrder().getUnitFromName(nextAttacker.getUnit().getName()).setTarget(null);
                    }
                    nextAttacker = nextAttacker.getLink();
                }
            }
        } catch (Exception e) {
            outputArea.append("\nNeeds to be two numbers >:(\n");
        }

        // After processing the move command, reset the state back to taking commands
        this.currentState = State.takeTurn;
    }

    public void helpTurn(JTextArea outputArea) {
        outputArea.append(
                "\n type \'t\' to pick a target and attack\n type \'m\' to move\n type \'h\' for help\n type \'s\' to skip turn and keep current target");
    }

    private boolean checkForWin(Set a1) {
        Set.NodeInterface next = a1.getTopNode().getLink();
        boolean someOneIsAlive = false;
        while (next != a1.getLastNode()) {
            if (next.getUnit().getCurrentHealth() > 0) {
                someOneIsAlive = true;
            }
            next = next.getLink();
        }
        return !someOneIsAlive;
    }

    public void help() {
        outputArea.append(
                "\nRULES: max unit can be 10k, all units must be given a name, and number that is unique to that unit\n");
        outputArea.append(
                "List of commands: h - for help,\n end - for end game (also should save into a txt file),\n battle - to start fight in appropriate turn order\n");
    }

public void takeTurn(){
     System.out.println("processing turn for: "+ currentUnit.getUnit().getName());
     if(currentUnit == battle.getTurnOrder().getTopNode()){
        outputArea.append("end of turn"); // add turn counter here
        
     }
     else if (currentUnit.getUnit().getCurrentHealth() <= 0) {
        outputArea.append(currentUnit.getUnit().getName() + " bled out all over the battlefield\n");
        updateField();
        System.out.println("Unit is dead, ending turn for: " + currentUnit.getUnit().getName());
    }else{
        outputArea.append("Enter command for unit " + currentUnit.getUnit().getName() + " (move, target, skip, etc.):\n");
    }

}
public void nextTurn(){
        // make turn change
        currentUnit = currentUnit.getLink();
        battleBool=false;
        //check if valid:
        if(currentUnit == battle.getTurnOrder().getLastNode()){
           currentState = State.betweenTurns;
           currentUnit = battle.getTurnOrder().getTop();
        }
        //initalize:
        battleBool=false;
        maxMove = currentUnit.getMovementSpeed();
}
    
    
     
    

    private void handleTargetCommand(String target_name) {
        // Process target command
        Unit target = battle.searchByName(target_name);
        if (target != null) {
            int dist = calculateDistance(currentUnit.getUnit(), target);
            if (dist <= currentUnit.getUnit().getRange() / CELLSIZE) {
                outputArea.append("Correct target\n");
            outputArea.append("did "+currentUnit.getUnit().dealDamage(target)+ " damage! \n");
                currentUnit.getUnit().setTarget(target);
                updateField();
            } else {
                outputArea.append("Target is out of range\n");
            }
        } else {
            outputArea.append("Not a target\n");
        }

        // Reset state to takeTurn after target selection
        this.currentState = State.takeTurn;
    }

    public void endTurn() {
        Set battleOrder = battle.turnOrder();
        Set.NodeInterface next = battleOrder.getTopNode();
        while (next.getLink() != battleOrder.getLast()) {
            next = next.getLink();
            next.getUnit().changeExhausted(0.5);
        }
        System.out.println("Ending the turn, resetting states.");
    outputArea.append("Turn has ended.\n");
    currentState = State.betweenTurns;

    }

    public static int calculateDistance(Unit unit1, Unit unit2) {
        int dx = unit2.getX() - unit1.getX();
        int dy = unit2.getY() - unit1.getY();
        return (int) Math.sqrt(dx * dx + dy * dy);
    }
}