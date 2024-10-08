package hello.moviebook.User;

import lombok.Data;

@Data
public class FindPwReq {
    private String id;
    private String password;
    private String rePassword;
}
