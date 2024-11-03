package hello.moviebook.movie.service;

import hello.moviebook.movie.dto.UserGenreReq;
import hello.moviebook.movie.repository.MovieRepository;
import hello.moviebook.movie.repository.UserGenreRepository;
import hello.moviebook.movie.repository.UserMovieRepository;
import hello.moviebook.movie.domain.Movie;
import hello.moviebook.movie.domain.UserGenre;
import hello.moviebook.movie.domain.UserMovie;
import hello.moviebook.movie.dto.MovieInfoRes;
import hello.moviebook.movie.dto.MovieRatingReq;
import hello.moviebook.user.domain.User;
import hello.moviebook.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.List;

import static hello.moviebook.movie.dto.UserGenreReq.dislikeGenreBuilder;
import static hello.moviebook.movie.dto.UserGenreReq.preferGenreBuilder;

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
    @Transactional
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

    @Transactional
    public UserGenre updateUserPreferredGenre(String userId, UserGenreReq userGenreReq) {
        User user = userRepository.findUserById(userId);
        UserGenre userGenre = userGenreRepository.findUserGenreByUser(user);

        if (user == null)
            return null;

        // 유저 취향 장르 정보 설정
        if (userGenreReq.getAction())
            userGenre.setAction(1L);
        if (userGenreReq.getDrama())
            userGenre.setDrama(1L);
        if (userGenreReq.getComedy())
            userGenre.setComedy(1L);
        if (userGenreReq.getRomance())
            userGenre.setRomance(1L);
        if (userGenreReq.getThriller())
            userGenre.setThriller(1L);
        if (userGenreReq.getHorror())
            userGenre.setHorror(1L);
        if (userGenreReq.getSf())
            userGenre.setSf(1L);
        if (userGenreReq.getFantasy())
            userGenre.setFantasy(1L);
        if (userGenreReq.getAnimation())
            userGenre.setAnimation(1L);
        if (userGenreReq.getDocumentary())
            userGenre.setDocumentary(1L);
        if (userGenreReq.getCrime())
            userGenre.setCrime(1L);

        userGenreRepository.save(userGenre);

        return userGenre;
    }

    @Transactional
    public UserGenre updateUserDislikedGenre(String userId, UserGenreReq userGenreReq) {
        User user = userRepository.findUserById(userId);
        UserGenre userGenre = userGenreRepository.findUserGenreByUser(user);

        if (user == null)
            return null;

        // 유저 취향 장르 정보 설정
        if (userGenreReq.getAction())
            userGenre.setAction(-1L);
        if (userGenreReq.getDrama())
            userGenre.setDrama(-1L);
        if (userGenreReq.getComedy())
            userGenre.setComedy(-1L);
        if (userGenreReq.getRomance())
            userGenre.setRomance(-1L);
        if (userGenreReq.getThriller())
            userGenre.setThriller(-1L);
        if (userGenreReq.getHorror())
            userGenre.setHorror(-1L);
        if (userGenreReq.getSf())
            userGenre.setSf(-1L);
        if (userGenreReq.getFantasy())
            userGenre.setFantasy(-1L);
        if (userGenreReq.getAnimation())
            userGenre.setAnimation(-1L);
        if (userGenreReq.getDocumentary())
            userGenre.setDocumentary(-1L);
        if (userGenreReq.getCrime())
            userGenre.setCrime(-1L);

        userGenreRepository.save(userGenre);

        return userGenre;
    }

    @Transactional
    public void updateMovieRating(String id, List<MovieRatingReq> ratingList) {
        User user = userRepository.findUserById(id);
        for (MovieRatingReq rating : ratingList) {
            UserMovie movie = userMovieRepository.findByUserAndMovie(user, movieRepository.findMovieByMovieId(rating.getMovieId()));

            if (!movie.getMovieRating().equals(rating.getRating())) {
                movie.setMovieRating(rating.getRating());
                userMovieRepository.save(movie);
            }
        }
    }

    public UserGenreReq getPreferredGenre(String id) {
        return preferGenreBuilder(userGenreRepository.findUserGenreByUser(userRepository.findUserById(id)));
    }

    public UserGenreReq getDislikedGenre(String id) {
        return dislikeGenreBuilder(userGenreRepository.findUserGenreByUser(userRepository.findUserById(id)));
    }
}
