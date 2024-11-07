package hello.moviebook.movie.dto;

import hello.moviebook.movie.domain.Movie;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Builder
public class MovieInfoRes {
    private Long movieId;
    private String movieName;
    private String poster;

    public static MovieInfoRes defaultBuilder(Movie movie) {
        return MovieInfoRes.builder()
                .movieId(movie.getMovieId())
                .movieName(movie.getMovieName())
                .poster(movie.getPoster())
                .build();
    }
}
