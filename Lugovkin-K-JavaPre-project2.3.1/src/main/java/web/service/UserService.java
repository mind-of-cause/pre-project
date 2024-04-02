package web.service;

import web.model.User;

import java.util.List;

public interface UserService {


    void saveUser(User user);

    void removeUserById(long id);

    List<User> getAllUsers();


    public User getUserById(long id);

    public void updateUser(long id, User user);
}