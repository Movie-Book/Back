package hello.moviebook.book.service;

import hello.moviebook.book.domain.Book;
import hello.moviebook.book.domain.UserBook;
import hello.moviebook.book.dto.BookDTO;
import hello.moviebook.book.dto.RatingBookReq;
import hello.moviebook.book.repository.BookRepository;
import hello.moviebook.book.repository.UserBookRepository;
import hello.moviebook.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserBookService {
    private final UserBookRepository userBookRepository;
    private final BookRepository bookRepository;

    @Transactional
    public void saveUserBookList(User user, List<String> recBookList) {
        // 사용자와 관련된 기존 UserBook 데이터 조회
        List<Book> existingBooks = userBookRepository.findAllByUser(user).stream()
                .map(UserBook::getBook)
                .toList();

        // 추천 도서 중 기존 데이터에 없는 책 필터링
        List<UserBook> userBookList = recBookList.stream()
                .map(bookRepository::findBookByBookName)
                .filter(book -> !existingBooks.contains(book)) // 기존 데이터에 없는 책만 필터링
                .map(book -> UserBook.builder()
                        .user(user)
                        .book(book)
                        .build())
                .toList();

        userBookRepository.saveAll(userBookList);
    }

    public List<BookDTO> getUserBookList(User user) {
        List<UserBook> userBookList = userBookRepository.findAllByUser(user);

        return userBookList.stream()
                .map(userBook -> BookDTO.builder()
                        .userBook(userBook)
                        .build())
                .toList();
    }

    @Transactional
    public boolean ratingBook(User user, RatingBookReq ratingBookReq) {
        UserBook userBook = userBookRepository.findByUserAndBook(user, bookRepository.findBookByIsbn(ratingBookReq.getIsbn()));

        if (userBook == null)
            return false;

        userBook.setBookRating(ratingBookReq.getRating());
        userBook.setBookReview(ratingBookReq.getReview());

        userBookRepository.save(userBook);
        return true;
    }
}
