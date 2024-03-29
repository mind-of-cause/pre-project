package web.dao;

import web.model.User;


import java.util.List;

public interface HibernateDao {

    void createUsersTable();

    void dropUsersTable();

    void saveUser(User user);

    void removeUserById(long id);

    List<User> getAllUsers();

    void cleanUsersTable();

    User getUserById(long id);

    void updateUser(long id, User user);
}
