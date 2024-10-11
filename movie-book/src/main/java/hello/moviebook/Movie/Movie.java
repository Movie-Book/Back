package hello.moviebook.Movie;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("id")
    private Long movieId;

    @Column(name = "movie_name")
    @JsonProperty("original_title")
    private String movieName;

    @Column(name = "genre")
    private String genre;

    @Column(name = "keyword")
    private String keyword;

    @Column(name = "keyword_kor")
    private String keywordKor;

    @Column(name = "description")
    @JsonProperty("overview")
    private String description;

    @Column(name = "poster")
    private String poster;

    @Column(name = "movie_name_en")
    private String movieNameEn;

    @Column(name = "genre_en")
    private String genreEn;

    @Column(name = "description_en")
    private String descriptionEn;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<UserMovie> userMovieList;

}
