package hello.moviebook.movie.dto;

import lombok.Data;

@Data
public class MovieInfoRes {
    private Long movieId;

    private String movieName;

    private String poster;

    public MovieInfoRes(Long movieId, String movieName, String poster) {
        this.movieId = movieId;
        this.movieName = movieName;
        this.poster = poster;
    }
}
