package main.java;

/**
 * Created by Marina on 12.02.2017.
 */
public class User {
    private Integer id_user;
    private String nickname;
    private String login;
    private String password;
    private int account;

    public User(){}

    public Integer getId() {
        return id_user;
    }

    public void setId(Integer id_user) {
        this.id_user = id_user;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public String getLogin() {
        return login;
    }

    public int getAccount() {
        return account;
    }

   /* @Override
    public String toString(){
        return "User{"+
                ",id_user='"+id_user+'\'' +
                ",nickname='"+nickname+'\'' +
                ",login='"+login+'\'' +
                ",password='"+password+'\'' +
                ",account='"+account+
                '?';
    }*/
}
