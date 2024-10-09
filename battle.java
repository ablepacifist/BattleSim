import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class battle {
   private set armyOne;
   private set armyTwo;
   private set turnOrder;
   private int counter;
   private feild battlefFeild;

   public battle(String filenameTwo, String filenameOne) throws IOException {
      this.armyOne = new set();
      armyOne.insertName("idiots play games");
      this.armyTwo = new set();
      armyTwo.insertName("null for now");
      this.counter = 1;
      this.battlefFeild = new feild(20, 20, set.Union(this.armyOne,this.armyTwo)); // change max values?
      fillArmyFromFile(filenameTwo, this.armyTwo);
      fillArmyFromFile(filenameOne, this.armyOne);
   }

   // defult contructor
   // fills in defult battle armies
   public battle() {
      this.armyOne = new set();
      armyOne.insertName("Tongass gillroy");
      this.armyTwo = new set();
      armyTwo.insertName("idiots play games");
      fillArmy();
      this.battlefFeild = new feild(20, 20,set.Union(armyOne,armyTwo)); // change max values?
   }
// fill defult army
   private void fillArmy() {
      this.armyTwo.insert(1, new swordsman(2000, "U1","U1"));
      this.armyTwo.insert(2, new spearmen(4000, "U2","U2"));
      this.armyTwo.insert(3, new archer(2000, "U3","U3"));
      // this.armyTwo.insert(4,new cavalry(1000, "U1"));
      this.armyTwo.insert(5, new archer(1000, "U4","U4"));
      // ----------------------------------
      this.armyOne.insert(101, new swordsman(2000, "devon","D1"));
      this.armyOne.insert(102, new spearmen(4000, "sam","S1"));
      this.armyOne.insert(103, new archer(2000, "devon","D2"));
      // this.armyOne.insert(104,new cavalry(1000, "U1"));
      this.armyOne.insert(105, new archer(1000, "hunter","H1"));
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
   public set turnOrder() {
      this.turnOrder = this.armyOne.Union(armyTwo, armyOne);
      return turnOrder;
   }
   // fill armies from txt file. given destination
   public void fillArmyFromFile(String filename, set army) {
      try {
         if (army == null) {
         } else {
            // Create a new Scanner with an input stream from a file to get input
            // from a file. This throws an IOException that you should handle.
            unit toInsertUnit = army.getTopNode().getUnit();
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
                     toInsertUnit = new swordsman(Integer.parseInt(token[1]), token[3], Double.parseDouble(token[2]),token[4]);
                  } else if (token[0].equalsIgnoreCase("spearmen")) {
                     toInsertUnit = new spearmen(Integer.parseInt(token[1]), token[3], Double.parseDouble(token[2]),token[4]);
                  } else if (token[0].equalsIgnoreCase("archer")) {
                     toInsertUnit = new archer(Integer.parseInt(token[1]), token[3], Double.parseDouble(token[2]),token[4]);
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
   public set getA1() {
      return armyOne;
   }

   public set getA2() {
      return armyTwo;
   }
   public feild getBattleFeild() {
      return this.battlefFeild;
   }
}