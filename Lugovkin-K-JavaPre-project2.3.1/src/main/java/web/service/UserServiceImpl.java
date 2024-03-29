package web.service;

import web.dao.HibernateDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.model.User;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final HibernateDao hibernateDao;

    @Autowired
    public UserServiceImpl(HibernateDao hibernateDao) {
        this.hibernateDao = hibernateDao;
    }

    @Transactional
    @Override
    public void createUsersTable() {
        hibernateDao.createUsersTable();
    }

    @Transactional
    @Override
    public void dropUsersTable() {
        hibernateDao.dropUsersTable();
    }

    @Transactional
    @Override
    public void saveUser(User user) {
        hibernateDao.saveUser(user);
    }

    @Transactional
    @Override
    public void removeUserById(long id) {
        hibernateDao.removeUserById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getAllUsers() {
        return hibernateDao.getAllUsers();
    }

    @Transactional
    @Override
    public void cleanUsersTable() {
        hibernateDao.cleanUsersTable();
    }

    @Transactional(readOnly = true)
    public User getUserById(long id) {
        return hibernateDao.getUserById(id);
    }

    @Transactional
    public void updateUser(long id, User user) {
        hibernateDao.updateUser(id, user);
    }
}