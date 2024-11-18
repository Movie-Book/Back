package hello.moviebook.book.repository;

import hello.moviebook.book.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book>, BookRepositoryCustom {
    boolean existsByIsbn(String isbn);
}
