package hello.moviebook.Movie;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import hello.moviebook.UserMovie.UserMovie;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "movie")
@Setter @Getter
@JsonIgnoreProperties(ignoreUnknown = true)  // 정의되지 않은 필드 무시
public class Movie {
    @Id
    @Column(name = "movie_id")
    private Long movieId;

    @Column(name = "movie_name")
    private String movieName;

    @Column(name = "genre")
    private String genre;

    @Column(name = "keyword")
    private String keyword;

    @Column(name = "description")
    private String description;

    @Column(name = "poster")
    private String poster;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<UserMovie> userMovieList;

}
