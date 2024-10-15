package hello.moviebook.UserGenre;

import hello.moviebook.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGenreRepository extends JpaRepository<UserGenre, Long> {
    UserGenre findUserGenreByUser(User user);
}
