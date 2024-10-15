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
    private Boolean action;

    @Column(name = "drama", nullable = false)
    private Boolean drama;

    @Column(name = "comedy", nullable = false)
    private Boolean comedy;

    @Column(name = "romance", nullable = false)
    private Boolean romance;

    @Column(name = "thriller", nullable = false)
    private Boolean thriller;

    @Column(name = "horror", nullable = false)
    private Boolean horror;

    @Column(name = "sf", nullable = false)
    private Boolean sf;

    @Column(name = "fantasy", nullable = false)
    private Boolean fantasy;

    @Column(name = "animation", nullable = false)
    private Boolean animation;

    @Column(name = "documentary", nullable = false)
    private Boolean documentary;

    @Column(name = "crime", nullable = false)
    private Boolean crime;

    @ManyToOne
    @JoinColumn(name = "user_number")
    private User user;
}
