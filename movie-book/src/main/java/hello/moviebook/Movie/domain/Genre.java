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
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL)
    private List<UserDislikeGenre> userDislikeGenreList;

    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL)
    private List<UserLikeGenre> userLikeGenreList;
}
