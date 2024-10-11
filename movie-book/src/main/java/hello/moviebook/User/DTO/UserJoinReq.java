package hello.moviebook.User.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserJoinReq {
    @NotBlank(message = "아이디를 입력해주세요.")
    @Size(min = 3, max = 10, message = "아이디를 3~10자리로 입력해주세요.")
    @Pattern(regexp="^(?=.*[a-z])[a-zA-Z0-9]*$", message = "아이디는 영문 소문자를 포함해야 합니다.")
    private String id;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, max = 16, message = "비밀번호를 8~16자리로 입력해주세요.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]*$",
            message = "비밀번호는 영문 소문자, 숫자, 특수문자를 모두 포함해야 합니다.")
    private String password;

    @NotBlank(message = "이름을 입력해주세요.")
    private String userName;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email
    private String email;
}
