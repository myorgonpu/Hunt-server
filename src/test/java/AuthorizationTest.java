package test.java;

import main.java.ConnectionHandler;
import main.java.Role;
import main.java.messaging.*;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Slon on 16.02.2017.
 */
public class AuthorizationTest {

    private static final String HOST = "localhost";
    private static final int PORT = 1234;

    @Test
    public void successfulAuthorization() throws IOException, MessageFormatException, InterruptedException {

        new Thread(()->{
            new ConnectionHandler().start();
        }).start();

        Socket client = new Socket(HOST, PORT);
        Messenger messenger = new Messenger(client);

        Message auth = new Message(Type.REQUEST, Target.AUTHORIZATION);
        auth.addExtraField(MessageFields.LOGIN, "pupok666");
        auth.addExtraField(MessageFields.PASSWORD, "12345");
        auth.addExtraField(MessageFields.ROLE, Role.HUNTER.getName());

        System.out.println("1");
//        Thread.sleep(2000);
        messenger.send(auth);
        System.out.println("2");
        Message response = messenger.receive(0);

        System.out.println("3");
        System.out.println(response.toJSON());

        client.close();
    }

    @Test
    public void failedAuthorization() throws IOException {

        Socket client = new Socket(HOST, PORT);



        client.close();
    }
}
