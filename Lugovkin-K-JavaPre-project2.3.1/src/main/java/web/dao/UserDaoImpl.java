package web.dao;

import org.springframework.stereotype.Repository;
import web.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import java.util.List;


@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    public void saveUser(User user) {
        try {
            entityManager.persist(user);
        } catch (PersistenceException e) {
            entityManager.getTransaction().setRollbackOnly();
            e.printStackTrace();
            System.out.println("Проблемы с сохранением пользователя в БД");
        }
    }

    public void removeUserById(long id) {
        try {
            User user = entityManager.find(User.class, id);
            if (user != null) {
                entityManager.remove(user);
            }
        } catch (PersistenceException e) {
            entityManager.getTransaction().setRollbackOnly();
            e.printStackTrace();
            System.out.println("Проблемы с удалением пользователя из БД");
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = null;
        try {
            TypedQuery<User> query = entityManager.createQuery("SELECT u FROM usertable u", User.class);
            users = query.getResultList();
        } catch (PersistenceException e) {
            e.printStackTrace();
            System.out.println("Проблемы с получением всех пользователей из БД ");
        }
        return users;
    }

    public User getUserById(long id) {
        return entityManager.find(User.class, id);
    }

    public void updateUser(long id, User user) {
        try {
            User userToUpdate = entityManager.find(User.class, id);
            if (userToUpdate != null) {
                userToUpdate.setName(user.getName());
                userToUpdate.setWeight(user.getWeight());
                userToUpdate.setHasCar(user.isHasCar());

                entityManager.merge(userToUpdate);

            }
        } catch (PersistenceException e) {
            e.printStackTrace();
            System.out.println("Проблемы с обновлением пользователя в БД ");
        }
    }
}