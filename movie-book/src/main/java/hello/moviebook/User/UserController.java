package hello.moviebook.User;

import hello.moviebook.Jwt.TokenDTO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원가입 API
    @PostMapping("/join")
    public ResponseEntity<String> userJoin(@RequestBody UserJoinReq joinReq) {

        User saveUser = userService.createUser(joinReq);

        if (saveUser == null)
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 유저 정보입니다.");

        return ResponseEntity.status(HttpStatus.OK).body(saveUser.getUserName() + "님 회원가입에 성공했습니다.");
    }

    // 로그인 API
    @GetMapping("/login")
    public ResponseEntity<TokenDTO> userLogin(@RequestBody UserLoginReq loginReq, HttpServletResponse response) {

        // 잘못된 로그인 요청
        if (loginReq.getId() == null || loginReq.getPassword() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        // 로그인 토큰 정보 받아오기
        TokenDTO loginToken = userService.login(loginReq, response);

        if (loginToken == null)
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);

        return ResponseEntity.status(HttpStatus.OK).body(loginToken);
    }
}
