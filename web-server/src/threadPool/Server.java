package threadPool;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final ExecutorService threadPool;

    public Server(int poolSize) {
        this.threadPool = Executors.newFixedThreadPool(poolSize);
    }

    public void handleClient(Socket acceptedConnection) {
            try (PrintWriter toClient =  new PrintWriter(acceptedConnection.getOutputStream(), true)) {
                toClient.println("Hello from the server"+acceptedConnection.getInetAddress());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
    }

    public static void main(String[] args) {
        int port = 8010;
        int poolSize = 10;
        Server server = new Server(poolSize);

        try{
            ServerSocket socket = new ServerSocket(port);
            socket.setSoTimeout(70000);
            System.out.println("Server is listening on port " + port + " ...");
            while(true) {
                Socket acceptedConnection = socket.accept();
                server.threadPool.execute(()-> server.handleClient(acceptedConnection));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            server.threadPool.shutdown();
        }
    }
}
