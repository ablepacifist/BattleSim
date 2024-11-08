import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class FileIO {
     ArrayList<String> lines = new ArrayList<String>();

    // Store current game state in a txt format
    public void saveOne(String filename, Set army) {
        System.out.println("saving game..."); // Example filename: C:\\Users\\Owner\\Documents\\DnD\\warGame
        System.out.println(filename);
        String destination = filename;
        String line;

        try {
            File f1 = new File(destination);
            if (f1.exists() && f1.isFile()) {
                lines.clear(); // Ensure lines is cleared before use
                lines.add(army.getName() + "\n");
                Set.NodeInterface next = army.getTopNode();
                while (next != null && next.getLink() != army.getLast()) {
                    next = next.getLink();
                    line = next.getUnit().getType() + "," + next.getUnit().getCurrentHealth() + ","
                            + next.getUnit().getExhausted() + "," + next.getUnit().getName() + "," + next.getUnit().getDesignation()+ ","+ next.getUnit().getX()+ "," + next.getUnit().getY() + "\n";
                    lines.add(line);
                }
                try (FileWriter fw = new FileWriter(f1)) {
                    for (String ln : lines) {
                        fw.append(ln);
                    }
                }
            } else {
                throw new IOException("File not found or invalid: " + destination);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.print("...Done \n");
    }

    // Method to fill army from a file
    public static void fillArmyFromFile(String filename, Set army, Battle currentBattle) {
        if (army == null) {
            System.out.println("Error: Army object is null.");
            return;
        }

        try (Scanner theFile = new Scanner(Files.newInputStream(Path.of(filename)))) {
            String read_word;
            String[] token;

            // First line of txt file is always name of the army
            read_word = theFile.nextLine();
            token = read_word.split(",");
            army.insertName(read_word);

            // Iterate over file and insert all units into army
            while (theFile.hasNextLine()) {
                read_word = theFile.nextLine();
                token = read_word.split(",");

                Unit toInsertUnit = null;
                if (token[0].equalsIgnoreCase("swordsmen")) {
                    toInsertUnit = new Swordsmen(Integer.parseInt(token[1]), token[3], Double.parseDouble(token[2]), token[4]);
                } else if (token[0].equalsIgnoreCase("spearmen")) {
                    toInsertUnit = new Spearmen(Integer.parseInt(token[1]), token[3], Double.parseDouble(token[2]), token[4]);
                } else if (token[0].equalsIgnoreCase("archer")) {
                    toInsertUnit = new Archer(Integer.parseInt(token[1]), token[3], Double.parseDouble(token[2]), token[4]);
                }else if(token[0].equalsIgnoreCase("cavalry")) {
                    toInsertUnit = new Cavalry(Integer.parseInt(token[1]), token[3], Double.parseDouble(token[2]), token[4]);
                }else if(token[0].equalsIgnoreCase("Catapult")) {
                    toInsertUnit = new Catapult(Integer.parseInt(token[1]), token[3], Double.parseDouble(token[2]), token[4]);
                }

                if (toInsertUnit != null) {
                    toInsertUnit.setX(Integer.parseInt(token[5]));
                    toInsertUnit.setY(Integer.parseInt(token[6]));
                    // Insert unit and change id
                    army.insert(currentBattle.getCounter(), toInsertUnit);
                    currentBattle.incrementCounter();
                } else {
                    System.out.println("Error: Unknown unit type: " + token[0]);
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // This occurs on an exception in reading the file. Usually wrong file name
            System.out.println("This occurs on an exception in reading the file. Usually wrong file name");
        }
    }
}
/*
 * 
Idiots Play Games
Swordsmen,4000,1.0,Sam1,S1,1,9
Spearmen,4000,1.0,Devon1,D1,2,9
Archer,5000,1.0,Sam2,S2,3,9
Archer,5000,1.0,Hunter1,H1,4,9
 * 
 * 
Tongas Gillroy
Swordsmen,4000,1.0,U1,U1,1,1
Spearmen,4000,1.0,U3,U3,2,1
Archer,5000,1.0,U2,U2,3,1
 * 
 * 
 */