package hello.moviebook.Movie;

import hello.moviebook.Movie.DTO.MovieInfoRes;
import hello.moviebook.Movie.DTO.MovieRatingReq;
import hello.moviebook.User.User;
import hello.moviebook.User.UserRepository;
import hello.moviebook.UserGenre.UserGenre;
import hello.moviebook.UserGenre.UserGenreRepository;
import hello.moviebook.UserMovie.UserMovie;
import hello.moviebook.UserMovie.UserMovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovieService {

    private final UserRepository userRepository;
    private final UserGenreRepository userGenreRepository;
    private final UserMovieRepository userMovieRepository;
    private final MovieRepository movieRepository;

    // 유저 -> 유저 취향 장르 -> 장르에 맞는 영화 리스트 조회
    public List<MovieInfoRes> getPreferredMovieList(String userId) {
        User user = userRepository.findUserById(userId);

        if (user == null)
            return null;

        UserGenre userGenre = userGenreRepository.findUserGenreByUser(user);
        if (userGenre == null)
            return null;

        List<Movie> preferredMovieList = movieRepository.findMoviesByUserPreferredGenres(userGenre.getAction(), "액션", userGenre.getDrama(), "드라마",
                userGenre.getComedy(), "코미디", userGenre.getRomance(), "로맨스", userGenre.getThriller(), "스릴러",
                userGenre.getHorror(), "공포", userGenre.getSf(), "SF", userGenre.getFantasy(), "판타지",
                userGenre.getAnimation(), "애니메이션", userGenre.getDocumentary(), "다큐멘터리", userGenre.getCrime(), "범죄");

        List<Movie> filteredMovieList = filterMoviesDislikedGenres(preferredMovieList, userGenre);

        List<MovieInfoRes> movieInfoResList = new ArrayList<>();
        for (Movie movie : filteredMovieList) {
            MovieInfoRes movieInfoRes = new MovieInfoRes(movie.getMovieId(), movie.getMovieName(), movie.getPoster());
            movieInfoResList.add(movieInfoRes);
        }

        log.info("선호 장르 영화 개수 : {}", movieInfoResList.size());

        return movieInfoResList;
    }

    // 비선호 장르 영화 필터링
    public List<Movie> filterMoviesDislikedGenres(List<Movie> movieList, UserGenre userGenre) {
        // 비선호 장르를 확인하고, 그 장르에 속하는 영화들을 제외
        return movieList.stream()
                .filter(movie -> !(userGenre.getAction() == -1L && movie.getGenre().contains("액션")))
                .filter(movie -> !(userGenre.getDrama() == -1L && movie.getGenre().contains("드라마")))
                .filter(movie -> !(userGenre.getComedy() == -1L && movie.getGenre().contains("코미디")))
                .filter(movie -> !(userGenre.getRomance() == -1L && movie.getGenre().contains("로맨스")))
                .filter(movie -> !(userGenre.getThriller() == -1L && movie.getGenre().contains("스릴러")))
                .filter(movie -> !(userGenre.getHorror() == -1L && movie.getGenre().contains("공포")))
                .filter(movie -> !(userGenre.getSf() == -1L && movie.getGenre().contains("SF")))
                .filter(movie -> !(userGenre.getFantasy() == -1L && movie.getGenre().contains("판타지")))
                .filter(movie -> !(userGenre.getAnimation() == -1L && movie.getGenre().contains("애니메이션")))
                .filter(movie -> !(userGenre.getDocumentary() == -1L && movie.getGenre().contains("다큐멘터리")))
                .filter(movie -> !(userGenre.getCrime() == -1L && movie.getGenre().contains("범죄")))
                .collect(Collectors.toList());
    }

    // 유저 영화 리뷰 저장
    public String saveUserMovieList(List<MovieRatingReq> movieList, String userId) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            log.info("유저 정보가 없어요");
            return null;
        }

        for (MovieRatingReq movieReq : movieList) {
            log.info(movieReq.toString());

            Movie movie = movieRepository.findMovieByMovieId(movieReq.getMovieId());

            if (movie == null) {
                log.info("영화 정보가 없어요");
                return null;
            }

            UserMovie userMovie = new UserMovie(user, movie, movieReq.getRating());
            userMovieRepository.save(userMovie);
        }

        return "유저 선호 영화 정보가 저장되었습니다.";
    }
}
