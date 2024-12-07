package hello.moviebook.book.controller;

import hello.moviebook.book.dto.BookDescriptRes;
import hello.moviebook.book.dto.RecommendBookRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface BookController {
    @Operation(summary = "줄거리 키워드 기반 도서 검색", description = "줄거리 키워드 기반 도서 검색 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "줄거리 키워드 기반으로 도서 정보를 검색했습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 유저입니다.", content = @Content(mediaType = "application/json")),
    })
    @Parameter(name = "keywords", description = "줄거리 키워드", example = "[우주, 임무]")
    public ResponseEntity<List<BookDescriptRes>> getSimilarBookList(@RequestParam("keywords") List<String> keywords);

    @Operation(summary = "선호 영화 리스트 최빈 키워드 기반 도서 검색", description = "선호 영화 리스트 최빈 키워드 키워드 기반 도서 검색 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "선호 영화 리스트 최빈 키워드 기반으로 도서 정보를 검색했습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 유저입니다.", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<List<BookDescriptRes>> getKeywordBookList(Authentication authentication);

    @Operation(summary = "사용자 맞춤 추천 도서 리스트", description = "사용자 맞춤 추천 도서 리스트 제공 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "추천 도서 리스트입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "잘못된 로그인입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "도서 리스트 추천에 실패했습니다.", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<List<RecommendBookRes>> getRecommendBookList(Authentication authentication);
}
