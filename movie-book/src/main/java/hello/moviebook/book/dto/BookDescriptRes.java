package hello.moviebook.book.dto;

import hello.moviebook.book.domain.Book;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BookDescriptRes {
    private String isbn;
    private String bookName;
    private String description;
    private String genre;

    public static BookDescriptRes defaultBuilder(Book book) {
        return BookDescriptRes.builder()
                .isbn(book.getIsbn())
                .bookName(book.getBookName())
                .description(book.getDescription())
                .genre(book.getGenre())
                .build();
    }
}
