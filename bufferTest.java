import java.io.*;

public class bufferTest {

   public static void main(String [] args) {
     char[][] game;
     game=new char[50][50];
     for (int x=0; x<50; x++) {
        for (int y=0; y<50; y++) {
          if(x==0||x==49||y==0||y==49) game[x][y]='+';
          else game[x][y] = '-';
        }
      }
      game[10][10]='*';
      game[20][20]='*';
      game[40][40]='*';
      String gameBuff= new String();
        for (int x=0; x<50; x++) {
           for (int y=0; y<50; y++) {
             gameBuff+=game[x][y];
           }
           gameBuff+='\n';
      }
      System.out.println(gameBuff);
   }
}
