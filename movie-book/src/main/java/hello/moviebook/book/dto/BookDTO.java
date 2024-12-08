package hello.moviebook.book.dto;

import hello.moviebook.book.domain.UserBook;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(description = "책 정보 DTO")
public class BookDTO {
    @Schema(description = "책 이름", example = "나인")
    private String name;

    @Schema(description = "책 이미지 URL", example = "https://contents.kyobobook.co.kr/sih/fit-in/458x0/pdt/9788936438609.jpg")
    private String image;

    @Schema(description = "책 평점", example = "4")
    private Long rating;

    @Schema(description = "책 리뷰", example = "흥미진진하고 재밌는 이야기입니다.")
    private String review;


    @Builder
    public BookDTO(UserBook userBook) {
        this.name = userBook.getBook().getBookName();
        this.image = userBook.getBook().getImage();
        this.rating = userBook.getBookRating();
        this.review = userBook.getBookReview();
    }
}
