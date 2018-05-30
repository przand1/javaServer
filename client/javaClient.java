// File Name javaClient1.java
package client;

import java.net.*;
import java.io.*;
import java.util.concurrent.ExecutionException;

public class javaClient {

   public static void main(String [] args) {
      String serverName = args[0];
      int port = Integer.parseInt(args[1]);
      String buff;
      try {
         System.out.println("Connecting to " + serverName + " on port " + port);
         Socket client = new Socket(serverName, port);

         System.out.println("Just connected to " + client.getRemoteSocketAddress());
         OutputStream outToServer = client.getOutputStream();
         DataOutputStream out = new DataOutputStream(outToServer);

         out.writeUTF("Hello from " + client.getLocalSocketAddress());
         InputStream inFromServer = client.getInputStream();
         DataInputStream in = new DataInputStream(inFromServer);

         System.out.println("Server says: " + in.readUTF());
//-----------------------------------------------------------------------

        //BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        TimedScanner scanner = new TimedScanner(System.in);
        int count=0;
        try {
          while(count++<5) {
            System.out.println("Ener CORDS:X:Y");
            buff = null;
            if ((buff=scanner.nextLine(10000))==null) {
              out.writeUTF("NONE");
            }
            else {
              out.writeUTF(buff);
            }
            //buff=reader.readLine();
            //System.out.println("Sending coordinates");
            //out.writeUTF(buff);
            System.out.println("From server:\n"+in.readUTF());
          }
        }
        catch (InterruptedException e) {
      		e.printStackTrace();
      	}
      	catch (ExecutionException e) {
      		e.printStackTrace();
      	}
//-----------------------------------------------------------------------
         client.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
