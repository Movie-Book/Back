package hello.moviebook.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FlaskReqDTO {
    private List<String> userLikeGenreList;
    private List<String> userDislikeGenreList;
    private List<Long> movieList;
    private List<Long> ratingList;
    private Long nBooks;
}
