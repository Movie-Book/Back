package hello.moviebook.movie.controller;

import hello.moviebook.movie.service.MovieService;
import hello.moviebook.movie.dto.MovieInfoRes;
import hello.moviebook.movie.dto.MovieRatingReq;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/movie")
@RequiredArgsConstructor
@Tag(name = "Movie", description = "영화 관련 API")
public class UserMovieControllerImpl implements UserMovieController {
    private final MovieService movieService;

    // 유저 선호 장르에 맞는 영화 리스트 조회 API
    @GetMapping("/recommend")
    public ResponseEntity<List<MovieInfoRes>> userPreferredGenreMovieList(Authentication principal) {
        if (principal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        List<MovieInfoRes> movieInfoResList = movieService.getPreferredMovieList(principal.getName());
        if (movieInfoResList == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        return ResponseEntity.status(HttpStatus.OK).body(movieInfoResList);
    }

    // 유저 시청한 영화 저장 API
    @PostMapping("/watch")
    public ResponseEntity<String> userWatchingMovieList(Authentication principal, @RequestBody List<MovieRatingReq> movieList) {
        if (principal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        String result = movieService.saveUserMovieList(movieList, principal.getName());
        if (result == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 유저가 관람한 영화 평점 수정 API
    @PatchMapping("/rating")
    public ResponseEntity<String> userMovieRatingList(Authentication principal, @RequestBody List<MovieRatingReq> movieList) {
        if (principal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        if (movieList.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        movieService.updateMovieRating(principal.getName(), movieList);

        return ResponseEntity.status(HttpStatus.OK).body("영화 평점을 수정했습니다.");
    }
}
