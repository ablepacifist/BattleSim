import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Battle {
   private Set armyOne;
   private Set armyTwo;
   private Set turnOrder;
   private int counter;
   private Field battleField;



   public Battle(String filenameTwo, String filenameOne, int cellSize) throws IOException {
       this.armyOne = new Set();
       armyOne.insertName("idiots play games");
       this.armyTwo = new Set();
       armyTwo.insertName("null for now");
       this.counter = 1;
       fillArmyFromFile(filenameTwo, this.armyTwo);
       fillArmyFromFile(filenameOne, this.armyOne);
       this.battleField = new Field( Set.Union(this.armyOne, this.armyTwo),cellSize); // change max values?

   }

   // defult contructor
   // fills in defult battle armies
   public Battle(int cellSize) {
      this.armyOne = new Set();
      armyOne.insertName("Tongass gillroy");
      this.armyTwo = new Set();
      armyTwo.insertName("idiots play games");
      fillArmy();
      this.battleField = new Field( Set.Union(armyOne,armyTwo),cellSize); // change max values?
   }
// fill defult army
   private void fillArmy() {
      this.armyTwo.insert(1, new Swordsmen(2000, "U1","U1"));
      this.armyTwo.insert(2, new Spearmen(4000, "U2","U2"));
      this.armyTwo.insert(3, new Archer(2000, "U3","U3"));
      // this.armyTwo.insert(4,new cavalry(1000, "U1"));
      this.armyTwo.insert(5, new Archer(1000, "U4","U4"));
      // ----------------------------------
      this.armyOne.insert(101, new Swordsmen(2000, "devon","D1"));
      this.armyOne.insert(102, new Spearmen(4000, "sam","S1"));
      this.armyOne.insert(103, new Archer(2000, "devon","D2"));
      // this.armyOne.insert(104,new cavalry(1000, "U1"));
      this.armyOne.insert(105, new Archer(1000, "hunter","H1"));
   }
    // Getter for counter
    public int getCounter() {
      return counter;
  }

  // Setter for counter
  public void incrementCounter() {
      this.counter++;
  }
   @SuppressWarnings("static-access")
   // takes the two armies and calculates their turn order by creating
   // a new set from thier union. sorted by speed.
   public Set turnOrder() {
      this.turnOrder = this.armyOne.Union(armyTwo, armyOne);
      return turnOrder;
   }
   // fill armies from txt file. given destination
   public void fillArmyFromFile(String filename, Set army) {
      try {
         if (army == null) {
         } else {
            // Create a new Scanner with an input stream from a file to get input
            // from a file. This throws an IOException that you should handle.
            Unit toInsertUnit = army.getTopNode().getUnit();
            try (Scanner theFile = new Scanner(Files.newInputStream(Path.of(filename)))) {
               String read_word;
               String[] token;
               // First line of txt file is always name of the army
               read_word = theFile.nextLine();
               token = read_word.split(",");
               army.insertName(read_word);
               // txt file format:
               // unitType,health,exhausted,unitName
               // iterate over file and insert all into armyTwo
               while (theFile.hasNextLine()) {
                  read_word = theFile.nextLine();
                  token = read_word.split(",");
                  // check type and create unit
                  if (token[0].equalsIgnoreCase("swordsmen")) {
                     toInsertUnit = new Swordsmen(Integer.parseInt(token[1]), token[3], Double.parseDouble(token[2]),token[4]);
                  } else if (token[0].equalsIgnoreCase("spearmen")) {
                     toInsertUnit = new Spearmen(Integer.parseInt(token[1]), token[3], Double.parseDouble(token[2]),token[4]);
                  } else if (token[0].equalsIgnoreCase("archer")) {
                     toInsertUnit = new Archer(Integer.parseInt(token[1]), token[3], Double.parseDouble(token[2]),token[4]);
                  }
                  toInsertUnit.setX(Integer.parseInt(token[5]));
                  toInsertUnit.setY(Integer.parseInt(token[6]));
                  // insert unit and change id
                  army.insert(this.counter, toInsertUnit);
                  this.counter++;
               }
            } catch (NumberFormatException e) {
               e.printStackTrace();
            }
         }
      } catch (IOException e) {
         // this occurs on a exeption in reading the file. usualy wrong file name
         // go with defult values then
         fillArmy();
         System.out.println("running defults");
      }
   }

   // getter
   public Set getA1() {
      return armyOne;
   }

   public Set getA2() {
      return armyTwo;
   }
   public Field getBattleFeild() {
      return this.battleField;
   }
}