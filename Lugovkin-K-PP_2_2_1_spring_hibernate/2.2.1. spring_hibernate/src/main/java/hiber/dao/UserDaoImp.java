package hiber.dao;

import hiber.model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void add(User user) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.save(user);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            System.out.println("Ошибка при добавлении пользователя");
        }
    }

    @Override

    public List<User> listUsers() {
        try {
            Query<User> query = sessionFactory.getCurrentSession().createQuery("from User", User.class);
            return query.getResultList();
        } catch (HibernateException e) {
            e.printStackTrace();
            System.out.println("Ошибка при получении списка пользователей");
            return Collections.emptyList();
        }
    }

    @Override
    public User getUserByCarModelAndSeries(String model, int series) {
        try {
            Session session = sessionFactory.getCurrentSession();
            Query<User> query = session.createQuery("FROM User WHERE car.model = :model AND car.series = :series", User.class);
            query.setParameter("model", model);
            query.setParameter("series", series);
            List<User> users = query.getResultList();
            return users.isEmpty() ? null : users.get(0);
        } catch (HibernateException e) {
            e.printStackTrace();
            System.out.println("Ошибка при получении пользователя по модели и серии машины ");
            return null;
        }
    }
}
