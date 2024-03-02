package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;


public class UserDaoHibernateImpl implements UserDao {


    private final SessionFactory sessionFactory;

    public UserDaoHibernateImpl() {
        this.sessionFactory = Util.UtilHibernate.getSessionFactory();
    }


    @Override
    public void createUsersTable() {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Query query = session.createSQLQuery("CREATE TABLE IF NOT EXISTS usertable("
                    + "id BIGINT PRIMARY KEY AUTO_INCREMENT, "
                    + "name VARCHAR(50), "
                    + "lastName VARCHAR(50), "
                    + "age TINYINT);");
            query.executeUpdate();
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String sql = "DROP TABLE IF EXISTS user.usertable";
            Query query = session.createNativeQuery(sql);
            query.executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            User newUser = new User();
            newUser.setName(name);
            newUser.setLastName(lastName);
            newUser.setAge(age);

            session.save(newUser);
            session.getTransaction().commit();
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction ts = null;
            try {
                ts = session.beginTransaction();
                User user = session.get(User.class, id);
                if (user != null) {
                    session.delete(user);
                }
                ts.commit();
            } catch (RuntimeException e) {
                if (ts != null) ts.rollback();
                throw e;
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            users = session.createQuery(" FROM User", User.class).list();

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String hql = "DELETE FROM User";
            Query query = session.createQuery(hql);
            query.executeUpdate();
            session.getTransaction().commit();
        }
    }
}
