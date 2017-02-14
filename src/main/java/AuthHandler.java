package main.java;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class AuthHandler {

    private static final int TIMEOUT_MILLIS = 200;

    private Socket client;

    public AuthHandler(Socket client) {
        this.client = client;
    }


    public void processUserInfo() {
        //TODO: think about messaging
//        try {
//            InputStream inputStream = client.getInputStream();
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//
//            client.setSoTimeout(TIMEOUT_MILLIS);
//
//            String request = bufferedReader
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}

