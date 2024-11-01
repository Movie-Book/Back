package hello.moviebook.user.controller;

import hello.moviebook.user.domain.User;
import hello.moviebook.user.service.UserService;
import hello.moviebook.jwt.TokenDTO;
import hello.moviebook.user.dto.FindPwReq;
import hello.moviebook.user.dto.UserJoinReq;
import hello.moviebook.user.dto.UserLoginReq;
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
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "유저 관련 API")
public class UserControllerImpl implements UserController{

    private final UserService userService;

    // 회원가입 API
    @PostMapping("/join")
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
    public ResponseEntity<String> userLogout(Authentication principal, HttpServletRequest request) {

        if (principal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        // 로그아웃 처리
        userService.logout(request, principal.getName());

        return ResponseEntity.status(HttpStatus.OK).body("로그아웃 처리되었습니다.");
    }

    // 아이디 찾기 API
    @GetMapping("/find-id")
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

    // 로그인 API 테스트
    @GetMapping("/test")
    public ResponseEntity<String> test(Authentication principal) {

        if (principal == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error");
        return ResponseEntity.status(HttpStatus.OK).body("test");
    }
}
