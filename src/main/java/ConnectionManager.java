package main.java;

import java.sql.*;

public class ConnectionManager {
    private static final String URL="jdbc:mysql://localhost:3306/user?useSSL=false";
    private static final String login="good";
    private static final String password="if_I_know1";
    static Connection connection=null;

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
