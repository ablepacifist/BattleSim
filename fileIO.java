import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

class fileIO {
    static ArrayList<String> lines = new ArrayList<String>();

    // Store current game state in a txt format
    public static void saveOne(String filename, set army) {
        System.out.println("saving game..."); // Example filename: C:\\Users\\Owner\\Documents\\DnD\\warGame
        System.out.println(filename);
        String destination = filename;
        String line = null;
        try {
            File f1 = new File(destination);
            FileReader fr = new FileReader(f1);
            set.NodeInterface next = army.getTopNode();
            lines.add(army.getName() + "\n");
            while (next.getLink() != army.getLast()) {
                next = next.getLink();
                line = next.getUnit().getType() + "," + next.getUnit().getCurrentHealth() + ","
                        + next.getUnit().getExhausted() + "," + next.getUnit().getName() + "\n";
                lines.add(line);
            }
            FileWriter fw = new FileWriter(f1);
            for (String ln : lines) {
                fw.append(ln);
                System.out.println(ln);
            }
            fw.flush();
            fr.close();
            fw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.print("done");
    }

    // Method to fill army from a file
    public static void fillArmyFromFile(String filename, set army, battle currentBattle) {
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

                unit toInsertUnit = null;
                if (token[0].equalsIgnoreCase("swordsmen")) {
                    toInsertUnit = new swordsman(Integer.parseInt(token[1]), token[3], Double.parseDouble(token[2]), token[4]);
                } else if (token[0].equalsIgnoreCase("spearmen")) {
                    toInsertUnit = new spearmen(Integer.parseInt(token[1]), token[3], Double.parseDouble(token[2]), token[4]);
                } else if (token[0].equalsIgnoreCase("archer")) {
                    toInsertUnit = new archer(Integer.parseInt(token[1]), token[3], Double.parseDouble(token[2]), token[4]);
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
            System.out.println("running defaults due to IOException");
        }
    }
}
