package hello.moviebook.friend.dto;

import hello.moviebook.book.domain.UserBook;
import hello.moviebook.book.dto.BookDTO;
import hello.moviebook.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class FriendBookListRes {
    private String id;

    private List<BookDTO> bookList;

    @Builder
    public FriendBookListRes(User user, List<UserBook> userBookList) {
        this.id = user.getId();
        this.bookList = userBookList.stream()
                .map(BookDTO::new)
                .toList();
    }
}
