package main.java;


import java.util.concurrent.CopyOnWriteArrayList;

public class ActiveUsers {
    private static final CopyOnWriteArrayList<User> users = new CopyOnWriteArrayList<User>();

    public static CopyOnWriteArrayList<User> getUsers() {
        return users;
    }
}
