import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Scanner;

public class WarGameGUI extends JFrame {
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

    public WarGameGUI(Battle battle, int width, int height) {
        this.width = width;
        this.height = height;
        this.battleField = battle.getBattleFeild();
        this.battle = battle;
        setupGUI();
    }

    public WarGameGUI(int width, int height) {
        this.width = width;
        this.height = height;
        setupGUI();
        initGUI();
        this.battleField = battle.getBattleFeild();
        betweenTurns();
    }

    private void setupGUI() {
        setTitle("War Game");
        setSize(width, height); // Adjust size as needed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        inputField = new JTextField();
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userInput = inputField.getText();
                inputField.setText("");
                processInput(userInput);
            }
        });
        
        add(new JScrollPane(outputArea), BorderLayout.CENTER);
        add(inputField, BorderLayout.SOUTH);
        
        setVisible(true);
    }

    public void updateField() {
        battleField.repaint();
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
    
        int result = JOptionPane.showConfirmDialog(null, panel, "File Input", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            userInput = textField.getText();
            processUserInput();
        } else {
            outputArea.append("Input cancelled.\n");
        }
    }
    
    private void processUserInput() {
        String[] filenameString = userInput.split(",");
        try {
            Battle hills2 = new Battle(filenameString[0], filenameString[1], CELLSIZE);
            fileNameOne = filenameString[0];
            fileNameTwo = filenameString[1];
            this.battle = hills2;
            outputArea.append("Files loaded successfully.\n");
        } catch (IOException e) {
            outputArea.append("Error loading files. Please try again.\n");
            initGUI(); // prompt again if there's an error
        }
    }

    public void betweenTurns() {
        outputArea.append("Welcome to war! Please enter command (h for help)\n");
        boolean end = false;
    
        while (!end) {
            if (checkForWin(battle.getA1())) {
                outputArea.append("YOU WIN... but at what cost\n");
            } else if (checkForWin(battle.getA2())) {
                outputArea.append("YOU LOSE! Git gud scrub\n");
            }
    
            // Wait for user input
            if (userInput != null) {
                String[] command = userInput.split(" ");
                if (command[0].equalsIgnoreCase("h")) {
                    help();
                } else if (command[0].equalsIgnoreCase("battle") || command[0].equalsIgnoreCase("s")) {
                    takeTurn();
                    endTurn();
                    turnCounter++;
                    outputArea.append("End of turn: " + turnCounter + "\n");
                } else if (command[0].equalsIgnoreCase("end")) {
                    FileIO fileHandler = new FileIO();
                    closeField();
                    fileHandler.saveOne(fileNameTwo, battle.getA1());
                    fileHandler.saveOne(fileNameOne, battle.getA2());
                    end = true;
                }
                userInput = null; // Reset user input to re-prompt
            }
    
            // Optional: Add a short sleep to prevent CPU overuse
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    

    private void processInput(String input) {
        userInput = input;
        outputArea.append("Input received: " + input + "\n");
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
        outputArea.append("\nRULES: max unit can be 10k, all units must be given a name, and number that is unique to that unit\n");
        outputArea.append("List of commands: h - for help, end - for end game (also should save into a txt file), battle - to start fight in appropriate turn order\n");
    }
    public void takeTurn() {
        Set battleOrder = battle.turnOrder();
        Set.NodeInterface next = battleOrder.getTopNode();
    
        while (next != null && next.getLink() != battleOrder.getLast()) {
            next = next.getLink();
            next.print();
            boolean endTurn = false;
            boolean battleBool = false;
            int maxMove = next.getMovementSpeed();
    
            while (!endTurn) {
                if (next.getUnit().getCurrentHealth() <= 0) {
                    outputArea.append(next.getUnit().getName() + " bled out all over the battlefield\n");
                    updateField();
                    endTurn = true;
                } else {
                    // Wait for user input
                    if (userInput != null) {
                        String[] toDo = userInput.split(" ");
                        if (toDo[0].equalsIgnoreCase("s")) {
                            if (!battleBool && next.getUnit().getTarget() != null) {
                                next.getUnit().dealDamage(next.getUnit().getTarget());
                                battleBool = true;
                            }
                            endTurn = true;
                        } else if (toDo[0].equalsIgnoreCase("h")) {
                            outputArea.append(
                                "s- skip/end turn, move-move current unit, t- change target (if applicable), end - only use if you don't want to attack\n");
                        } else if (toDo[0].equalsIgnoreCase("move") || toDo[0].equalsIgnoreCase("m")) {
                            userInput = null;
                            move(toDo, maxMove, next);
                        } else if (toDo[0].equalsIgnoreCase("t")) {
                            if (!battleBool) {
                                userInput = null;
                                outputArea.append("Who is the target? (Use unit name):\n");
                                while (userInput == null) {
                                    // Waiting for user input
                                }
                                toDo = userInput.split(" ");
                                Unit target = battleOrder.getUnitFromName(toDo[0]);
                                if (target != null) {
                                    int dist = calculateDistance(next.getUnit(), target);
                                    if (dist <= next.getUnit().getRange() / CELLSIZE) {
                                        outputArea.append("Correct target\n");
                                        next.getUnit().dealDamage(target);
                                        battleBool = true;
                                        next.getUnit().setTarget(target);
                                        updateField();
                                    } else {
                                        outputArea.append("Target is out of range\n");
                                    }
                                } else {
                                    outputArea.append("Not a target\n");
                                }
                            } else {
                                outputArea.append("Only one attack per turn\n");
                            }
                        } else if (toDo[0].equalsIgnoreCase("end")) {
                            endTurn = true;
                        }
                        userInput = null; // Reset user input
                    }
    
                    // Optional: Add a short sleep to prevent CPU overuse
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }
    

    private void move(String[] toDo, int maxMove, Set.NodeInterface next) {
        userInput = null;
        if (toDo.length < 2) {
            updateCommandArea("To where? (row, col) Unit's movement speed: " + maxMove);
            while (userInput == null) {
                try {
                    Thread.sleep(100); // Short sleep to avoid CPU overuse
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            toDo = userInput.split(",");
            userInput = null; // Reset user input to re-prompt
        }
        
        try {
            int newY = Integer.parseInt(toDo[1]);
            int newX = Integer.parseInt(toDo[0]);
            int yDif = Math.abs(next.getUnit().getY() - newY);
            int xDif = Math.abs(next.getUnit().getX() - newX);
            if ((yDif * CELLSIZE) + (xDif * CELLSIZE) > maxMove) {
                updateCommandArea("Can't move that far");
            } else if (newX < 1 || newY < 1 || newX > width / CELLSIZE || newY > height / CELLSIZE) {
                updateCommandArea("Out of bounds");
            } else {
                next.getUnit().setX(newX);
                next.getUnit().setY(newY);
                next.getUnit().setTarget(null);
                updateField();
                maxMove = maxMove - ((yDif + xDif) * CELLSIZE);
                updateCommandArea("Unit moved to (" + newX + ", " + newY + ")");
            }
        } catch (Exception e) {
            updateCommandArea("Needs to be two numbers >:(\n");
        }
    }
    
    
    private void updateCommandArea(String message) {
        outputArea.append(message + "\n");
    }
    

    public void endTurn() {
        Set battleOrder = battle.turnOrder();
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
