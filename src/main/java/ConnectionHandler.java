package main.java;


import main.java.database.UserRepository;
import main.java.messaging.MessageFormatException;
import test.java.RepositoryStub;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionHandler {

    private int port;
    private UserRepository repository;

    public ConnectionHandler(int port, UserRepository repository) {
        this.repository = repository;
        this.port = port;
    }

    public void start(){

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true){
                final Socket client = serverSocket.accept();
                new Thread(() -> {
                    // TODO: solve da stub
                        AuthHandler authHandler = new AuthHandler(client, repository);
                    try {
                        authHandler.processUserInfo();
                    } catch (MessageFormatException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


