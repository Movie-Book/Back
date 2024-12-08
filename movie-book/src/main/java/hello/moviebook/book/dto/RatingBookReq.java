package hello.moviebook.book.dto;

import lombok.Getter;

@Getter
public class RatingBookReq {
    private String isbn;
    private Long rating;
    private String review;
}
