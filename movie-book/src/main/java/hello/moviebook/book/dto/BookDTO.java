package hello.moviebook.book.dto;

import hello.moviebook.book.domain.UserBook;
import lombok.Builder;
import lombok.Getter;

@Getter
public class BookDTO {
    private String name;
    private String image;
    private Double rating;
    private String review;

    @Builder
    public BookDTO(UserBook userBook) {
        this.name = userBook.getBook().getBookName();
        this.image = userBook.getBook().getImage();
        this.rating = userBook.getBookRating();
        this.review = userBook.getBookReview();
    }
}
