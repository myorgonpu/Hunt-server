package main.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Marina on 12.02.2017.
 */
public class ConnectionManager {
    private static final String URL="jdbc:mysql://localhost:3306/user?useSSL=false";
    private static final String login="login";
    private static final String password="password";
    private static Connection connection;

    public Connection getConnection(){
        if (connection==null){
            try {
                connection=createConnection();
            }catch (SQLException ex){
                ex.getMessage();
            }
        }
        return connection;
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(URL, login, password);
    }
}
