package main.java;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionHandler {

    private static final int PORT = 1234;

    public void start(){

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            while (true){
                final Socket client = serverSocket.accept();
                new Thread(() -> {
                        AuthHandler authHandler = new AuthHandler(client);
                        authHandler.processUserInfo();
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


