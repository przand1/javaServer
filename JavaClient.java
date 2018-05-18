import java.net.Socket;
import java.net.InetAddress;
import java.io.*;

public class JavaClient {
  private static Socket clientSocket;
  private static String serverAddress = "150.254.79.244";
  private static int serverPort = 9000;
  public static void main(String[] args) {
    try {
      System.out.println("Connecting: "+serverAddress+" "+serverPort);
      clientSocket = new Socket(serverAddress,serverPort);

      clientSocket.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
