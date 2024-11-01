package hello.moviebook.movie.dto;

import lombok.Data;

@Data
public class MovieRatingReq {
    private Long movieId;
    private Double rating;
}
