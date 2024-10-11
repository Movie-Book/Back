package hello.moviebook.User.DTO;

import lombok.Data;

@Data
public class UserLoginReq {
    private String id;
    private String password;
}
