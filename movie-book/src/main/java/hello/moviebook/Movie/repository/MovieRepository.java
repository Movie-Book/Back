package hello.moviebook.movie.repository;

import hello.moviebook.movie.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    // 키워드 채워주기 위해서 키워드 값이 NULL인 데이터 조회
    List<Movie> findByKeywordIsNull();

    // 유저 선호 장르에 속하는 영화 리스트 조회
    @Query("SELECT m FROM Movie m JOIN UserGenre ug ON "
            + "(:action = 1 AND m.genre LIKE %:actionGenre%) OR "
            + "(:drama = 1 AND m.genre LIKE %:dramaGenre%) OR "
            + "(:comedy = 1 AND m.genre LIKE %:comedyGenre%) OR "
            + "(:romance = 1 AND m.genre LIKE %:romanceGenre%) OR "
            + "(:thriller = 1 AND m.genre LIKE %:thrillerGenre%) OR "
            + "(:horror = 1 AND m.genre LIKE %:horrorGenre%) OR "
            + "(:sf = 1 AND m.genre LIKE %:sfGenre%) OR "
            + "(:fantasy = 1 AND m.genre LIKE %:fantasyGenre%) OR "
            + "(:animation = 1 AND m.genre LIKE %:animationGenre%) OR "
            + "(:documentary = 1 AND m.genre LIKE %:documentaryGenre%) OR "
            + "(:crime = 1 AND m.genre LIKE %:crimeGenre%)")
    List<Movie> findMoviesByUserPreferredGenres(
            @Param("action") Long action, @Param("actionGenre") String actionGenre,
            @Param("drama") Long drama, @Param("dramaGenre") String dramaGenre,
            @Param("comedy") Long comedy, @Param("comedyGenre") String comedyGenre,
            @Param("romance") Long romance, @Param("romanceGenre") String romanceGenre,
            @Param("thriller") Long thriller, @Param("thrillerGenre") String thrillerGenre,
            @Param("horror") Long horror, @Param("horrorGenre") String horrorGenre,
            @Param("sf") Long sf, @Param("sfGenre") String sfGenre,
            @Param("fantasy") Long fantasy, @Param("fantasyGenre") String fantasyGenre,
            @Param("animation") Long animation, @Param("animationGenre") String animationGenre,
            @Param("documentary") Long documentary, @Param("documentaryGenre") String documentaryGenre,
            @Param("crime") Long crime, @Param("crimeGenre") String crimeGenre);

    Movie findMovieByMovieId(Long movieId);
}
