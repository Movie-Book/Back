package hello.moviebook.book.controller;

import hello.moviebook.book.dto.BookDTO;
import hello.moviebook.book.dto.BookDescriptRes;
import hello.moviebook.book.dto.RatingBookReq;
import hello.moviebook.book.dto.RecommendBookRes;
import hello.moviebook.book.service.BookService;
import hello.moviebook.book.service.UserBookService;
import hello.moviebook.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/book")
@RequiredArgsConstructor
public class BookControllerImpl implements BookController{
    private final BookService bookService;
    private final UserBookService userBookService;
    private final UserRepository userRepository;

    // 줄거리 키워드 기반 도서 검색
    @GetMapping("/search-descript")
    public ResponseEntity<List<BookDescriptRes>> getSimilarBookList(@RequestParam("keywords") List<String> keywords) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.getBookListByKeywords(keywords));
    }

    // 선호 영화 리스트 최빈 키워드 -> 도서 검색
    @GetMapping("/search-keyword")
    public ResponseEntity<List<BookDescriptRes>> getKeywordBookList(Authentication authentication) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.getBookListByMovieKeywords(authentication.getName()));
    }

    // 사용자 맞춤 추천 도서 리스트
    @PostMapping("/recommend")
    public ResponseEntity<List<RecommendBookRes>> getRecommendBookList(Authentication authentication) {
        if (authentication == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        List<RecommendBookRes> recBookList = bookService.getRecommendBookList(userRepository.findUserById(authentication.getName()));
        if (recBookList == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        return ResponseEntity.status(HttpStatus.OK).body(recBookList);
    }

    @GetMapping("/rec-list")
    public ResponseEntity<List<BookDTO>> getUserBookList(Authentication authentication) {
        if (authentication == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        List<BookDTO> bookDTOList = userBookService.getUserBookList(userRepository.findUserById(authentication.getName()));
        if (bookDTOList == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        return ResponseEntity.status(HttpStatus.OK).body(bookDTOList);
    }

    @PatchMapping("/rating")
    public ResponseEntity<String> ratingBook(@RequestBody RatingBookReq ratingBookReq, Authentication authentication) {
        if (authentication == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("잘못된 로그인입니다.");

        if (!userBookService.ratingBook(userRepository.findUserById(authentication.getName()), ratingBookReq))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("해당 도서의 평가를 실패했습니다.");
        return ResponseEntity.status(HttpStatus.OK).body("해당 도서의 평가 정보가 저장되었습니다.");
    }
}