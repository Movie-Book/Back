package hello.moviebook.movie.repository;

import hello.moviebook.user.domain.User;
import hello.moviebook.movie.domain.UserGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGenreRepository extends JpaRepository<UserGenre, Long> {
    UserGenre findUserGenreByUser(User user);
}
