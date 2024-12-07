package hello.moviebook.movie.domain;

import jakarta.persistence.*;
import lombok.Getter;
import java.util.List;

@Entity
@Table(name = "genre")
@Getter
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "number")
    private Long number;
    @Column(name = "name")
    private String name;
    @Column(name = "name_en")
    private String nameEn;

    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL)
    private List<UserDislikeGenre> userDislikeGenreList;

    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL)
    private List<UserLikeGenre> userLikeGenreList;
}
