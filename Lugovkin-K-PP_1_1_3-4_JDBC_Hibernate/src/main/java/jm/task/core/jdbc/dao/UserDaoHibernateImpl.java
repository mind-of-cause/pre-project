package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;


import java.util.ArrayList;
import java.util.List;




public class UserDaoHibernateImpl implements UserDao {


    private final SessionFactory sessionFactory;

    public UserDaoHibernateImpl() {
        this.sessionFactory = Util.getSessionFactory();
    }


    @Override
    public void createUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query query = session.createSQLQuery("CREATE TABLE IF NOT EXISTS usertable("
                    + "id BIGINT PRIMARY KEY AUTO_INCREMENT, "
                    + "name VARCHAR(50), "
                    + "lastName VARCHAR(50), "
                    + "age TINYINT);");
            query.executeUpdate();
        }  catch (HibernateException e){
        e.printStackTrace();
    }
    }

    @Override
    public void dropUsersTable() {
        Transaction ts;
        try (Session session = sessionFactory.openSession()) {
            ts = session.beginTransaction();
            String sql = "DROP TABLE IF EXISTS user.usertable";
            Query query = session.createNativeQuery(sql);
            query.executeUpdate();
            ts.commit();
        } catch (HibernateException e){
        e.printStackTrace();
    }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction ts = null;

        try (Session session = sessionFactory.openSession()) {
           ts = session.beginTransaction();
            User newUser = new User();
            newUser.setName(name);
            newUser.setLastName(lastName);
            newUser.setAge(age);
            System.out.println("Пользователь с именем " + name + " добавлен в базу данных");

            session.save(newUser);
            ts.commit();
        } catch (HibernateException e){
            if (ts!= null) {ts.rollback();}
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction ts = null;
        try (Session session = sessionFactory.openSession()) {
                ts = session.beginTransaction();
                User user = session.get(User.class, id);
                if (user != null) {
                    session.delete(user);
                }
                ts.commit();
            } catch (HibernateException e) {
                if (ts != null) {ts.rollback();}
            e.printStackTrace();
            }
        }


    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Transaction ts = null;
        try (Session session = sessionFactory.openSession()) {
           ts = session.beginTransaction();
            users = session.createQuery(" FROM User", User.class).list();

            ts.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public void cleanUsersTable() {
        Transaction ts = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String hql = "DELETE FROM User";
            Query query = session.createQuery(hql);
            query.executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException e){
            if (ts!= null) {ts.rollback();}
            e.printStackTrace();
        }
    }
}
