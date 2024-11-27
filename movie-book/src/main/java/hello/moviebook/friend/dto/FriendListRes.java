package hello.moviebook.friend.dto;

import hello.moviebook.friend.domain.Friend;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(description = "친구 리스트 응답 DTO")
public class FriendListRes {
    @Schema(description = "친구의 사용자 ID", example = "ksj01128")
    private String id;

    @Builder
    public FriendListRes(String userId, Friend friend) {
        if (friend.getUser1().getId().equals(userId)) {
            this.id = friend.getUser2().getId();
        }
        else {
            this.id = friend.getUser1().getId();
        }
    }
}
