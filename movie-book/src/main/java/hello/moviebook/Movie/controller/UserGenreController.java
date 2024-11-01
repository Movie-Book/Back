package hello.moviebook.movie.controller;

import hello.moviebook.user.dto.UserGenreReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserGenreController {
    @Operation(summary = "유저 취향 - 영화 장르", description = "유저 영화 장르 선호도 조사 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 취향 장르 정보를 저장했습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "유효성 검사 오류", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 유저입니다.", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<String> userPreferredGenre(Authentication principal, @RequestBody UserGenreReq userGenreReq);

    @Operation(summary = "유저 취향 - 영화 장르", description = "유저 영화 장르 비선호도 조사 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 취향 장르 정보를 저장했습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "유효성 검사 오류", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 유저입니다.", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<String> userDislikedGenre(Authentication principal, @RequestBody UserGenreReq userGenreReq);
}
