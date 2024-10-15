package hello.moviebook.Movie;

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
            + "(:action = TRUE AND ug.action = TRUE AND m.genreEn LIKE %:actionGenre%) OR "
            + "(:drama = TRUE AND ug.drama = TRUE AND m.genreEn LIKE %:dramaGenre%) OR "
            + "(:comedy = TRUE AND ug.comedy = TRUE AND m.genreEn LIKE %:comedyGenre%) OR "
            + "(:romance = TRUE AND ug.romance = TRUE AND m.genreEn LIKE %:romanceGenre%) OR "
            + "(:thriller = TRUE AND ug.thriller = TRUE AND m.genreEn LIKE %:thrillerGenre%) OR "
            + "(:horror = TRUE AND ug.horror = TRUE AND m.genreEn LIKE %:horrorGenre%) OR "
            + "(:sf = TRUE AND ug.sf = TRUE AND m.genre LIKE %:sfGenre%) OR "
            + "(:fantasy = TRUE AND ug.fantasy = TRUE AND m.genreEn LIKE %:fantasyGenre%) OR "
            + "(:animation = TRUE AND ug.animation = TRUE AND m.genreEn LIKE %:animationGenre%) OR "
            + "(:documentary = TRUE AND ug.documentary = TRUE AND m.genreEn LIKE %:documentaryGenre%) OR "
            + "(:crime = TRUE AND ug.crime = TRUE AND m.genreEn LIKE %:crimeGenre%)")
    List<Movie> findMoviesByUserPreferredGenres(
            @Param("action") Boolean action, @Param("actionGenre") String actionGenre,
            @Param("drama") Boolean drama, @Param("dramaGenre") String dramaGenre,
            @Param("comedy") Boolean comedy, @Param("comedyGenre") String comedyGenre,
            @Param("romance") Boolean romance, @Param("romanceGenre") String romanceGenre,
            @Param("thriller") Boolean thriller, @Param("thrillerGenre") String thrillerGenre,
            @Param("horror") Boolean horror, @Param("horrorGenre") String horrorGenre,
            @Param("sf") Boolean sf, @Param("sfGenre") String sfGenre,
            @Param("fantasy") Boolean fantasy, @Param("fantasyGenre") String fantasyGenre,
            @Param("animation") Boolean animation, @Param("animationGenre") String animationGenre,
            @Param("documentary") Boolean documentary, @Param("documentaryGenre") String documentaryGenre,
            @Param("crime") Boolean crime, @Param("crimeGenre") String crimeGenre);

    Movie findMovieByMovieId(Long movieId);
}
