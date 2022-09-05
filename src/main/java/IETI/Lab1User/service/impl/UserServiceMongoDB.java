package IETI.Lab1User.service.impl;

import IETI.Lab1User.dto.UserDto;
import IETI.Lab1User.entities.User;
import IETI.Lab1User.repository.UserRepository;
import IETI.Lab1User.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceMongoDB implements UserService {

    private final UserRepository userRepository;

    public UserServiceMongoDB(@Autowired UserRepository userRepository )
    {
        this.userRepository = userRepository;
    }

    @Override
    public User create(User user )
    {
        userRepository.save(user);
        return user;
    }

    @Override
    public User findById( String id )
    {
        return userRepository.findById(id).get();
    }

    @Override
    public List<User> getAll()
    {
        return userRepository.findAll();
    }

    @Override
    public void deleteById( String id )
    {
        userRepository.deleteById(id);
    }

    @Override
    public User update(User user, String id )
    {
        userRepository.save(user);
        return userRepository.findById(id).get();
    }

    @Override
    public List<User> findUsersWithNameOrLastNameLike(String textofind) {
        return userRepository.findByNameOrLastNameLike(textofind, textofind);
    }

    @Override
    public List<User> findUsersCreatedAfter(String CreatedAt) {
        return  userRepository.findByCreatedAtGreaterThan(CreatedAt);
    }
}
