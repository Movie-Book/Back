package hello.moviebook.movie.controller;

import hello.moviebook.movie.dto.UserGenreReq;
import hello.moviebook.movie.service.GenreService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/genre")
@Tag(name = "Genre", description = "영화 장르 관련 API")
@RequiredArgsConstructor
public class UserGenreControllerImpl implements UserGenreController {
    private final GenreService genreService;

    @PostMapping("/like")
    public ResponseEntity<String> saveLikeGenre(Authentication principal, @RequestBody UserGenreReq userGenreReq) {
        if (principal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("권한이 없는 접근입니다.");

        if (!genreService.saveUserLikeGenre(principal.getName(), userGenreReq.getGenres()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("존재하지 않는 유저입니다.");

        return ResponseEntity.status(HttpStatus.OK).body("유저 선호 장르 정보를 저장했습니다.");
    }

    @PostMapping("/dislike")
    public ResponseEntity<String> saveDislikeGenre(Authentication principal, @RequestBody UserGenreReq userGenreReq) {
        if (principal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("권한이 없는 접근입니다.");

        if (!genreService.saveUserDislikeGenre(principal.getName(), userGenreReq.getGenres()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("존재하지 않는 유저입니다.");

        return ResponseEntity.status(HttpStatus.OK).body("유저 비선호 장르 정보를 저장했습니다.");
    }

    @PatchMapping("/like")
    public ResponseEntity<String> updateLikeGenre(Authentication principal, @RequestBody UserGenreReq userGenreReq) {
        if (principal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("권한이 없는 접근입니다.");

        if (!genreService.updateUserLikeGenre(principal.getName(), userGenreReq.getGenres()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("존재하지 않는 유저입니다.");

        return ResponseEntity.status(HttpStatus.OK).body("유저 선호 장르 정보를 수했습니다.");
    }

    @PatchMapping("/dislike")
    public ResponseEntity<String> updateDislikeGenre(Authentication principal, @RequestBody UserGenreReq userGenreReq) {
        if (principal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("권한이 없는 접근입니다.");

        if (!genreService.updateUserDislikeGenre(principal.getName(), userGenreReq.getGenres()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("존재하지 않는 유저입니다.");

        return ResponseEntity.status(HttpStatus.OK).body("유저 비선호 장르 정보를 수정했습니다.");
    }

    @GetMapping("/like")
    public ResponseEntity<List<Long>> getLikeGenre(Authentication principal) {
        if (principal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        List<Long> userLikeGenre = genreService.getUserLikeGenre(principal.getName());
        if (userLikeGenre == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        return ResponseEntity.status(HttpStatus.OK).body(userLikeGenre);
    }

    @GetMapping("/dislike")
    public ResponseEntity<List<Long>> getDislikeGenre(Authentication principal) {
        if (principal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        List<Long> userDislikeGenre = genreService.getUserDislikeGenre(principal.getName());
        if (userDislikeGenre == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        return ResponseEntity.status(HttpStatus.OK).body(userDislikeGenre);
    }
}
