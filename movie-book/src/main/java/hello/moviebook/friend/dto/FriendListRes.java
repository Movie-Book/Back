package hello.moviebook.friend.dto;

import hello.moviebook.friend.domain.Friend;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FriendListRes {
    private String id;
    private String name;

    @Builder
    public FriendListRes(String userId, Friend friend) {
        if (friend.getUser1().getId().equals(userId)) {
            this.id = friend.getUser2().getId();
            this.name = friend.getUser2().getId();
        }
        else {
            this.id = friend.getUser1().getId();
            this.name = friend.getUser1().getId();
        }
    }
}
