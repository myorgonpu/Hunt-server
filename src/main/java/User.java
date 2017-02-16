package main.java;


import main.java.messaging.Messenger;

public class User {
    private int id;
    private String login;
    private String password;
    private int score;
    private Role role;
    private Messenger messenger;
    private Location location;
    private int timesAFK = 0;

    public User(int id, String login, String password, int score, Messenger messenger) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.score = score;
        this.messenger = messenger;
    }

    public User(String login, String password, int score) {
        this.login = login;
        this.password = password;
        this.score = score;
    }

    public User(){

    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getTimesAFK() {
        return timesAFK;
    }

    public void setAFK(boolean isAFK) {
        if(isAFK){
            timesAFK++;
        }else{
            timesAFK = 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        return login.equals(user.login);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + login.hashCode();
        return result;
    }
}
