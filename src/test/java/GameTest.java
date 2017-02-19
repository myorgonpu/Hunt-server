package test.java;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.*;
import main.java.database.UserRepository;
import main.java.messaging.*;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class GameTest {

    private static final String HOST = "localhost";
    private static final int PORT = 1234;

    @Test
    public void runnerWin() throws IOException, InterruptedException {

        UserRepository repository = new RepositoryStub();
        new Thread(()->{
            new ConnectionHandler(PORT, repository).start();
        }).start();

        new Thread(()->{
            try {
                Client pupok = new Client("pupok666", "12345",
                        Role.HUNTER, new Location(46.579207, 30.809716));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(()->{
            try {
                Client vasil = new Client("vasil", "qwerty",
                        Role.RUNNER, new Location(46.579498, 30.809925));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();


        MainLoop game = new MainLoop(repository);
        game.loop();
        
        // фиксить таймауты+
        // убрать слипы+
        // чекать енкаунтер+
        // убрать принты+

    }
}

