package multiThreaded;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    private Runnable getRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                int port = 8010;
                try {
                    InetAddress address = InetAddress.getByName("localhost");
                    Socket socket = new Socket(address, port);
                    PrintWriter toServer = new PrintWriter(socket.getOutputStream());
                    BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    toServer.println("Hello from the client");
                    String line = fromServer.readLine();
                    System.out.println("Response from the server is : " + line);
                    toServer.close();
                    fromServer.close();
                    socket.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        };
    }

    public static void main(String[] args) {
        Client client = new Client();
        for(int i=0; i<100; ++i) {
            try {
                Thread thread = new Thread(client.getRunnable());
                thread.start();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }


}
