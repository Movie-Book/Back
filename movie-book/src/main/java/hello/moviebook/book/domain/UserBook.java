package hello.moviebook.book.domain;

import hello.moviebook.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_book")
@Getter @Setter
public class UserBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_book_number")
    private Long userBookNumber;

    @Column(name = "book_rating")
    private Double bookRating;

    @Column(name = "book_review")
    private String bookReview;

    @ManyToOne
    @JoinColumn(name = "user_number")
    private User user;

    @ManyToOne
    @JoinColumn(name = "isbn")
    private Book book;
}
