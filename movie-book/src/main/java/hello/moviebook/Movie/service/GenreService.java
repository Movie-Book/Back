package hello.moviebook.movie.service;

import hello.moviebook.movie.domain.UserDislikeGenre;
import hello.moviebook.movie.domain.UserLikeGenre;
import hello.moviebook.movie.repository.GenreRepository;
import hello.moviebook.movie.repository.UserDislikeGenreRepository;
import hello.moviebook.movie.repository.UserLikeGenreRepository;
import hello.moviebook.user.domain.User;
import hello.moviebook.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreService {
    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final UserLikeGenreRepository userLikeGenreRepository;
    private final UserDislikeGenreRepository userDislikeGenreRepository;

    @Transactional
    public boolean saveUserLikeGenre(String id, List<Long> genres) {
        User user = userRepository.findUserById(id);

        // 유저 선호 장르 정보 설정
        for (Long genre : genres) {
            userLikeGenreRepository.save(new UserLikeGenre(genreRepository.findGenreByNumber(genre), user));
        }

        return true;
    }

    @Transactional
    public boolean saveUserDislikeGenre(String id, List<Long> genres) {
        User user = userRepository.findUserById(id);


        // 유저 선호 장르 정보 설정
        for (Long genre : genres) {
            userDislikeGenreRepository.save(new UserDislikeGenre(genreRepository.findGenreByNumber(genre), user));
        }

        return true;
    }

    public List<Long> getUserLikeGenre(String id) {
        return userLikeGenreRepository.findAllByUser(userRepository.findUserById(id)).stream()
                .map(userLikeGenre -> userLikeGenre.getGenre().getNumber())
                .toList();
    }

    public List<Long> getUserDislikeGenre(String id) {
        return userDislikeGenreRepository.findAllByUser(userRepository.findUserById(id)).stream()
                .map(userDislikeGenre -> userDislikeGenre.getGenre().getNumber())
                .toList();
    }

    @Transactional
    public boolean updateUserLikeGenre(String id, List<Long> genres) {
        User user = userRepository.findUserById(id);

        // 기존 유저 선호 장르 정보 삭제
        userLikeGenreRepository.deleteAllByUser(user);

        // 유저 선호 장르 정보 설정
        for (Long genre : genres) {
            userLikeGenreRepository.save(new UserLikeGenre(genreRepository.findGenreByNumber(genre), user));
        }

        return true;
    }

    @Transactional
    public boolean updateUserDislikeGenre(String id, List<Long> genres) {
        User user = userRepository.findUserById(id);

        // 기존 유저 비선호 장르 정보 삭제
        userDislikeGenreRepository.deleteAllByUser(user);

        // 유저 선호 장르 정보 설정
        for (Long genre : genres) {
            userDislikeGenreRepository.save(new UserDislikeGenre(genreRepository.findGenreByNumber(genre), user));
        }

        return true;
    }
}
