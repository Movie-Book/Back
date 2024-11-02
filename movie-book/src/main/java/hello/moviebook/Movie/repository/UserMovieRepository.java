package hello.moviebook.movie.repository;

import hello.moviebook.movie.domain.Movie;
import hello.moviebook.movie.domain.UserMovie;
import hello.moviebook.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMovieRepository extends JpaRepository<UserMovie, Long> {
    UserMovie findByUserAndMovie(User user, Movie movie);
}
