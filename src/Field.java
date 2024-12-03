import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Field extends JPanel {
    private int xMax;
    private int yMax;
    private Set units;
    private int cellSize; // Set the size of each cell
    private Unit currentUnit;

    public Field(Set units, int cellSize) {
        this.xMax = 20;
        this.yMax = 20;
        this.units = units;
        this.cellSize = cellSize;
         setPreferredSize(new Dimension(xMax*cellSize, yMax*cellSize));
    }
    public void setCurrentUnit(Unit givenUnit){
        this.currentUnit = givenUnit;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Call the superclass's paintComponent method

        // Draw the board (grid)
        int counterx = 0;
        int countery = 1;
        for (int x = 0; x < getWidth(); x += cellSize) {
            for (int y = 0; y < getHeight(); y += cellSize) {
                g.drawRect(x, y, cellSize, cellSize);
                if (x == 0) {
                    g.drawString(Integer.toString(counterx), x + cellSize / 2, y + cellSize / 2);
                    counterx++;
                } else if (y == 0) {
                    g.drawString(Integer.toString(countery), x + cellSize / 2, y + cellSize / 2);
                    countery++;
                }
            }
        }
        // draw units and target lines
        Set.NodeInterface next = units.getTopNode().getLink();
        while (next != null && next != units.getLastNode()) {
            if(next.getUnit().getCurrentHealth() > 0){
                if(currentUnit != null && next.getUnit().getName().equalsIgnoreCase(currentUnit.getName())){
                    g.setColor(Color.BLUE);
                }else{
            g.setColor(Color.BLACK);
                }
            int x = next.getUnit().getX() * cellSize;
            int y = next.getUnit().getY() * cellSize;
            g.drawString(next.getUnit().getDesignation(), x + cellSize / 2, y + cellSize / 2);
            //draw health:
                float ratio = (float) next.getUnit().getCurrentHealth()/10000;
                int red = (int) ((1-ratio)*225);
                int green = (int)(ratio *225);
                g.setColor(new Color(red,green,0));
        g.drawString("HP: " + next.getUnit().getCurrentHealth(), x + cellSize/100, y + cellSize/2 -(cellSize/5));
            }

            // draw target lines:
            if (next.getUnit().getTarget() != null) {
                int x0 = (next.getUnit().getX() * cellSize) + cellSize / 2;
                int y0 = (next.getUnit().getY() * cellSize) + cellSize / 2;
                int x1 = (next.getUnit().getTarget().getX() * cellSize) + cellSize / 2;
                int y1 = (next.getUnit().getTarget().getY() * cellSize) + cellSize / 2;
                List<Point> points = bresenham(x0, y0, x1, y1);
                drawPoints(points, g, Color.RED);
                // draw arrow
                // draw arrowhead
                int arrowSize = 10;
                int dx = x1 - x0;
                int dy = y1 - y0;
                double D = Math.sqrt(dx * dx + dy * dy);
                double xm = x1 - arrowSize * (dx / D);
                double ym = y1 - arrowSize * (dy / D);
                double xLeft = xm + arrowSize * (dy / D);
                double yLeft = ym - arrowSize * (dx / D);
                double xRight = xm - arrowSize * (dy / D);
                double yRight = ym + arrowSize * (dx / D);

                g.drawLine(x1, y1, (int) xLeft, (int) yLeft);
                g.drawLine(x1, y1, (int) xRight, (int) yRight);
            }
            next = next.getLink();
        }

    }

    public Point unitVector(Point p) {
        double d = Math.sqrt((p.getX() * p.getX()) + (p.getY() * p.getY()));
        return new Point((int) (p.getX() / d), ((int) (p.getY() / d)));
    }

    public int getXMax() {
        return this.xMax;
    }

    public int getYMax() {
        return this.yMax;
    }

    private void drawPoints(List<Point> points, Graphics g, Color c) {
        g.setColor(c);
        for (Point p : points) {
            g.fillRect(p.x, p.y, 1, 1);
        }
    }

    @SuppressWarnings("rawtypes")
    private List bresenham(int x0, int y0, int x1, int y1) {
        List<Point> points = new ArrayList<Point>();
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1; // set direction to pos or negtive
        int sy = y0 < y1 ? 1 : -1; // set direction to pos or negtive
        int error = dx - dy;

        while (true) {
            points.add(new Point(x0, y0));
            if (y0 == y1 && x0 == x1) {
                break;
            }
            int error2 = error * 2;
            if (error2 > -dy) {
                error -= dy;
                x0 += sx;
            }
            if (error2 < dx) {
                error += dx;
                y0 += sy;

            }
        }
        return points;
    }
}
