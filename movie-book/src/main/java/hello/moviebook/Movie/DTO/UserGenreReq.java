package hello.moviebook.movie.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.List;

@Getter
@Schema(description = """
        1:액션
        2:드라마
        3:코미디
        4:로맨스
        5:스릴러
        6:공포
        7:SF
        8:판타지
        9:애니메이션
        10:다큐멘터리
        11:범죄""")
public class UserGenreReq {
    List<Long> genres;
}