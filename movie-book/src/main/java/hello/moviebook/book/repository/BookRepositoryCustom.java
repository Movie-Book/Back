package hello.moviebook.book.repository;

import hello.moviebook.book.domain.Book;

import java.util.List;

public interface BookRepositoryCustom {
    List<Book> findBooksByKeywords(List<String> keywords);
}
