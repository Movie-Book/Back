package hello.moviebook.Movie;

import jakarta.persistence.*;

@Entity
@Table(name = "genre")
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_number")
    private Long genreNumber;

    @Column(name = "genre_name")
    private String genreName;
}
