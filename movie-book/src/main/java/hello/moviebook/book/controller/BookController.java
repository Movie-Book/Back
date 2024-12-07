package hello.moviebook.book.controller;

import hello.moviebook.book.dto.BookDescriptRes;
import hello.moviebook.book.dto.RecommendBookRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Book", description = "도서 관련 API")
public interface BookController {

    public ResponseEntity<List<BookDescriptRes>> getSimilarBookList(@RequestParam("keywords") List<String> keywords);


    public ResponseEntity<List<BookDescriptRes>> getKeywordBookList(Authentication authentication);

    @Operation(summary = "사용자 맞춤 추천 도서 리스트", description = "사용자 맞춤 추천 도서 리스트 제공 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "추천 도서 리스트입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "잘못된 로그인입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "도서 리스트 추천에 실패했습니다.", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<List<RecommendBookRes>> getRecommendBookList(Authentication authentication);
}
