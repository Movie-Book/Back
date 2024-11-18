package hello.moviebook.book.controller;

import hello.moviebook.book.dto.BookDescriptRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface UserBookController {
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
}
