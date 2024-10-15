package hello.moviebook.Movie.DTO;

import lombok.Data;

@Data
public class MovieRatingReq {
    private Long movieId;
    private Double rating;
}
