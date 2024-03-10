package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    private static final String URL = "jdbc:mysql://localhost:3306/user";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private static Connection connection = null;
    private static final Util newUtil = new Util();


    static {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            if (connection != null && !connection.isClosed()) {
                System.out.println("We are connected!");
            }
        } catch (SQLException e) {
            System.out.println("there is no connection... Exception!");
        }
    }

    // Метод для получения соединения с базой данных
    public static Connection getConnection() {
        return Util.connection;
    }

    public static void shutdown() throws SQLException {
        connection.close();
    }
}
