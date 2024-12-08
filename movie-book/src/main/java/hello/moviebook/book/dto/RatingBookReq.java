package hello.moviebook.book.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class RatingBookReq {

    @Schema(description = "도서 ISBN 번호", example = "9788932924458")
    private String isbn;

    @Schema(description = "평점 (1~5 사이의 정수)", example = "5")
    private Long rating;

    @Schema(description = "리뷰 내용", example = "이 책은 매우 유익하고 재미있었습니다!")
    private String review;
}
