package hello.moviebook.movie.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @Column(columnDefinition = "TEXT")
//    @JsonProperty("overview")
    private String description;

    @Column(name = "poster")
    private String poster;

    @Column(name = "movie_name_en")
    private String movieNameEn;

    @Column(name = "genre_en")
    private String genreEn;

    @Column(columnDefinition = "TEXT", name = "description_en")
    @JsonProperty("overview")
    private String descriptionEn;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<UserMovie> userMovieList;

}
