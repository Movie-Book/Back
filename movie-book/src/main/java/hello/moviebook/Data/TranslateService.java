package hello.moviebook.data;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import hello.moviebook.movie.domain.Movie;
import hello.moviebook.movie.repository.MovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class TranslateService {
    private final Translate translate;
    private final MovieRepository movieRepository;

    public TranslateService(MovieRepository movieRepository) {
        String keyPath = System.getProperty("user.home") + "/.ssh/scenic-aileron-437613-r2-d79cc15dd382.json";

        try (FileInputStream serviceAccountStream = new FileInputStream(keyPath)) {
            // 명시적으로 서비스 계정 키를 설정
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccountStream);
            TranslateOptions options = TranslateOptions.newBuilder()
                    .setCredentials(credentials)
                    .build();
            this.translate = options.getService();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load Google credentials", e);
        }
        this.movieRepository = movieRepository;
    }

    public void keywordToKo() {
        // 전체 영화 리스트
        List<Movie> movieList = movieRepository.findAll();

        for (Movie movie : movieList) {
            // 해당 영화의 키워드
            String keywords = movie.getKeyword();

            // 한글로 해석된 영화의 키워드
            Translation translation = translate.translate(keywords, Translate.TranslateOption.targetLanguage("ko"));

            // 번역 키워드 출력 테스트
            log.info("keywords : {}", translation.getTranslatedText());
            movie.setKeywordKor(translation.getTranslatedText());

            // 번역 데이터 저장
            movieRepository.save(movie);
        }

        log.info("키워드 정보 저장 완료");
    }
}
