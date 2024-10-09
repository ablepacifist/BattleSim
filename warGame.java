import java.io.IOException;
import java.util.Scanner;
public class warGame {
    public static void main(String[] args) {
        battle hills= new battle();
        boolean end = false;
        int turnCounter = 0;
        boolean init = false;
        String fileNameOne="";
        String fileNameTwo="";
        // prepare the input
        Scanner input = new Scanner(System.in);
        System.out.println("input 2 file names(separated by a comma ,):");
        String filenameString[] = input.nextLine().split(",");
        // make sure there is no errors in file name
            try {
                hills= new battle(filenameString[0],filenameString[1]);
                fileNameOne = filenameString[0];
                fileNameTwo = filenameString[1];
            } catch (IOException e) {
                // call battle with no args here??
            }
        //Enter command prompt area
        System.out.println("welcome to war! \n please enter comand(h for help)");
        while(!end){// go until flag is true
           String comand[] = input.nextLine().split(" ");
           if(comand[0].equalsIgnoreCase("h")){// print help
            warGame.help();
           }else if(comand[0].equalsIgnoreCase("battle")){// start the turn
            warGame.takeTurn(hills,input,fileNameOne,fileNameTwo);
            endTurn(hills);
            turnCounter++;
            System.out.println("end of turn:"+turnCounter);
           }else if(comand[0].equalsIgnoreCase("end")){// end the game and save
            // the file names and armies are backwards. need to fix?
            fileIO.save(fileNameOne,hills.getA2());
            fileIO.save(fileNameTwo,hills.getA1());
            end = true;
           }else if(comand[0].equalsIgnoreCase("init")){// for now init is not needed
           }
        }
    }
    // just prints the help
    public static void help(){
        System.out.println();
        System.out.println("RULES:\n max unit can be 10k, all units must be given a name,\n and number that is unique to that unit");
        System.out.println("list of comands:\n h - for help \n end - for end game(also should save into a txt file)\n battle - to start fight in appropriate turn order\n");
    }
    // go through turn order and ask for input on what that unit did:
    // print stats: hp, type, range, speed, etc
    // ask did move(beguining and end of turn)?
    // attack what target
        public static void takeTurn(battle iniBattle, Scanner input,String fileNameOne,String fileNameTwo){
            // get the new proper order:
        set battleOrder = iniBattle.turnOrder();
        set.Node next = battleOrder.getTop();
        // go through the list one by one and make their turn
        while(next.getLink() != battleOrder.getLast()){
            // get basic info:
            next = next.getLink(); // the next item in the order
            next.print();
            boolean endTurn= false;
            boolean battleBool =false;
            int maxMove = next.getMovementSpeed();
            while(!endTurn){
                // list all the things we can do:
                String toDo[] = input.nextLine().split(" ");
                if(toDo[0].equalsIgnoreCase("s")){//skip turn
                    if(!battleBool && next.getUnit().getTarget() != null){
                        // if there still is a target this will attack it
                        next.getUnit().dealDamage(next.getUnit().getTarget());
                        battleBool=true;
                    }
                    endTurn=true;
                }else if(toDo[0].equalsIgnoreCase("h")){//help
                    System.out.println("s- skip/end turn,\nmove-move current unit,\nt- change target(if appicable)\nend - only use if you dont want to attack\n");
                }else if(toDo[0].equalsIgnoreCase("move") || toDo[0].equalsIgnoreCase("m")){// beguin with a move
                    if(toDo.length < 2){//need to get how much moved by
                        System.out.println("by how much(in feet)\n units movement speed:"+maxMove);
                        toDo = input.nextLine().split(" ");
                        try{
                           if(maxMove>=Integer.parseInt(toDo[0])){
                                double a =(Integer.parseInt(toDo[0]));
                                double b =maxMove;
                                next.getUnit().setExhausted(-(a/b));// if full move units are exhausted 
                                maxMove = maxMove - Integer.parseInt(toDo[0]);
                                next.getUnit().setTarget(null);
                                System.out.println("Note: must retarget after every move\nunit movement speed:"+maxMove);
                                //check if moved out of range of anyone:
                                set outOfRange =battleOrder.hasTarget(next.getUnit());
                                set.Node askAbout =outOfRange.getTop();
                                askAbout = askAbout.getLink();
                                while(askAbout!=outOfRange.getLast()){
                                    // ask user if current unit is still in range. must change this later
                                    System.out.println("is "+next.getUnit().getName()+" still in range of "+askAbout.getUnit().getName()+" ?(y/n):");
                                    toDo = input.nextLine().split(" ");
                                    if(toDo[0].equalsIgnoreCase("yes") || toDo[0].equalsIgnoreCase("y")){
                                        // do nothing
                                    }else{
                                        //change target to no one
                                        askAbout.getUnit().setTarget(null);
                                    }
                                    askAbout= askAbout.getLink();
                                }
                            }else{
                            System.out.println("cant move that far");
                            }
                        }catch(java.lang.NumberFormatException ohNo){
                            System.out.println("needs to be a number >:(\n");
                        }
                    }
                }else if(toDo[0].equalsIgnoreCase("t")){// target enemy and deal damage
                    if(!battleBool){
                        System.out.println("who is the target?(use unit name):");
                        toDo = input.nextLine().split(" ");
                        unit target = battleOrder.getUnitFromName(toDo[0]);
                        if(target != null){
                            next.getUnit().dealDamage(target);
                            battleBool =true;
                            next.getUnit().setTarget(target);
                        }
                    }else{
                        System.out.println("Only one attack per turn");
                    }
                }else if(toDo[0].equalsIgnoreCase("end")){// end turn early
                    endTurn = true;
                }
            }

        }
    }
    public static void endTurn(battle feild){
        set battleOrder = feild.turnOrder();
        set.Node next = battleOrder.getTop();
        while(next.getLink() != battleOrder.getLast()){
            next = next.getLink();
            next.getUnit().changeExhausted(0.5);
        }
    }
}
