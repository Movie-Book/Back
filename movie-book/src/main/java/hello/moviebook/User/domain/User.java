package hello.moviebook.user.domain;

import hello.moviebook.book.domain.UserBook;
import hello.moviebook.movie.domain.UserGenre;
import hello.moviebook.movie.domain.UserMovie;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static hello.moviebook.user.domain.Auth.USER;

import java.util.List;

@Entity
@Table(name = "user")
@Getter @Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_number")
    private Long userNumber;

    @Column(name = "id")
    private String id;

    @Column(name = "password")
    private String password;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "email")
    private String email;

    @Column(name = "auth")
    private Auth auth;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserMovie> userMovieList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserBook> userBookList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserGenre> userGenreList;

    @Builder
    public User(String id, String password, String userName, String email) {
        this.id = id;
        this.password = password;
        this.userName = userName;
        this.email = email;
        this.auth = USER;
    }
}