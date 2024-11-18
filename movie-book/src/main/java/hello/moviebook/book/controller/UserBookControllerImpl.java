package hello.moviebook.book.controller;

import hello.moviebook.book.dto.BookDescriptRes;
import hello.moviebook.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/api/v1/book")
@RequiredArgsConstructor
public class UserBookControllerImpl {
    private final BookService bookService;

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
}