package hello.moviebook.UserGenre;

import hello.moviebook.User.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_genre")
@Setter @Getter
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
}
