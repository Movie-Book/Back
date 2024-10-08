package hello.moviebook.UserMovie;

import hello.moviebook.Movie.Movie;
import hello.moviebook.User.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_movie")
@Getter @Setter
public class UserMovie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_movie_number")
    private Long userMovieNumber;

    @Column(name = "movie_rating")
    private Double movieRating;

    @ManyToOne
    @JoinColumn(name = "user_number")
    private User user;

    @ManyToOne
    @JoinColumn(name = "movie_number")
    private Movie movie;
}
