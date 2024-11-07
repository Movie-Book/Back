package hello.moviebook.movie.dto;

import lombok.Getter;

@Getter
public class MovieRatingReq {
    private Long movieId;
    private Double rating;
}
