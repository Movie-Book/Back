package hello.moviebook.user.repository;

import hello.moviebook.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsById(String id);
    Boolean existsByEmail(String email);
    User findUserById(String id);
    User findUserByEmail(String email);
    User findUserByUserName(String userName);
}
