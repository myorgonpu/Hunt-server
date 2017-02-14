package main.java;


import main.java.messaging.Messenger;

public class User {
    private int id;
    private String login;
    private String password;
    private int score;
    private Messenger messenger;

    public User(int id, String login, String password, int score, Messenger messenger) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.score = score;
        this.messenger = messenger;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Messenger getMessenger() {
        return messenger;
    }

    public void setMessenger(Messenger messenger) {
        this.messenger = messenger;
    }
}
