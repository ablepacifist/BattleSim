import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

class fileIO{
    static ArrayList<String> lines = new ArrayList<String>();
    static String line = null;
    public static void  save(String filename, set army){
        System.out.println("saving game...");//C:\\Users\\Owner\\Documents\\DnD\\warGame
        String destination = filename;
            try
            {
                File f1 = new File(destination);
                FileReader fr = new FileReader(f1);
                set.Node next = army.getTop();
                lines.add(army.getName()+"\n");
                while(next.getLink() != army.getLast()){
                    next=next.getLink();
                    line = next.getUnit().getType()+","+next.getUnit().getCurrentHealth()+","+next.getUnit().getExhausted()+","+next.getUnit().getName()+"\n";
                    lines.add(line);
                }
                FileWriter fw = new FileWriter(f1);
                for(int i=0;i<lines.size();i++){
                    fw.append(lines.get(i));
                    System.out.println(lines.get(i));
                }
                fw.flush();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
            System.out.print("done");
        }
        /*
Ididots play games
swordsmen,4000,1.0,hunter1
archer,5000,1.0,devon1
spearmen,4000,1.0,hunter2
         */
}