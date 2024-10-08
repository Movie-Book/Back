package hello.moviebook.User;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public User createUser(UserJoinReq joinReq) {

        // 유저 아이디 중복
        if (userRepository.existsById(joinReq.getId()))
            return (null);

        // 유저 이메일 중복
        if (userRepository.existsByEmail(joinReq.getEmail()))
            return (null);

        // 유저 정보 생성
        User newUser = User.builder()
                .id(joinReq.getId())
                .password(passwordEncoder.encode(joinReq.getPassword()))
                .userName(joinReq.getUserName())
                .email(joinReq.getEmail())
                .build();

        userRepository.save(newUser);

        return (newUser);
    }
}
