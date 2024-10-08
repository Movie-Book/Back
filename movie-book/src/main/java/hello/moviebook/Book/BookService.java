package hello.moviebook.Book;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@Slf4j
public class BookService {
    private final String defaultURL = "https://openapi.naver.com/v1/search/book.json?";
    private final String option = "display=100&sort=sim&";
    private final String clientId = "luVmm20hRvg7dhaITaOy"; // 클라이언트 아이디
    private final String clientSecret = "jjIk8pafab"; // 클라이언트 시크릿

    @Autowired
    BookRepository bookRepository;

    // Book 데이터베이스 Insert
    public void updateBookData(String keyword, List<Book> bookList) {

        ObjectMapper objectMapper = new ObjectMapper();

        for (int i = 1; i <= 901; i+=100) {
            try {
                StringBuffer result = new StringBuffer();

                String encodedQuery = URLEncoder.encode(keyword, StandardCharsets.UTF_8.toString());
                String startNo = "&start=" + i;

                // 도서 검색 api
                URL bookUrl = new URL(defaultURL + option + "query=" + encodedQuery + startNo);
                HttpURLConnection bookSearchUrlConnection = (HttpURLConnection) bookUrl.openConnection();
                bookSearchUrlConnection.setRequestMethod("GET");
                bookSearchUrlConnection.setRequestProperty("Content-type", "application/json");
                bookSearchUrlConnection.setRequestProperty("X-Naver-Client-Id", clientId);
                bookSearchUrlConnection.setRequestProperty("X-Naver-Client-Secret", clientSecret);

                log.info(bookUrl.toString());

                // BufferedReader로 응답을 UTF-8로 읽어오기
                BufferedReader bf = new BufferedReader(new InputStreamReader(bookSearchUrlConnection.getInputStream(), StandardCharsets.UTF_8));

                String line;
                while ((line = bf.readLine()) != null) {
                    result.append(line);
                }

                // JSON 파싱
                JSONObject jsonResponse = new JSONObject(result.toString());
                JSONArray items = jsonResponse.getJSONArray("items");

                for (int j = 0; j < items.length(); j++) {
                    JSONObject item = items.getJSONObject(j);
                    Book book = objectMapper.readValue(item.toString(), Book.class);

                    String isbn = book.getIsbn();
                    String author = book.getAuthor();
                    String bookName = book.getBookName();
                    String description = book.getDescription();
                    String publisher = book.getPublisher();

                    if (isbn == null || author == null || bookName == null || description == null || publisher == null)
                        continue;

                    if (description.length() > 255) {
                        book.setDescription(description.substring(0, 255));
                    }

                    if (!bookRepository.existsByIsbn(isbn))
                        bookList.add(book);
                }
            }
            catch (IOException e) {
                log.error("An error occurred while parsing and saving book data: ", e);
            }
        }
        // 책 리스트 DB에 저장
        bookRepository.saveAll(bookList);
        log.info("Books inserted successfully into the database.");
    }
}
