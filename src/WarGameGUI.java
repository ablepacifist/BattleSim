import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class WarGameGUI extends WarGameLogic {
    private JTextField inputField;
    private JTextArea outputArea;
    private Timer timer;
    private Field battleField;
    // find a way to remove these please:
    private Battle battle;
    public String fileNameOne = "";
    public String fileNameTwo = "";

    public WarGameGUI( int width, int height ) {
        super(width, height); // Call the parent constructor
        setupGUI(width, height);
        initGUI();
        startGame();
    }

    private void setupGUI(int width, int height) {
        setTitle("War Game");
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        inputField = new JTextField();
        outputArea = new JTextArea();
        outputArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setPreferredSize(new Dimension(width, 80));
        add(scrollPane, BorderLayout.NORTH);
        add(inputField, BorderLayout.SOUTH);

        setVisible(true);
        inputField.requestFocusInWindow();
    }

    private void initGUI() {
        String message = "Input 2 file names (separated by a comma ,). to use defult just press cancel:";
        JTextField textField = new JTextField(20);

        JPanel panel = new JPanel();
        panel.add(new JLabel(message));
        panel.add(textField);

        int result = JOptionPane.showConfirmDialog(null, panel, "File Input", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String userInput = textField.getText();
            System.out.println("start processing file...");
            processFileInput(userInput);
        } else {
            System.out.println("input cancelled");
            outputArea.append("Input cancelled. run defults\n");
            processFileInput("ally.txt,enemy.txt");
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
                    viewPosition.translate(0, -WarGame.CELLSIZE); // Adjust translation size
                    battleField.scrollRectToVisible(new Rectangle(viewPosition, viewport.getSize()));
                }
            });

            // Down arrow key
            inputMap.put(KeyStroke.getKeyStroke("DOWN"), "panDown");
            actionMap.put("panDown", new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    JViewport viewport = fieldScrollPane.getViewport();
                    Point viewPosition = viewport.getViewPosition();
                    viewPosition.translate(0, WarGame.CELLSIZE); // Adjust translation size
                    battleField.scrollRectToVisible(new Rectangle(viewPosition, viewport.getSize()));
                }
            });

            // Left arrow key
            inputMap.put(KeyStroke.getKeyStroke("LEFT"), "panLeft");
            actionMap.put("panLeft", new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    JViewport viewport = fieldScrollPane.getViewport();
                    Point viewPosition = viewport.getViewPosition();
                    viewPosition.translate(-WarGame.CELLSIZE, 0); // Adjust translation size
                    battleField.scrollRectToVisible(new Rectangle(viewPosition, viewport.getSize()));
                }
            });

            // Right arrow key
            inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "panRight");
            actionMap.put("panRight", new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    JViewport viewport = fieldScrollPane.getViewport();
                    Point viewPosition = viewport.getViewPosition();
                    viewPosition.translate(WarGame.CELLSIZE, 0); // Adjust translation size
                    battleField.scrollRectToVisible(new Rectangle(viewPosition, viewport.getSize()));
                }
            });
        } else {
            System.out.println("battleField is null during initGUI.");
        }
    }

    private void processFileInput(String userInput) {
        String[] filenameString = userInput.split(",");
        
        try {
            this. battle = new Battle(filenameString[0], filenameString[1], WarGame.CELLSIZE);
            this.battleField = battle.getBattleFeild();
            outputArea.append("Files loaded successfully.\n");
            this.fileNameOne = filenameString[0];
            this.fileNameTwo = filenameString[1];
            add(battleField, BorderLayout.CENTER);
            revalidate(); // Refresh the JFrame to ensure new component is displayed
            repaint();
        } catch (IOException e) {
            outputArea.append("Error loading files. Please try again.\n");
            initGUI(); // Prompt again if there's an error
        }
    }

    private void startGame() {
        outputArea.append("Welcome to war! Please enter command (h for help)\n");

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
                String command = inputField.getText().trim();
                inputField.setText("");
                processCommand(command);
                inputField.requestFocusInWindow();
            }
        });
    }

    private void updateField() {
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
        if (battleField != null) {
            battleField.repaint();
        }
        revalidate();
    }
    @Override
    public void printToGui(String message) {
        outputArea.append(message + "\n");
    }

    public void closeField() {
        this.dispose();
    }
    @Override
    public Battle getBattle() {
      return this.battle;
    }
    @Override
    public String getFileOne(){
        return this.fileNameOne;
    }
    @Override
    public String getFileTwo(){
        return this.fileNameTwo;
    }
}
