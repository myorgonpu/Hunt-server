package main.java;

/**
 * Created by Marina on 12.02.2017.
 */
public class User {
    private int id;
    private String login;
    private String password;
    private int score;

    public User(String login, String password, int score) {
        this.login = login;
        this.password = password;
        this.score = score;
    }

    public User(int id,String login, String password, int score) {
        this.id=id;
        this.login = login;
        this.password = password;
        this.score = score;
    }
    public User(){};

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public int getScore() {
        return score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString(){
        return getId()+" "+getLogin()+" " +getPassword()+" "+getScore();
    }

}
