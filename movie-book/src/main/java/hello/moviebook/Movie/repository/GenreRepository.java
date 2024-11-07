package hello.moviebook.movie.repository;

import hello.moviebook.movie.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    Genre findGenreById(Long id);
}
