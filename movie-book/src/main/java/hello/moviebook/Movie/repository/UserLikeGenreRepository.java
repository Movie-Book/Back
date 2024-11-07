package hello.moviebook.movie.repository;

import hello.moviebook.movie.domain.UserLikeGenre;
import hello.moviebook.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLikeGenreRepository extends JpaRepository<UserLikeGenre, Long> {
    List<UserLikeGenre> findAllByUser(User user);
    void deleteAllByUser(User user);
}
