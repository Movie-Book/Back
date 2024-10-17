package hello.moviebook.User;

import hello.moviebook.Jwt.TokenDTO;
import hello.moviebook.User.DTO.FindPwReq;
import hello.moviebook.User.DTO.UserGenreReq;
import hello.moviebook.User.DTO.UserJoinReq;
import hello.moviebook.User.DTO.UserLoginReq;
import hello.moviebook.UserGenre.UserGenre;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "유저 관련 API")
public class UserController {

    private final UserService userService;

    // 회원가입 API
    @PostMapping("/join")
    @Operation(summary = "회원가입", description = "유저가 회원가입 할 때 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OO님 회원가입에 성공하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "유효성 검사 오류.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 유저 정보입니다.", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<String> userJoin(@Valid @RequestBody UserJoinReq joinReq, BindingResult bindingResult) {

        // 유효성 검사 오류 존재시 가장 먼저 발생한 오류 메시지 출력
        if (bindingResult.hasErrors())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효성 검사 오류: " + bindingResult.getAllErrors().getFirst().getDefaultMessage() + "\n");

        // 유저 정보 생성
        User saveUser = userService.createUser(joinReq);

        if (saveUser == null)
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 유저 정보입니다.");

        return ResponseEntity.status(HttpStatus.OK).body(saveUser.getUserName() + "님 회원가입에 성공했습니다.");
    }

    // 로그인 API
    @PostMapping("/login")
    @Operation(summary = "로그인", description = "유저가 로그인 할 때 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "올바르지 않은 로그인.", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<TokenDTO> userLogin(@RequestBody UserLoginReq loginReq, HttpServletResponse response) {

        // 잘못된 로그인 요청
        if (loginReq.getId() == null || loginReq.getPassword() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        // 로그인 토큰 정보 받아오기
        TokenDTO loginToken = userService.login(loginReq, response);

        // 존재하지 않는 유저 혹은 비밀번호 불일치
        if (loginToken == null)
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);

        return ResponseEntity.status(HttpStatus.OK).body(loginToken);
    }

    // 로그아웃 API
    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "유저가 로그아웃 할 때 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 처리되었습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "유효성 검사 오류 또는 권한 오류.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<String> userLogout(Authentication principal, HttpServletRequest request) {

        if (principal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        // 로그아웃 처리
        userService.logout(request, principal.getName());

        return ResponseEntity.status(HttpStatus.OK).body("로그아웃 처리되었습니다.");
    }

    // 아이디 찾기 API
    @GetMapping("/find-id")
    @Operation(summary = "아이디 찾기", description = "유저가 아이디를 찾기 위해 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OO님의 아이디는 OOO입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "존재하지 않는 유저입니다.", content = @Content(mediaType = "application/json")),
    })
    @Parameters({
            @Parameter(name = "userName", description = "유저명", example = "김서진"),
            @Parameter(name = "email", description = "이메일", example = "ksj01128@naver.com")
    })
    public ResponseEntity<String> findUserId(@RequestParam("userName") String userName, @RequestParam("email") String email) {

        if (userName == null || email == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("정보를 입력해주세요.");

        String findId = userService.getUserId(userName, email);

        if (findId == null)
            return ResponseEntity.status(HttpStatus.CONFLICT).body("존재하지 않는 유저입니다.");

        return ResponseEntity.status(HttpStatus.OK).body(userName + "님의 아이디는 " + findId + "입니다.");
    }

    // 비밀번호 재설정 API
    @PostMapping("/reset-pw")
    @Operation(summary = "비밀번호 재설정", description = "유저가 비밀번호를 재설정할 때 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호를 재설정했습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "유효성 검사 오류", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "올바르지 않은 정보입니다.", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<String> resetUserPw(@Valid @RequestBody FindPwReq findPwReq, BindingResult bindingResult) {

        // 유효성 검사 오류 존재시 가장 먼저 발생한 오류 메시지 출력
        if (bindingResult.hasErrors())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효성 검사 오류: " + bindingResult.getAllErrors().getFirst().getDefaultMessage() + "\n");

        // 재설정한 비밀번호
        User findUser = userService.resetPassword(findPwReq);

        if (findUser == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("올바르지 않은 정보입니다.");

        return ResponseEntity.status(HttpStatus.OK).body("비밀번호를 재설정했습니다.");
    }

    @PostMapping("/prefer-genre")
    @Operation(summary = "유저 취향 - 영화 장르", description = "유저 영화 장르 선호도 조사 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 취향 장르 정보를 저장했습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "유효성 검사 오류", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 유저입니다.", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<String> userPreferredGenre(Authentication principal, @RequestBody UserGenreReq userGenreReq) {
        if (principal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("권한이 없는 접근입니다.");

        UserGenre userGenre = userService.updateUserPreferredGenre(principal.getName(), userGenreReq);
        if (userGenre == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("존재하지 않는 유저입니다.");

        return ResponseEntity.status(HttpStatus.OK).body("유저 취향 장르 정보를 저장했습니다.");
    }

    @PatchMapping("/dislike-genre")
    @Operation(summary = "유저 취향 - 영화 장르", description = "유저 영화 장르 비선호도 조사 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 취향 장르 정보를 저장했습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "유효성 검사 오류", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 유저입니다.", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<String> userDislikedGenre(Authentication principal, @RequestBody UserGenreReq userGenreReq) {
        if (principal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("권한이 없는 접근입니다.");

        UserGenre userGenre = userService.updateUserDislikedGenre(principal.getName(), userGenreReq);
        if (userGenre == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("존재하지 않는 유저입니다.");

        return ResponseEntity.status(HttpStatus.OK).body("유저 취향 장르 정보를 저장했습니다.");
    }

    // 로그인 API 테스트
    @GetMapping("/test")
    @Operation(summary = "Test", description = "로그인 테스트용(연동XX)")
    public ResponseEntity<String> test(Authentication principal) {

        if (principal == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error");
        return ResponseEntity.status(HttpStatus.OK).body("test");
    }
}
