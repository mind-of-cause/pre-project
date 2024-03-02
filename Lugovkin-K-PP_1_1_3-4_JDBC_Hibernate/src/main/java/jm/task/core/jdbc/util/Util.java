package jm.task.core.jdbc.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    private static final String URL = "jdbc:mysql://localhost:3306/user";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private Connection connection;


    public Util() {
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
    public Connection getConnection() {
        return connection;
    }

    public static class UtilHibernate {
        private static final SessionFactory sessionFactory = buildSessionFactory();

        public static SessionFactory buildSessionFactory() {
            try {
                StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder().configure("/hibernate.cfg.xml").build();
                Metadata metadata = new MetadataSources(standardRegistry).getMetadataBuilder().build();
                return metadata.getSessionFactoryBuilder().build();
            } catch (Throwable ex) {
                System.err.println("Initial sessionFactory creation failed. " + ex);
                throw new ExceptionInInitializerError(ex);


            }

        }

        public static SessionFactory getSessionFactory() {
            return sessionFactory;
        }

        public static void shutdown() {
            if (sessionFactory != null && !sessionFactory.isClosed()) {
                sessionFactory.close();
            }
        }
    }
}

