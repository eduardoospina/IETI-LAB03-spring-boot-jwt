package IETI.Lab1User.service.impl;

import IETI.Lab1User.entities.User;
import IETI.Lab1User.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


//@Service
public class UserServiceHashMap implements UserService {

    private final ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

    @Override
    public User create(User user) {
        users.putIfAbsent(user.getId(), user);
        return user;
    }

    @Override
    public User findById(String id) {
        try{
            return users.get(id);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<User> getAll() {
        List<User> usuarios = new ArrayList<>();
        for (String id : users.keySet()){
            usuarios.add(users.get(id));
        }
        return usuarios;
    }

    @Override
    public void deleteById(String id) {
        users.remove(id);
    }

    @Override
    public User update(User user, String userId) {
        return users.put(userId, user);
    }

    @Override
    public List<User> findUsersWithNameOrLastNameLike(String queryText) {
        return null;
    }

    @Override
    public List<User> findUsersCreatedAfter(String startDate) {
        return null;
    }
}
