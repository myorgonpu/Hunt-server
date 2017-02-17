package test.java;

import main.java.*;
import main.java.database.UserRepository;
import main.java.messaging.*;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class GameTest {

    private static final String HOST = "localhost";
    private static final int PORT = 1234;

    @Test
    public void runnerWin() throws IOException, InterruptedException {
        User client1 = new User("pupok666", "12345", 0);
        client1.setRole(Role.HUNTER);

        User client2 = new User("vasyl", "12345", 0);
        client2.setRole(Role.RUNNER);

        ActiveUsers.getUsers().add(client1);
        ActiveUsers.getUsers().add(client2);

        UserRepository repository = new RepositoryStub();
        new Thread(()->{
            new ConnectionHandler(PORT, repository).start();
        }).start();

        Socket socket1 = new Socket(HOST, PORT);
        Socket socket2 = new Socket(HOST, PORT);

        client1.setMessenger(new Messenger(socket1));
        client2.setMessenger(new Messenger(socket2));

        client1.setLocation(new Location(46.579207, 30.809716));
        client2.setLocation(new Location(46.579498, 30.809925));

        new Thread(()->{
            MainLoop game = new MainLoop(2000, repository);
            game.loop();
        }).start();

        TimeUnit.SECONDS.sleep(10);
        System.out.println("C1 " + client1.getScore());
        System.out.println("C2 " + client2.getScore());

    }
}
