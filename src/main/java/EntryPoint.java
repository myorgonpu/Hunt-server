package main.java;



public class EntryPoint {
    public static void main(String[] args) {
        MainLoop game = new MainLoop();
        game.loop();
    }
    // TODO: тестить авторизацию/регистрацию и БД
    //       сделать роли
    //       сделать енкаунтер
    //       тестить с клиентом
}
