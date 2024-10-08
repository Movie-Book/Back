package hello.moviebook.User;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
}
