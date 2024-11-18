package hello.moviebook.movie.dto;

import hello.moviebook.movie.domain.Movie;
import hello.moviebook.movie.domain.UserMovie;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class UserMovieRes {
    private Long movieId;
    private String movieName;
    private String poster;
    private Double rating;

    public static UserMovieRes defaultBuilder(UserMovie userMovie, Movie movie) {
        return UserMovieRes.builder()
                .movieId(movie.getMovieId())
                .movieName(movie.getMovieName())
                .poster(movie.getPoster())
                .rating(userMovie.getRating())
                .build();
    }
}
