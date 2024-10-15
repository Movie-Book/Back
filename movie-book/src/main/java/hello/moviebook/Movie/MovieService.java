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

        List<Movie> movieList = movieRepository.findMoviesByUserPreferredGenres(userGenre.getAction(), "action", userGenre.getDrama(), "drama",
                userGenre.getComedy(), "comedy", userGenre.getRomance(), "romance", userGenre.getThriller(), "thriller",
                userGenre.getHorror(), "horror", userGenre.getSf(), "sf", userGenre.getFantasy(), "fantasy",
                userGenre.getAnimation(), "animation", userGenre.getDocumentary(), "documentary", userGenre.getCrime(), "crime");

        List<MovieInfoRes> movieInfoResList = new ArrayList<>();
        for (Movie movie : movieList) {
            MovieInfoRes movieInfoRes = new MovieInfoRes(movie.getMovieId(), movie.getMovieName(), movie.getPoster());
            movieInfoResList.add(movieInfoRes);
        }

        log.info("선호 장르 영화 개수 : {}", movieInfoResList.size());

        return movieInfoResList;
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
