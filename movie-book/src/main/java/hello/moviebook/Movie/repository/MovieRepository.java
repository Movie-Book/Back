package hello.moviebook.movie.repository;

import hello.moviebook.movie.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {
    // 키워드 채워주기 위해서 키워드 값이 NULL인 데이터 조회
    List<Movie> findByKeywordIsNull();
    Movie findMovieByMovieId(Long movieId);
    List<Movie> findMoviesByMovieNameLike(String keyword);
}
