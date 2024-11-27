package hello.moviebook.friend.controller;

import hello.moviebook.friend.dto.FriendBookListRes;
import hello.moviebook.friend.dto.FriendListRes;
import hello.moviebook.friend.dto.FriendReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Friend", description = "친구 관련 API")
public interface FriendController {
    @Operation(summary = "친구 리스트 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "잘못된 접근 권한입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "200", description = "친구 리스트를 리턴합니다.", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<List<FriendListRes>> getFriendList(Authentication authentication);

    @Operation(summary = "친구 추가 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "잘못된 접근 권한입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 유저입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "200", description = "친구 추가에 성공했습니다.", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<String> postNewFriend(Authentication authentication, @RequestBody FriendReq friendReq);

    @Operation(summary = "친구 삭제 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "잘못된 접근 권한입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 유저입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "200", description = "친구 삭제에 성공했습니다.", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<String> patchFriendList(Authentication authentication, @RequestBody FriendReq friendReq);

    @Operation(summary = "친구 추천 도서 리스트 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "잘못된 접근 권한입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "204", description = "존재하지 않는 유저입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "200", description = "친구 추천 도서 리스트 조회에 성공했습니다.", content = @Content(mediaType = "application/json")),
    })
    @Parameter(name = "id", description = "유저 아이디", example = "ksj01128")
    public ResponseEntity<FriendBookListRes> getFriendBookList(Authentication authentication, @PathVariable("id") String id);
}
