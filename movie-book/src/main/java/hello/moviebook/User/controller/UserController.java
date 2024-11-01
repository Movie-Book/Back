package hello.moviebook.user.controller;

import hello.moviebook.jwt.TokenDTO;
import hello.moviebook.user.dto.FindPwReq;
import hello.moviebook.user.dto.UserJoinReq;
import hello.moviebook.user.dto.UserLoginReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserController {
    @Operation(summary = "회원가입", description = "유저가 회원가입 할 때 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OO님 회원가입에 성공하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "유효성 검사 오류.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 유저 정보입니다.", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<String> userJoin(@Valid @RequestBody UserJoinReq joinReq, BindingResult bindingResult);

    @Operation(summary = "로그인", description = "유저가 로그인 할 때 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "올바르지 않은 로그인.", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<TokenDTO> userLogin(@RequestBody UserLoginReq loginReq, HttpServletResponse response);

    @Operation(summary = "로그아웃", description = "유저가 로그아웃 할 때 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 처리되었습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "유효성 검사 오류 또는 권한 오류.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<String> userLogout(Authentication principal, HttpServletRequest request);

    @Operation(summary = "아이디 찾기", description = "유저가 아이디를 찾기 위해 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OO님의 아이디는 OOO입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "존재하지 않는 유저입니다.", content = @Content(mediaType = "application/json")),
    })

    @Parameters({
            @Parameter(name = "userName", description = "유저명", example = "김서진"),
            @Parameter(name = "email", description = "이메일", example = "ksj01128@naver.com")
    })
    public ResponseEntity<String> findUserId(@RequestParam("userName") String userName, @RequestParam("email") String email);

    @Operation(summary = "비밀번호 재설정", description = "유저가 비밀번호를 재설정할 때 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호를 재설정했습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "유효성 검사 오류", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "올바르지 않은 정보입니다.", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<String> resetUserPw(@Valid @RequestBody FindPwReq findPwReq, BindingResult bindingResult);

    @Operation(summary = "Test", description = "로그인 테스트용(연동XX)")
    public ResponseEntity<String> test(Authentication principal);
}
