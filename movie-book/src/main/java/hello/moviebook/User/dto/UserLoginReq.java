package hello.moviebook.user.dto;

import lombok.Data;

@Data
public class UserLoginReq {
    private String id;
    private String password;
}
