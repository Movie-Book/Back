package hello.moviebook.friend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "친구 ID DTO")
public class FriendReq {
    @Schema(description = "요청하는 사용자의 ID", example = "ksj01128")
    private String id;
}
