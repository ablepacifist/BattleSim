import java.awt.*;
public class feild extends Frame{
    char[][] map;
    int maxX;
    int maxY;
    public feild(int maxX, int maxY){
        this.maxX =maxX;
        this.maxY=maxY;
        this.map = new char[maxX][maxY];
        char inputChar = '.';
        for(int i = 0; i<maxX;i++){
            for(int j = 0; j<maxY;j++){
                inputChar = '.';
                if(i==0 || i==maxX-1){
                    inputChar = '_';
                }else if(j ==0 || j==maxY-1){
                    inputChar = '|';
                }
                map[i][j] = inputChar;
            }
        }

    }
    public void printFeild(){
        for(int i = 0; i<maxX;i++){
            for(int j = 0; j<maxY;j++){
                System.out.print(map[i][j]);
            }
            System.out.print("\n");
        }
    }
}
