package hello.moviebook.movie.repository;

import hello.moviebook.user.domain.User;
import hello.moviebook.movie.domain.UserDislikeGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDislikeGenreRepository extends JpaRepository<UserDislikeGenre, Long> {
    List<UserDislikeGenre> findAllByUser(User user);
    void deleteAllByUser(User user);
}
