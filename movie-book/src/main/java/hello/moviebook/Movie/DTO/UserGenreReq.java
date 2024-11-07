package hello.moviebook.movie.dto;

import lombok.*;
import java.util.List;

@Getter
public class UserGenreReq {
    List<Long> genres;
}