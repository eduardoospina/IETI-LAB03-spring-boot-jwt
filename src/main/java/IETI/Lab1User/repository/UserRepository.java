package IETI.Lab1User.repository;

import IETI.Lab1User.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String>
{
    List<User> findByNameOrLastNameLike(String name, String Lastname);

    List<User> findByCreatedAtGreaterThan(String createdAt);

    Optional<User> findByEmail(String email);
}
