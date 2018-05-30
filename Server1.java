// File Name Server1.java
import java.net.*;
import java.io.*;

public class Server1 extends Thread {
   private ServerSocket serverSocket;
   private String cords;
   private String buff;
   private int X;
   private int Y;
   private char[][] game;

   public Server1(int port) throws IOException {
      serverSocket = new ServerSocket(port);
      serverSocket.setSoTimeout(10000);

      game = new char[50][50];
      for (int x=0; x<50; x++) {
        for (int y=0; y<50; y++) {
          if(x==0||x==49||y==0||y==49) game[x][y]='+';
          else game[x][y] = '-';
        }
      }
   }

   public String getGame() {
     String gameBuff= new String();
     for (int x=0; x<50; x++) {
       for (int y=0; y<50; y++) {
         gameBuff+=game[x][y];
       }
       gameBuff+='\n';
     }
     return gameBuff;
   }

   public void run() {
      while(true) {
         try {
            System.out.println("Waiting for client on port " +
               serverSocket.getLocalPort() + "...");
            Socket server = serverSocket.accept();

            System.out.println("Just connected to " + server.getRemoteSocketAddress());
            DataInputStream in = new DataInputStream(server.getInputStream());

            System.out.println(in.readUTF());
            DataOutputStream out = new DataOutputStream(server.getOutputStream());
            out.writeUTF("Thank you for connecting to " + server.getLocalSocketAddress());
//--------------------------------------------------------------------------------------
            int count=0;
            while(count++<5) {
              cords = in.readUTF();
              System.out.println("Recieved: "+cords);
              if(cords.equals("NONE")) {
                out.writeUTF("SERVER: no input!");
              }
              else {
                buff = cords.substring(cords.indexOf(':') + 1, cords.lastIndexOf(':'));
                X = Integer.parseInt(buff);
                buff = cords.substring(cords.lastIndexOf(':') + 1);
                Y = Integer.parseInt(buff);
                game[X][Y] = 'a';
                out.writeUTF(getGame());
              }

              //game[X][Y] = 'a';
              //out.writeUTF(getGame());
            }

//--------------------------------------------------------------------------------------
            server.close();

         } catch (SocketTimeoutException s) {
            System.out.println("Socket timed out!");
            break;
         } catch (IOException e) {
            e.printStackTrace();
            break;
         }
      }
   }

   public static void main(String [] args) {
      int port = Integer.parseInt(args[0]);
      try {
         Thread t = new Server1(port);
         t.start();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
