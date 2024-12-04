package hello.moviebook.friend.controller;

import hello.moviebook.friend.dto.FriendBookListRes;
import hello.moviebook.friend.dto.FriendListRes;
import hello.moviebook.friend.dto.FriendReq;
import hello.moviebook.friend.service.FriendService;
import hello.moviebook.user.domain.User;
import hello.moviebook.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/friend")
@RequiredArgsConstructor
public class FriendControllerImpl implements FriendController{
    private final FriendService friendService;
    private final UserRepository userRepository;

    @GetMapping("")
    public ResponseEntity<List<FriendListRes>> getFriendList(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        List<FriendListRes> friendList = friendService.getFriends(authentication.getName());
        return ResponseEntity.status(HttpStatus.OK).body(friendList);
    }

    @PostMapping("/add")
    public ResponseEntity<String> postNewFriend(Authentication authentication, @RequestBody FriendReq friendReq) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        User friend = userRepository.findUserById(friendReq.getId());
        if (friend == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("존재하지 않는 유저입니다.");

        if (!friendService.addFriend(userRepository.findUserById(authentication.getName()), friend))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 친구 관계를 맺은 상태입니다.");
        return ResponseEntity.status(HttpStatus.OK).body(friendReq.getId() + "님을 친구 목록에 추가했습니다.");
    }

    @PatchMapping("/delete")
    public ResponseEntity<String> patchFriendList(Authentication authentication, @RequestBody FriendReq friendReq) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        User friend = userRepository.findUserById(friendReq.getId());
        if (friend == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("존재하지 않는 유저입니다.");


        if (!friendService.deleteFriend(userRepository.findUserById(authentication.getName()), friend))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(friendReq.getId() + "님과 친구 관계가 아닙니다.");
        return ResponseEntity.status(HttpStatus.OK).body(friendReq.getId() + "님을 친구 목록에서 삭제했습니다.");
    }

    @GetMapping("/{id}/book")
    public ResponseEntity<FriendBookListRes> getFriendBookList(Authentication authentication, @PathVariable("id") String id) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        User friend = userRepository.findUserById(id);
        if (friend == null)
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);

        FriendBookListRes friendBookList = friendService.getFriendBookList(friend);
        return ResponseEntity.status(HttpStatus.OK).body(friendBookList);
    }
}
