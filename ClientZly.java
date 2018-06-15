import java.net.*;
import java.io.*;
import java.util.concurrent.TimeUnit;
import java.util.Random;

public class ClientZly {

  public static void main(String[] args) {
    try {
      InetAddress myIp = InetAddress.getByName("localhost");
      Socket clientSocket = new Socket(myIp,9999);
      DataInputStream clientInput = new DataInputStream(clientSocket.getInputStream());
      DataOutputStream clientOutput = new DataOutputStream(clientSocket.getOutputStream());
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      String login = "";
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

      for (int i=0;i<200 ;i++ ) {
        clientOutput.writeUTF("sljtngbsot nosjtkhngsltjkn nsltrjk n");
      }

      reader.close();
      clientInput.close();
      clientOutput.close();
      clientSocket.close();
    } catch(Exception e) {e.printStackTrace();}
  }
}
