package test.java;

import main.java.AuthHandler;
import main.java.ConnectionHandler;
import main.java.Role;
import main.java.messaging.*;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Slon on 16.02.2017.
 */
public class AuthorizationTest {

    private static final String HOST = "localhost";
    private static final int PORT = 1234;

    @Test
    public void successfulAuthorization() throws IOException, MessageFormatException, InterruptedException, JSONException {

        new Thread(()->{
            new ConnectionHandler(PORT, new RepositoryStub()).start();
        }).start();

        Socket client = new Socket(HOST, PORT);
        Messenger messenger = new Messenger(client);

        Message auth = new Message(Type.REQUEST, Target.AUTHORIZATION);
        auth.addExtraField(MessageFields.LOGIN, "pupok666");
        auth.addExtraField(MessageFields.PASSWORD, "12345");
        auth.addExtraField(MessageFields.ROLE, Role.HUNTER.getName());

        messenger.send(auth);
        Message response = messenger.receive(AuthHandler.TIMEOUT_MILLIS);

        JSONAssert.assertEquals("{\"type\":\"response\",\"target\":\"authorization\"}", response.toJSON(), false);

        client.close();
    }

    @Test
    public void failedAuthorization() throws IOException {

        Socket client = new Socket(HOST, PORT);



        client.close();
    }
}
