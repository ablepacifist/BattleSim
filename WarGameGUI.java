import javax.swing.*;

public class WarGameGUI extends JFrame {
    private Field battleField;

    public WarGameGUI(Battle battle,int width, int height) {
        this.battleField = battle.getBattleFeild();
        add(battleField);

        setTitle("War Game");
        setSize(width, height); // Adjust size as needed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void updateField() {
        battleField.repaint();
    }
    public void closeField() {
        this.dispose(); // Close the window
    }
}
