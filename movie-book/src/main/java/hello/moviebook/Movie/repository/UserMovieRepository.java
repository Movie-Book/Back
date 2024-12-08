package hello.moviebook.movie.repository;

import hello.moviebook.movie.domain.Movie;
import hello.moviebook.movie.domain.UserMovie;
import hello.moviebook.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMovieRepository extends JpaRepository<UserMovie, Long> {
    UserMovie findByUserAndMovie(User user, Movie movie);
    List<UserMovie> findByUserAndMovieIn(User user, List<Movie> movies);
    List<UserMovie> findAllByUser(User user);
    Boolean existsByUserAndMovie(User user, Movie movie);
}
