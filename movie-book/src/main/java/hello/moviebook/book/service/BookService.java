package hello.moviebook.book.service;

import hello.moviebook.book.domain.Book;
import hello.moviebook.book.dto.BookDescriptRes;
import hello.moviebook.book.dto.FlaskReqDTO;
import hello.moviebook.book.dto.RecommendBookRes;
import hello.moviebook.book.repository.BookRepository;
import hello.moviebook.book.repository.BookSpecifications;
import hello.moviebook.movie.domain.UserLikeGenre;
import hello.moviebook.movie.domain.UserMovie;
import hello.moviebook.movie.repository.UserDislikeGenreRepository;
import hello.moviebook.movie.repository.UserLikeGenreRepository;
import hello.moviebook.movie.repository.UserMovieRepository;
import hello.moviebook.user.domain.User;
import hello.moviebook.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final UserMovieRepository userMovieRepository;
    private final UserRepository userRepository;
    private final UserLikeGenreRepository userLikeGenreRepository;
    private final UserDislikeGenreRepository userDislikeGenreRepository;
    private final WebClient.Builder webClientBuilder;
    private WebClient webClient;

    @PostConstruct
    public void init() {
        this.webClient = webClientBuilder.baseUrl("http://35.216.42.151:5000").build();
    }

    @PersistenceContext  // EntityManager 주입
    private EntityManager entityManager;

    public List<BookDescriptRes> getBookListByKeywords(List<String> keywords) {
        Specification<Book> spec = Specification.where(BookSpecifications.hasKeywords(keywords));

        return bookRepository.findAll(spec).stream()
                .map(BookDescriptRes::defaultBuilder)
                .toList();
    }

    public List<BookDescriptRes> getBookListByMovieKeywords(String id) {
        User user = userRepository.findUserById(id);
        List<UserLikeGenre> userLikeGenres = userLikeGenreRepository.findAllByUser(user);
        List<UserMovie> userMovieTopList = userMovieRepository.findAllByUser(user).stream()
                .filter(userMovie -> userMovie.getRating().compareTo(4L) >= 0)
                .toList();
        List<String> topKeywords = new ArrayList<>();

        // 장르 별로 최빈 키워드 뽑아 리스트에 저장
        for (UserLikeGenre likeGenre : userLikeGenres) {
            List<String> movieKeywords = userMovieTopList.stream()
                    .filter(userMovie -> userMovie.getMovie().getGenre().contains(likeGenre.getGenre().getName()))
                    .map(userMovie -> userMovie.getMovie().getKeywordKor())
                    .toList();
            log.info("Keywords : {}", movieKeywords);

             topKeywords.addAll(getTopKeywords(movieKeywords));
        }

        log.info("Top Keyword : {}", topKeywords);

        // 최빈 키워드 포함하는 책 리턴
        return bookRepository.findBooksByKeywords(topKeywords).stream()
                .map(BookDescriptRes::defaultBuilder)
                .toList();
    }

    private List<String> getTopKeywords(List<String> keywordList) {
        Map<String, Integer> keywordCounts = new HashMap<>();

        // 키워드를 개별적으로 분리하고 빈도수를 계산
        for (String keywordString : keywordList) {
            String[] keywords = keywordString.split(",");
            for (String keyword : keywords) {
                keyword = keyword.trim();
                // 현재 키워드의 빈도수를 가져오거나, 없으면 기본값 0으로 초기화 후 1을 더함
                keywordCounts.put(keyword, keywordCounts.getOrDefault(keyword, 0) + 1);
            }
        }

        // 자주 등장하는 키워드 topN 개 추출
        return getTopKeywordsByKeywordCounts(keywordCounts);
    }

    // 최빈 키워드 추출
    private static List<String> getTopKeywordsByKeywordCounts(Map<String, Integer> keywordCounts) {
        int topN = 10;

        return keywordCounts.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(topN)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public List<RecommendBookRes> getRecommendBookList(User user) {
        FlaskReqDTO flaskReqDTO = createFlaskReq(user);

        // Flask API URL
        String flaskUrl = "/rec-book";

        // 요청 데이터 생성
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("prefer_genre", flaskReqDTO.getUserLikeGenreList());   // 선호 장르
        requestBody.put("dislike_genre", flaskReqDTO.getUserDislikeGenreList());  // 비선호 장르
        requestBody.put("movie_index", flaskReqDTO.getMovieList());      // 영화 인덱스
        requestBody.put("rating", flaskReqDTO.getRatingList());                // 영화 평점
        requestBody.put("n_books", flaskReqDTO.getNBooks());

        log.info("Movie prefer_genre : {}", flaskReqDTO.getUserLikeGenreList());
        log.info("Movie dislike_genre : {}", flaskReqDTO.getUserDislikeGenreList());
        log.info("Movie idx : {}", flaskReqDTO.getMovieList());
        log.info("Movie rating : {}", flaskReqDTO.getRatingList());
        log.info("nbooks : {}", flaskReqDTO.getNBooks());

        return createRecBookList(webClient.post()
                .uri(flaskUrl)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .block());
    }

    private FlaskReqDTO createFlaskReq(User user) {
        Long nBooks = 10L;
        List<String> userLikeGenres = userLikeGenreRepository.findAllByUser(user).stream()
                .map(userLikeGenre -> userLikeGenre.getGenre().getNameEn())
                .toList();
        List<String> userDislikeGenres = userDislikeGenreRepository.findAllByUser(user).stream()
                .map(userDislikeGenre -> userDislikeGenre.getGenre().getNameEn())
                .toList();
        List<Long> movieList = userMovieRepository.findAllByUser(user).stream()
                .map(userMovie -> userMovie.getMovie().getMovieId())
                .toList();
        List<Long> ratingList = userMovieRepository.findAllByUser(user).stream()
                .map(UserMovie::getRating)
                .toList();

        return new FlaskReqDTO(userLikeGenres, userDislikeGenres, movieList, ratingList, nBooks);
    }

    private List<RecommendBookRes> createRecBookList(List<String> recBookList) {
        return recBookList.stream()
                .map(bookRepository::findBookByBookName)
                .filter(Objects::nonNull)
                .map(RecommendBookRes::new)
                .toList();
    }
}
