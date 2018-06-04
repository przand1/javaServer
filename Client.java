import java.net.*;
import java.io.*;

public class Client {

  public static void main(String[] args) {
    try {
      InetAddress myIp = InetAddress.getByName("localhost");
      Socket clientSocket = new Socket(myIp,9999);
      DataInputStream clientInput = new DataInputStream(clientSocket.getInputStream());
      DataOutputStream clientOutput = new DataOutputStream(clientSocket.getOutputStream());
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      String login = "";

      System.out.println("From Server: "+clientInput.readUTF());

      do {
        System.out.println("Login:");
      } while((login = reader.readLine()).equals(""));

      clientOutput.writeUTF("LOGIN "+login);
      System.out.println("Waiting for START...");
      System.out.println(clientInput.readUTF());


      reader.close();
      clientInput.close();
      clientOutput.close();
      clientSocket.close();
    } catch(Exception e) {e.printStackTrace();}
  }
}
