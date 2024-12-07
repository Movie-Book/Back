package hello.moviebook.book.dto;

import hello.moviebook.book.domain.Book;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RecommendBookRes {
    String isbn;
    String bookName;
    String description;
    String image;

    @Builder
    public RecommendBookRes(Book book) {
        this.isbn = book.getIsbn();
        this.bookName = book.getBookName();
        this.description = book.getDescription();
        this.image = book.getImage();
    }
}
