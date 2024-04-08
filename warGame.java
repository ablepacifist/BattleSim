import java.io.IOException;
import java.util.Scanner;
public class warGame {
    public static void main(String[] args) {
        battle hills= new battle();
        boolean end = false;
        int turnCounter = 0;
        boolean init = false;
        // prepare the input
        Scanner input = new Scanner(System.in);
        System.out.println("input 2 file names(separated by a comma ,):");
        String filenameString[] = input.nextLine().split(",");

            try {
                hills= new battle(filenameString[0],filenameString[1]);
            } catch (IOException e) {
            }
        System.out.println("welcome to war! \n please enter comand(h for help)");
        while(!end){// go until flag is true
           String comand[] = input.nextLine().split(" ");
           if(comand[0].equalsIgnoreCase("h")){
            warGame.help();
           }else if(comand[0].equalsIgnoreCase("battle")){
            warGame.takeTurn(hills,input);
            endTurn(hills);
            turnCounter++;
            System.out.println("end of turn:"+turnCounter);
           }else if(comand[0].equalsIgnoreCase("end")){
            end = true;
           }else if(comand[0].equalsIgnoreCase("init")){
           }
        }
    }
    public static void help(){
        System.out.println();
        System.out.println("RULES:\n max unit can be 10k, all units must be given a name,\n and number that is unique to that unit");
        System.out.println("list of comands:\n h - for help \n end - for end game(also should save into a txt file?)\n battle - to start fight in appropriate turn order\n");
    }
    // go through turn order and ask for input on what that unit did:
    // print stats: hp, type, range, speed, etc
    // ask did move(beguining and end of turn)?
    // attack what target
        public static void takeTurn(battle feild, Scanner input){
        set battleOrder = feild.turnOrder();
        set.Node next = battleOrder.getTop();
        while(next.getLink() != battleOrder.getLast()){
            next = next.getLink(); // the next item in th order
            next.print();
            //ask what to do?
            boolean endTurn= false;
            boolean battleBool =false;
            int maxMove = next.getMovementSpeed();
            while(!endTurn){
                String toDo[] = input.nextLine().split(" ");
                if(toDo[0].equalsIgnoreCase("s")){//skip turn
                    if(!battleBool && next.getUnit().getTarget() != null){
                        next.getUnit().dealDamage(next.getUnit().getTarget());
                        battleBool=true;
                    }
                    endTurn=true;
                }else if(toDo[0].equalsIgnoreCase("h")){//help
                    System.out.println("s- skip/end turn,\nmove-move current unit,\nt- change target(if appicable)\nend - only use if you dont want to attack\n");
                }else if(toDo[0].equalsIgnoreCase("move")){// beguin with a move
                    if(toDo.length < 2){//need to get how much moved by
                        System.out.println("by how much(in feet)\n units movement speed:"+maxMove);
                        toDo = input.nextLine().split(" ");
                        if(maxMove>=Integer.parseInt(toDo[0])){
                            double a =(Integer.parseInt(toDo[0]));
                            double b =maxMove;
                            next.getUnit().setExhausted(-(a/b));// if full move units are exhausted 
                            maxMove = maxMove - Integer.parseInt(toDo[0]); //?? check to make sure its getting the right element in toDo
                            next.getUnit().setTarget(null);
                            System.out.println("Note: must retarget after every move\nunit movement speed:"+maxMove);
                        }else{
                            System.out.println("cant move that far");
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
           // next.unit.dealDamage(next.target);
        }
    }
    public static void endTurn(battle feild){
        set battleOrder = feild.turnOrder();
        set.Node next = battleOrder.getTop();
        while(next.getLink() != battleOrder.getLast()){
            next = next.getLink();
            next.getUnit().changeExhausted(0.5);;
        }
    }
}
