package hello.moviebook.movie.domain;

import hello.moviebook.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_like_genre")
@Setter @Getter
@NoArgsConstructor
public class UserLikeGenre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public UserLikeGenre(Genre genre, User user) {
        this.genre = genre;
        this.user = user;
    }
}
