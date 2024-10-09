import javax.swing.*;
import java.awt.*;

public class feild extends JPanel {
    private int xMax;
    private int yMax;
    private set units;

    public feild(int xMax, int yMax, set units) {
        this.xMax = xMax;
        this.yMax = yMax;
        this.units = units;
        setPreferredSize(new Dimension(xMax, yMax));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Call the superclass's paintComponent method

        // Draw the board (grid)
        int cellSize = 50; // Set the size of each cell
        for (int x = 0; x < xMax; x += cellSize) {
            for (int y = 0; y < yMax; y += cellSize) {
                g.drawRect(x, y, cellSize, cellSize);
            }
        }
        // draw units
        set.NodeInterface next = units.getTopNode().getLink();
        while (next != null && next != units.getLastNode()) {
            int x = next.getUnit().getX() * cellSize;
            int y = next.getUnit().getY() * cellSize;
            g.drawString(next.getUnit().getDesignation(),x + cellSize/2, y+cellSize/2);
            next = next.getLink();
        }
    }
    /*
     * 
     * for (unit u : units) {
     * int x = u.getX() * cellSize;
     * int y = u.getY() * cellSize;
     * g.drawString(u.getDesignation(), x + cellSize / 2, y + cellSize / 2);
     * }
     */

}
