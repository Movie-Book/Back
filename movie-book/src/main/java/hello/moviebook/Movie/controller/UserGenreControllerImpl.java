package hello.moviebook.movie.controller;

import hello.moviebook.movie.dto.UserGenreReq;
import hello.moviebook.movie.service.MovieService;
import hello.moviebook.movie.domain.UserGenre;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/genre")
@RequiredArgsConstructor
public class UserGenreControllerImpl implements UserGenreController {
    private final MovieService movieService;

    @PatchMapping("/prefer")
    public ResponseEntity<String> userPreferredGenre(Authentication principal, @RequestBody UserGenreReq userGenreReq) {
        if (principal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("권한이 없는 접근입니다.");

        UserGenre userGenre = movieService.updateUserPreferredGenre(principal.getName(), userGenreReq);
        if (userGenre == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("존재하지 않는 유저입니다.");

        return ResponseEntity.status(HttpStatus.OK).body("유저 취향 장르 정보를 저장했습니다.");
    }

    @PatchMapping("/dislike")
    public ResponseEntity<String> userDislikedGenre(Authentication principal, @RequestBody UserGenreReq userGenreReq) {
        if (principal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("권한이 없는 접근입니다.");

        UserGenre userGenre = movieService.updateUserDislikedGenre(principal.getName(), userGenreReq);
        if (userGenre == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("존재하지 않는 유저입니다.");

        return ResponseEntity.status(HttpStatus.OK).body("유저 취향 장르 정보를 저장했습니다.");
    }
}
