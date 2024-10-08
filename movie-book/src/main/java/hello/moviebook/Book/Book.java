package hello.moviebook.Book;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import hello.moviebook.UserBook.UserBook;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "book")
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter @Setter
public class Book {
    @Id
    @Column(name = "isbn")
    @JsonProperty("isbn")
    private String isbn;

    @Column(name = "book_name")
    @JsonProperty("title")
    private String bookName;

    @Column(name = "publisher")
    @JsonProperty("publisher")
    private String publisher;

    @Column(name = "author")
    @JsonProperty("author")
    private String author;

    @Column(name = "description")
    @JsonProperty("description")
    private String description;

    @Column(name = "image")
    @JsonProperty("image")
    private String image;

    @Column(name = "pub_date")
    @JsonProperty("pubdate")
    private String pubDate;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<UserBook> userBookList;

}
