package hello.moviebook.movie.repository;

import hello.moviebook.movie.domain.UserMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMovieRepository extends JpaRepository<UserMovie, Long> {
}
