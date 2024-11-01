package hello.moviebook.movie.domain;

import hello.moviebook.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_genre")
@Setter @Getter
@NoArgsConstructor
public class UserGenre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_genre_number")
    private Long userGenreNumber;

    @Column(name = "action", nullable = false)
    private Long action;

    @Column(name = "drama", nullable = false)
    private Long drama;

    @Column(name = "comedy", nullable = false)
    private Long comedy;

    @Column(name = "romance", nullable = false)
    private Long romance;

    @Column(name = "thriller", nullable = false)
    private Long thriller;

    @Column(name = "horror", nullable = false)
    private Long horror;

    @Column(name = "sf", nullable = false)
    private Long sf;

    @Column(name = "fantasy", nullable = false)
    private Long fantasy;

    @Column(name = "animation", nullable = false)
    private Long animation;

    @Column(name = "documentary", nullable = false)
    private Long documentary;

    @Column(name = "crime", nullable = false)
    private Long crime;

    @ManyToOne
    @JoinColumn(name = "user_number")
    private User user;

    public UserGenre(User user) {
        this.action = 0L;
        this.drama = 0L;
        this.comedy = 0L;
        this.romance = 0L;
        this.thriller = 0L;
        this.horror = 0L;
        this.sf = 0L;
        this.fantasy = 0L;
        this.animation = 0L;
        this.documentary = 0L;
        this.crime = 0L;
        this.user = user;
    }
}
