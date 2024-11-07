package hello.moviebook.movie.service;

import hello.moviebook.movie.domain.*;
import hello.moviebook.movie.dto.UserMovieRes;
import hello.moviebook.movie.repository.*;
import hello.moviebook.movie.dto.MovieInfoRes;
import hello.moviebook.movie.dto.MovieRatingReq;
import hello.moviebook.user.domain.User;
import hello.moviebook.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovieService {

    private final GenreService genreService;
    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final UserMovieRepository userMovieRepository;
    private final MovieRepository movieRepository;

    // 유저 -> 유저 취향 장르 -> 장르에 맞는 영화 리스트 조회
    public List<MovieInfoRes> getPreferredMovieList(String id) {
        User user = userRepository.findUserById(id);
        List<Genre> userLikeGenre = genreRepository.findAllById(genreService.getUserLikeGenre(id));
        List<Genre> userDislikeGenre = genreRepository.findAllById(genreService.getUserDislikeGenre(id));

        Specification<Movie> spec = Specification.where(MovieSpecifications.hasPreferredGenres(user, userLikeGenre))
                .and(MovieSpecifications.doesNotHaveDislikedGenres(user, userDislikeGenre));

        return movieRepository.findAll(spec).stream()
                .map(MovieInfoRes::defaultBuilder)
                .toList();
    }

    // 유저 영화 리뷰 저장
    @Transactional
    public String saveUserMovieList(List<MovieRatingReq> movieList, String userId) {
        User user = userRepository.findUserById(userId);

        for (MovieRatingReq movieReq : movieList) {
            Movie movie = movieRepository.findMovieByMovieId(movieReq.getMovieId());

            if (movie == null) {
                log.info("영화 정보가 없어요.");
                return null;
            }

            UserMovie userMovie = new UserMovie(user, movie, movieReq.getRating());
            userMovieRepository.save(userMovie);
        }
        return "유저 선호 영화 정보가 저장되었습니다.";
    }


    @Transactional
    public void updateMovieRating(String id, List<MovieRatingReq> ratingList) {
        User user = userRepository.findUserById(id);
        for (MovieRatingReq rating : ratingList) {
            UserMovie movie = userMovieRepository.findByUserAndMovie(user, movieRepository.findMovieByMovieId(rating.getMovieId()));

            if (!movie.getRating().equals(rating.getRating())) {
                movie.setRating(rating.getRating());
                userMovieRepository.save(movie);
            }
        }
    }

    public List<UserMovieRes> getUserMovieList(String id) {
        List<UserMovie> userMovieList = userMovieRepository.findAllByUser(userRepository.findUserById(id));
        List<UserMovieRes> userMovieResList = new ArrayList<>();

        for (UserMovie userMovie : userMovieList) {
            userMovieResList.add(UserMovieRes.defaultBuilder(userMovie, userMovie.getMovie()));
        }

        return userMovieResList;
    }

    public List<UserMovieRes> searchMovieList(String id, String keyword) {
        List<Movie> movieList = movieRepository.findMoviesByMovieNameLike("%" + keyword + "%");
        List<UserMovie> userMovieList = userMovieRepository.findByUserAndMovieIn(userRepository.findUserById(id), movieList);

        List<UserMovieRes> userMovieResList = new ArrayList<>();
        for (Movie movie : movieList) {
            userMovieResList.add(new UserMovieRes(movie.getMovieId(), movie.getMovieName(), movie.getPoster(), getMovieRating(movie, userMovieList)));
        }

        return userMovieResList;
    }

    private Double getMovieRating(Movie movie, List<UserMovie> userMovieList) {
        return userMovieList.stream()
                .filter(usermovie -> usermovie.getMovie().equals(movie))
                .map(UserMovie::getRating)
                .findFirst().orElse(0.0);
    }
}
