package hello.moviebook.book.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FlaskResDTO {
    @JsonProperty("book_name")
    private String bookName;
}
