package hello.moviebook.Book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/book")
public class BookController {
    @Autowired
    BookService bookService;

    // 네이버 책 키워드 도서 조회 API -> DB 저장
    @PostMapping()
    public ResponseEntity<String> updateBookDB(@RequestParam("keyword") String keyword) {
        List<Book> bookList = new ArrayList<>();
        bookService.updateBookData(keyword, bookList);

        return ResponseEntity.ok("Book data inserted successfully!\n");
    }
}
