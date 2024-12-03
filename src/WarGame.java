import java.io.IOException;
import java.util.Scanner;
/*
 * 
 * current issues:
 * 3. change color of current unit

 */
public class WarGame {
    final static int CELLSIZE = 50;
    final static int WIDTH = 1200;
    final static int HEIGHT = 800;
    final static int MAXX = WIDTH / CELLSIZE;
    final static int MAXY = HEIGHT / CELLSIZE;

    public static void main(String[] args) {
        String fileOne = "ipg.txt" ;
        String fileTwo = "enemy.txt" ;
        try {
            // Initialize the game with file names or any initial setup you need
            WarGameLogic theGame = new WarGameGUI(WIDTH, HEIGHT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
