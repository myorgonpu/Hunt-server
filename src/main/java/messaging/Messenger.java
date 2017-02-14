package main.java.messaging;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class Messenger {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public Messenger(Socket socket) throws IOException{
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    public void send(String json) throws IOException{
        out.write(json);
    }

    public String recieve(int timeout) throws IOException {
        socket.setSoTimeout(timeout);
        return in.readLine();
    }
}

