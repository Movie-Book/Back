package hello.moviebook.book.repository;

import hello.moviebook.book.domain.Book;
import hello.moviebook.book.domain.UserBook;
import hello.moviebook.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserBookRepository extends JpaRepository<UserBook, Long> {
    List<UserBook> findAllByUser(User user);
    UserBook findByUserAndBook(User user, Book book);
}
