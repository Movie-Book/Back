package hello.moviebook.movie.domain;

import hello.moviebook.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_movie")
@Getter @Setter
@NoArgsConstructor
public class UserMovie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "number")
    private Long number;

    @Column(name = "rating")
    private Double rating;

    @ManyToOne
    @JoinColumn(name = "user_number")
    private User user;

    @ManyToOne
    @JoinColumn(name = "movie_number")
    private Movie movie;

    public UserMovie(User user, Movie movie, Double rating) {
        this.user = user;
        this.movie = movie;
        this.rating = rating;
    }
}
