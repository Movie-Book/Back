package hello.moviebook.friend.dto;

import hello.moviebook.book.dto.BookDTO;

import java.util.List;

public class FriendBookListRes {
    private String id;
    private String name;

    List<BookDTO> bookList;
}
