package client;

import java.net.*;
import java.io.*;
import java.util.concurrent.TimeUnit;

public class Client {

  public static void main(String[] args) {
    try {
      InetAddress myIp = InetAddress.getByName("localhost");
      Socket clientSocket = new Socket(myIp,9999);
      DataInputStream clientInput = new DataInputStream(clientSocket.getInputStream());
      DataOutputStream clientOutput = new DataOutputStream(clientSocket.getOutputStream());
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      String login = "";
      String board;

      System.out.println("From Server: "+clientInput.readUTF());

      do {
        System.out.println("Login:");
      } while((login = reader.readLine()).equals(""));

      clientOutput.writeUTF("LOGIN "+login);
      System.out.println("Waiting for START...");
      System.out.println(clientInput.readUTF());//START

      System.out.println(clientInput.readUTF());//PLAYERS
      System.out.println("Enter direction {W,A,S,D}:");
      clientOutput.writeUTF("BEGIN "+reader.readLine());//DIRECTION
      System.out.println("DEBUG:SENT Direction");

      System.out.println(clientInput.readUTF());//GAME
      Inputer inputer = new Inputer();
      inputer.start();

      while(true) {
        board=clientInput.readUTF();
        if (board.equals("LOST") || board.equals("WIN")) {
          System.out.println("YOU "+board);
          break;
        }
        System.out.print(board+'\n');
        if(inputer.getDirChanged()) {
          clientOutput.writeUTF("MOVE "+inputer.getDir());
          inputer.setDirChanged();
        }
      }

      inputer.setStop();

      reader.close();
      clientInput.close();
      clientOutput.close();
      clientSocket.close();
    } catch(Exception e) {e.printStackTrace();}
  }
}
