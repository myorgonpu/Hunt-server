package main.java.database;

import main.java.User;

import java.sql.*;


public class UserRepositoryRDB implements UserRepository{

    private static UserRepositoryRDB instance = new UserRepositoryRDB();

    private Connection connection;


    private UserRepositoryRDB(){
        connection = new ConnectionManager().getConnection();
    }

    public static UserRepositoryRDB getInstance(){
        return instance;
    }

    @Override
    public User create(User user) {

        return null;
    }

    @Override
    public User get(String login, String password) {
        return null;
    }


    @Override
    public User delete(Integer id) {
        return null;
    }

}
