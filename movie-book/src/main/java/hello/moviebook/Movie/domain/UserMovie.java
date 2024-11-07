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
    @Column(name = "id")
    private Long id;

    @Column(name = "rating")
    private Double rating;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    public UserMovie(User user, Movie movie, Double rating) {
        this.user = user;
        this.movie = movie;
        this.rating = rating;
    }
}
