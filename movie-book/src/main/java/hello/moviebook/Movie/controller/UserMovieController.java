package hello.moviebook.movie.controller;

import hello.moviebook.movie.dto.MovieInfoRes;
import hello.moviebook.movie.dto.MovieRatingReq;
import hello.moviebook.movie.dto.UserMovieRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface UserMovieController {
    @Operation(summary = "유저 선호 장르에 맞는 영화 리스트 조회", description = "유저 선호 장르에 속하고, 비선호 장르는 제외한 영화들을 화면에 표시하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 선호 장르 영화 리스트.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "유효성 검사 오류", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 유저입니다.", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<List<MovieInfoRes>> userPreferredGenreMovieList(Authentication principal);

    @Operation(summary = "유저 선호 영화 저장", description = "유저가 시청한 영화 및 별점 정보를 저장하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 선호 영화 정보가 저장되었습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "유효성 검사 오류", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "잘못된 접근입니다.", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<String> userWatchingMovieList(Authentication principal, @RequestBody List<MovieRatingReq> movieList);

    @Operation(summary = "유저가 관람한 영화 평점 수정", description = "유저가 관람한 영화 평점 수정하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 선호 영화 정보가 저장되었습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "유효성 검사 오류", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "수정할 영화 정보가 없습니다.", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<String> userMovieRatingList(Authentication principal, @RequestBody List<MovieRatingReq> movieList);

    @Operation(summary = "유저가 관람한 영화 리스트 조회", description = "유저가 관람한 영화 리스트 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저가 관람한 영화 리스트를 조회했습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "유효성 검사 오류", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "조회할 영화 정보가 없습니다.", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<List<UserMovieRes>> userMovieList(Authentication principal);

    @Operation(summary = "영화 검색", description = "영화 검색 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "영화 검색 결과입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "유효성 검사 오류", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "잘못된 검색입니다.", content = @Content(mediaType = "application/json")),
    })
    @Parameter(name = "keyword", description = "검색어", example = "인터스텔라")
    public ResponseEntity<List<UserMovieRes>> searchMovie(Authentication principal, @RequestParam("keyword") String keyword);
}
