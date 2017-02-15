package main.java;

import java.sql.*;

/**
 * Created by Marina on 14.02.2017.
 */
public class UserRepositoryImplements implements UserRepository{

    private Connection connection;

    @Override
    public User create(User user) {

        try {
            final String query =
                    "INSERT INTO user(nickname,login,password,account) " +
                            "VALUES (?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,user.getNickname());
            preparedStatement.setString(2,user.getLogin());
            preparedStatement.setString(3,user.getPassword());
            preparedStatement.setString(4,"0");

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
