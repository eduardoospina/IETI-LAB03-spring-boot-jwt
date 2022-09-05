package IETI.Lab1User.service;
import IETI.Lab1User.entities.User;

import java.util.Date;
import java.util.List;

public interface UserService
{
    User create( User user );

    User findById( String id );

    List<User> getAll();

    void deleteById( String id );

    User update(User user, String userId );

    List<User> findUsersWithNameOrLastNameLike(String textofind);

    List<User> findUsersCreatedAfter(String CreatedAt);
}
