package hello.moviebook.User;

import lombok.Data;

@Data
public class UserJoinReq {
    private String id;
    private String password;
    private String userName;
    private String email;
}
