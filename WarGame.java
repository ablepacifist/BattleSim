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
    
        try {
            WarGameGUI theGame = new WarGameGUI(WIDTH,HEIGHT);
        } catch (Exception e) {
            e.printStackTrace();
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
}
