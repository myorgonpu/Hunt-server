package main.java;


import main.java.database.UserRepository;
import main.java.database.UserRepositoryRDB;


public class EntryPoint {
    private static final int PORT = 1234;

    public static void main(String[] args) {

        UserRepository repository = UserRepositoryRDB.getInstance();

        new Thread(()->{
            new ConnectionHandler(PORT, repository).start();
        }).start();

        MainLoop game = new MainLoop(repository);
        game.loop();
    }
// TODO: тестить авторизацию+
// /регистрацию и БД
//       сделать роли+
//       сделать енкаунтер+
//       тестить с клиентом+
}
