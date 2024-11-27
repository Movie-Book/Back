package hello.moviebook.user.domain;

import hello.moviebook.book.domain.UserBook;
import hello.moviebook.friend.domain.Friend;
import hello.moviebook.movie.domain.UserDislikeGenre;
import hello.moviebook.movie.domain.UserLikeGenre;
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
    @Column(name = "number")
    private Long number;

    @Column(name = "id")
    private String id;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "auth")
    private Auth auth;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserMovie> userMovieList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserBook> userBookList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserDislikeGenre> userDislikeGenreList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserLikeGenre> userLikeGenreList;

    @OneToMany(mappedBy = "user1", cascade = CascadeType.ALL)
    private List<Friend> friendUserOne;

    @OneToMany(mappedBy = "user2", cascade = CascadeType.ALL)
    private List<Friend> friendUserTwo;

    @Builder
    public User(String id, String password, String name, String email) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.email = email;
        this.auth = USER;
    }
}
