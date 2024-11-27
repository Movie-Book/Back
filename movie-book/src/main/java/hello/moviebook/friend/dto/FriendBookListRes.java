package hello.moviebook.friend.dto;

import hello.moviebook.book.domain.UserBook;
import hello.moviebook.book.dto.BookDTO;
import hello.moviebook.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "친구가 읽은 책 리스트 응답 DTO")
public class FriendBookListRes {
    @Schema(description = "친구의 사용자 ID", example = "ksj01128")
    private String id;

    @Schema(description = "친구가 읽은 책 리스트")
    private List<BookDTO> bookList;

    @Builder
    public FriendBookListRes(User user, List<UserBook> userBookList) {
        this.id = user.getId();
        this.bookList = userBookList.stream()
                .map(BookDTO::new)
                .toList();
    }
}
