package hello.moviebook.movie.domain;

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
