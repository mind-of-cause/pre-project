package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static jm.task.core.jdbc.util.Util.getConnection;

public class UserDaoJDBCImpl implements UserDao {

    private static final String INSERT_NEW = "INSERT INTO usertable (name, lastName, age) VALUES(?,?,?)";
    private static final String GET_ALL = "SELECT * FROM usertable";
    private static final String GET_BY_ID = "SELECT id, name,lastName, age WHERE id=?";

    private static final String DELETE = "DELETE FROM usertable WHERE id = ?";
    private static final String CLEAN = "TRUNCATE TABLE usertable";
    private static final String DROP = "DROP TABLE IF EXISTS usertable";
    private static final String SAVE = "UPDATE usertable SET name, lastName, age";


    public UserDaoJDBCImpl() {

    }

    @Override
    public void createUsersTable() {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS usertable ("
                + "id BIGINT PRIMARY KEY AUTO_INCREMENT, "
                + "name VARCHAR(50), "
                + "lastName VARCHAR(50), "
                + "age TINYINT);")) {
            preparedStatement.execute();

        } catch (SQLException e) {
            System.out.println("Error in createUsersTable!");
        }
    }

    @Override
    public void dropUsersTable() {
        try (Statement statement = getConnection().createStatement()) {
            statement.execute(DROP);
        } catch (SQLException e) {
            System.out.println("Error in dropUsersTable!");
        }

    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try {
            getConnection().setAutoCommit(false);
            try (PreparedStatement preparedStatement = getConnection().prepareStatement(INSERT_NEW)) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, lastName);
                preparedStatement.setByte(3, age);

                preparedStatement.execute();
                System.out.println("User с именем " + name + " добавлен в базу данных");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error in saveUser!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                getConnection().setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        try {
            getConnection().setAutoCommit(false);
            try (PreparedStatement preparedStatement = getConnection().prepareStatement(DELETE)) {
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();
                getConnection().commit();

            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error in removeUserById!");
                getConnection().rollback();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                getConnection().setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>(); // Создание списка для хранения объектов User
        ResultSet res;
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(GET_ALL)) {
            res = preparedStatement.executeQuery();


            while (res.next()) {
                long id = res.getLong("id");
                String name = res.getString("name");
                String lastName = res.getString("lastName");
                byte age = res.getByte("age");

                User user = new User(name, lastName, age); // Создание нового пользователя
                user.setId(id); // установка id
                users.add(user); // Добавление пользователя в список
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error in getAllUsers!");
        }

        return users; // Возвращение списка пользователей
    }

    @Override
    public void cleanUsersTable() {
        try (Statement statement = getConnection().createStatement()) {
            statement.execute(CLEAN);
        } catch (SQLException e) {
            System.out.println("Error in cleanUsersTable!");
        }

    }

}

