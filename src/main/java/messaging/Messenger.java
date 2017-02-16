package main.java.messaging;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;


public class Messenger {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public Messenger(Socket socket) throws IOException{
        Objects.requireNonNull(socket);
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    public void send(Message message) throws IOException{
        Objects.requireNonNull(message);
        out.write(message.toJSON());
    }

    public Message receive(int timeout) throws IOException {
        socket.setSoTimeout(timeout);
        return new Message(in.readLine());
    }
}

