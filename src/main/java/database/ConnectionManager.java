package main.java.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionManager {
    private static final String URL = "jdbc:mysql://localhost:3306/user?useSSL=false";
    private static final String login = "root";
    private static final String password = "root";
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
