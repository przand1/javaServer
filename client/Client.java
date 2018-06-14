package client;

import java.net.*;
import java.io.*;
import java.util.concurrent.TimeUnit;
import java.util.Random;

public class Client {

  private static int myX;
  private static int myY;
  private static int myNumber;
  private static char myDir;
  private static char myMove;
  private static String[] myBoard = new String[50];
  private static Random rand = new Random();
  private static int randInt;

  public static char avoidCollision() {
    char toReturn = 'S';
    switch (myDir) {
      case 'w':
      case 'W':
          System.out.println("pole przede mną: "+myBoard[myY-1].charAt(myX));
        if( (myBoard[myY-1].charAt(myX) != '0') || myY == 0) {
          System.out.println("CASE W. X="+myX+" Y="+myY);
          if ((myBoard[myY].charAt(myX-1) != '0') || myX == 0) {
            toReturn = 'R';   //  ---->
            myDir = 'D';      //  \
            myX++;            //  |
          }
          else {
            toReturn = 'L';   //  <--
            myDir = 'A';      //    |
            myX--;            //    |
          }
        }

        break;
      case 'a':
      case 'A':
          System.out.println("pole przede mną: "+myBoard[myY].charAt(myX-1));
        if((myBoard[myY].charAt(myX-1) != '0') || myX == 0) {
          System.out.println("CASE A. X="+myX+" Y="+myY);
          if ((myBoard[myY+1].charAt(myX) != '0') || myY == 49) {
            toReturn = 'R';   //    ^
            myDir = 'W';      //    |
            myY--;            //    ------
          }
          else {
            toReturn = 'L';   //  -------
            myDir = 'S';      //  |
            myY++;            //  v
          }
        }

        break;
      case 's':
      case 'S':
          System.out.println("pole przede mną: "+myBoard[myY+1].charAt(myX));
        if((myBoard[myY+1].charAt(myX) != '0') || myY == 49) {
          System.out.println("CASE S. X="+myX+" Y="+myY);
          if ((myBoard[myY].charAt(myX+1) != '0') || myX == 49) {
            toReturn = 'R'; //    |
            myDir = 'A';    //    |
            myX++;          // <---
          }
          else {
            toReturn = 'L'; //  |
            myDir = 'D';    //  |
            myX--;          //  ---->
        }
        }

        break;
      case 'd':
      case 'D':
          System.out.println("pole przede mną: "+myBoard[myY].charAt(myX+1));
        if((myBoard[myY].charAt(myX+1) != '0') || myX == 49) {
          System.out.println("CASE D. X="+myX+" Y="+myY);
          if ((myBoard[myY-1].charAt(myX) != '0') || myY == 0) {
            toReturn = 'R'; //  ---
            myDir = 'S';    //    |
            myY++;          //    v
          }
          else {
            toReturn = 'L'; //    ^
            myDir = 'W';    //    |
            myY--;          //  ---
          }
        }

        break;
    }
    return toReturn;
  }

  public static void main(String[] args) {
    try {
      InetAddress myIp = InetAddress.getByName("localhost");
      Socket clientSocket = new Socket(myIp,9999);
      DataInputStream clientInput = new DataInputStream(clientSocket.getInputStream());
      DataOutputStream clientOutput = new DataOutputStream(clientSocket.getOutputStream());
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      String login = "";
      String board;
      String cordBuffer;


      System.out.println("From Server: "+clientInput.readUTF());//connect

      do {
        System.out.println("Login:");
      } while((login = reader.readLine()).equals(""));

      clientOutput.writeUTF("LOGIN "+login);//SEND LOGIN
      System.out.println(clientInput.readUTF());//OK

      System.out.println("Waiting for START...");
      cordBuffer = clientInput.readUTF();
      System.out.println(cordBuffer);//START
      // myNumber = Integer.parseInt(cordBuffer.split(" ",2)[1]);
      // System.out.println("My number: "+myNumber);

//-------------------- POCZĄTEK PĘTLI
for (int round =0;round<5 ;++round) {
    if (round>0) {
      System.out.println(clientInput.readUTF());//ROUND
    }
      cordBuffer = clientInput.readUTF();
      System.out.println(cordBuffer);//PLAYERS

// wczytywanie współrzędnych
      // myX = Integer.parseInt(cordBuffer.split(" ")[(2*myNumber)-1]);
      // myY = Integer.parseInt(cordBuffer.split(" ")[(2*myNumber)]);
      // System.out.println("my cords: "+myX+" "+myY);


      System.out.println("Enter direction {W,A,S,D}:");
      myDir = reader.readLine().charAt(0);
      clientOutput.writeUTF("BEGIN "+(myDir+""));//DIRECTION
      System.out.println(clientInput.readUTF());//OK

      System.out.println(clientInput.readUTF());//GAME

      while(true) {//--------------- JEDNA RUNDA
        board=clientInput.readUTF();
        if (board.split(" ")[0].equals("LOST") || board.equals("WIN")) {
          System.out.println(board);
          break;
        }

        randInt = rand.nextInt(10);
        if (randInt==0) {
          clientOutput.writeUTF("MOVE R");
          clientInput.readUTF();//OK
        }
        else if (randInt==1) {
          clientOutput.writeUTF("MOVE L");
          clientInput.readUTF();//OK
        }
        System.out.print(board+'\n');
      }//KONIEC RUNDY

//------------- KONIEC PĘTLI
}

      System.out.println(clientInput.readUTF());//score

      reader.close();
      clientInput.close();
      clientOutput.close();
      clientSocket.close();
    } catch(Exception e) {e.printStackTrace();}
  }
}
