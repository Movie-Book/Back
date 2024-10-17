package hello.moviebook.User;

import hello.moviebook.Jwt.JwtService;
import hello.moviebook.Jwt.TokenDTO;
import hello.moviebook.User.DTO.FindPwReq;
import hello.moviebook.User.DTO.UserGenreReq;
import hello.moviebook.User.DTO.UserJoinReq;
import hello.moviebook.User.DTO.UserLoginReq;
import hello.moviebook.UserGenre.UserGenre;
import hello.moviebook.UserGenre.UserGenreRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final UserGenreRepository userGenreRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final StringRedisTemplate redisTemplate;

    private final String PREFIX_LOGOUT = "LOGOUT:";
    private final String PREFIX_LOGOUT_REFRESH = "LOGOUT_REFRESH:";

    @Transactional
    // 회원가입 API - 유저 생성
    public User createUser(UserJoinReq joinReq) {

        // 유저 아이디 중복
        if (userRepository.existsById(joinReq.getId())){
            log.info("회원가입 : 중복된 아이디");
            return (null);
        }

        // 유저 이메일 중복
        if (userRepository.existsByEmail(joinReq.getEmail())) {
            log.info("회원가입 : 중복된 이메일");
            return (null);
        }

        // 유저 정보 생성
        User newUser = User.builder()
                .id(joinReq.getId())
                // 비밀번호 암호화해 저장
                .password(passwordEncoder.encode(joinReq.getPassword()))
                .userName(joinReq.getUserName())
                .email(joinReq.getEmail())
                .build();

        // 유저 정보 저장
        userRepository.save(newUser);

        // 유저-장르 정보 생성
        UserGenre newUserGenre = new UserGenre(newUser);

        // 유저-장르 정보 저장
        userGenreRepository.save(newUserGenre);

        return (newUser);
    }

    @Transactional
    // 응답 헤더에 토큰 정보 세팅
    public TokenDTO setTokenInHeader(User loginUser, HttpServletResponse response) {

        // 토큰 생성 및 리프레쉬 토큰 redis 저장
        TokenDTO loginToken = jwtService.generateToken(loginUser);

        // 액세스 토큰 발급
        log.info("Id = {}, password = {}", loginUser.getId(), loginUser.getPassword());
        log.info("accessToken = {}", loginToken.getAccessToken());

        // Header에 정보 넘겨주기
        response.setHeader("UserId", loginUser.getId());
        response.setHeader("TokenType", "Bearer");
        response.setHeader("AccessToken", loginToken.getAccessToken());

        return (loginToken);
    }

    @Transactional
    // 로그인 API
    public TokenDTO login(UserLoginReq loginReq, HttpServletResponse response) {

        // 로그인 요청 ID - 유저 조회
        User loginUser = userRepository.findUserById(loginReq.getId());

        // 존재하지 않는 유저 정보인 경우
        if (loginUser == null)
            return (null);

        // 비밀번호 정보가 일치하지 않는 경우
        if (!passwordEncoder.matches(loginReq.getPassword(), loginUser.getPassword()))
            return (null);

        // 토큰 정보 받아오기
        return (setTokenInHeader(loginUser, response));
    }

    @Transactional
    public void logout(HttpServletRequest request, String id) {
        // 토큰 정보
        String accessToken = jwtService.resolveAccessToken(request);
        String refreshToken = jwtService.resolveRefreshToken(id);

        log.info("refreshToken : {}", refreshToken);

        // 토큰 만료 기한
        Date accessExpiration = jwtService.parseClaims(accessToken).getExpiration();
        Date refreshExpiration = jwtService.parseClaims(refreshToken).getExpiration();

        // 남은 만료 기한 동안 블랙리스트 처리
        redisTemplate.opsForValue()
                .set(PREFIX_LOGOUT + id, accessToken, Duration.ofSeconds(accessExpiration.getTime() - new Date().getTime()));
        redisTemplate.opsForValue()
                .set(PREFIX_LOGOUT_REFRESH + accessToken, refreshToken, Duration.ofSeconds(refreshExpiration.getTime() - new Date().getTime()));
    }

    public String getUserId(String userName, String email) {
        // 이메일로 유저 검색
         User nameUser = userRepository.findUserByUserName(userName);
         User emailUser = userRepository.findUserByEmail(email);

         // 존재하지 않는 유저인 경우
         if (nameUser == null || emailUser == null)
             return (null);

         // 정보가 일치하지 않는 경우
         if (nameUser != emailUser)
             return (null);

         return (nameUser.getId());
    }

    public User resetPassword(FindPwReq findPwReq) {
        // 아이디로 유저 검색
         User findUser = userRepository.findUserById(findPwReq.getId());

         // 존재하지 않는 유저인 경우
         if (findUser == null)
             return (null);

         // 비밀번호, 비밀번호 확인이 일치하지 않는 경우
         if (!findPwReq.getPassword().equals(findPwReq.getRePassword()))
             return (null);

         // 비밀번호 재설정
         findUser.setPassword(passwordEncoder.encode(findPwReq.getPassword()));
         userRepository.save(findUser);

         return (findUser);
    }

    public UserGenre updateUserPreferredGenre(String userId, UserGenreReq userGenreReq) {
        User user = userRepository.findUserById(userId);
        UserGenre userGenre = userGenreRepository.findUserGenreByUser(user);

        if (user == null)
            return null;

        // 유저 취향 장르 정보 설정
        if (userGenreReq.getAction())
            userGenre.setAction(1L);
        if (userGenreReq.getDrama())
            userGenre.setDrama(1L);
        if (userGenreReq.getComedy())
            userGenre.setComedy(1L);
        if (userGenreReq.getRomance())
            userGenre.setRomance(1L);
        if (userGenreReq.getThriller())
            userGenre.setThriller(1L);
        if (userGenreReq.getHorror())
            userGenre.setHorror(1L);
        if (userGenreReq.getSf())
            userGenre.setSf(1L);
        if (userGenreReq.getFantasy())
            userGenre.setFantasy(1L);
        if (userGenreReq.getAnimation())
            userGenre.setAnimation(1L);
        if (userGenreReq.getDocumentary())
            userGenre.setDocumentary(1L);
        if (userGenreReq.getCrime())
            userGenre.setCrime(1L);

       userGenreRepository.save(userGenre);

       return userGenre;
    }

    public UserGenre updateUserDislikedGenre(String userId, UserGenreReq userGenreReq) {
        User user = userRepository.findUserById(userId);
        UserGenre userGenre = userGenreRepository.findUserGenreByUser(user);

        if (user == null)
            return null;

        // 유저 취향 장르 정보 설정
        if (userGenreReq.getAction())
            userGenre.setAction(-1L);
        if (userGenreReq.getDrama())
            userGenre.setDrama(-1L);
        if (userGenreReq.getComedy())
            userGenre.setComedy(-1L);
        if (userGenreReq.getRomance())
            userGenre.setRomance(-1L);
        if (userGenreReq.getThriller())
            userGenre.setThriller(-1L);
        if (userGenreReq.getHorror())
            userGenre.setHorror(-1L);
        if (userGenreReq.getSf())
            userGenre.setSf(-1L);
        if (userGenreReq.getFantasy())
            userGenre.setFantasy(-1L);
        if (userGenreReq.getAnimation())
            userGenre.setAnimation(-1L);
        if (userGenreReq.getDocumentary())
            userGenre.setDocumentary(-1L);
        if (userGenreReq.getCrime())
            userGenre.setCrime(-1L);

       userGenreRepository.save(userGenre);

       return userGenre;
    }
}