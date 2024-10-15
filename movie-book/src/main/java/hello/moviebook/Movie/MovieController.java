package hello.moviebook.Movie;

import hello.moviebook.Movie.DTO.MovieInfoRes;
import hello.moviebook.Movie.DTO.MovieRatingReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movie")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    // 유저 선호 장르 영화 조회 API
    @GetMapping("")
    @Operation(summary = "유저 선호 장르 영화", description = "유저 선호 장르에 속하는 영화들을 화면에 표시하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 선호 장르 영화 리스트.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "유효성 검사 오류", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 유저입니다.", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<List<MovieInfoRes>> userPreferredGenreMovieList(Authentication principal) {
        if (principal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        List<MovieInfoRes> movieInfoResList = movieService.getPreferredMovieList(principal.getName());
        if (movieInfoResList == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        return ResponseEntity.status(HttpStatus.OK).body(movieInfoResList);
    }

    // 유저 영화 선호도 조사 API
    @PostMapping("/watch")
    @Operation(summary = "유저 선호 영화 정보", description = "유저가 시청한 영화 및 별점 정보를 저장하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 선호 영화 정보가 저장되었습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "유효성 검사 오류", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "잘못된 접근입니다.", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<String> userWatchingMovieList(Authentication principal, @RequestBody List<MovieRatingReq> movieList) {
        if (principal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        String result = movieService.saveUserMovieList(movieList, principal.getName());
        if (result == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
