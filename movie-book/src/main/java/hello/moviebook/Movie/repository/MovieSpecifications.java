package hello.moviebook.movie.repository;

import hello.moviebook.movie.domain.Genre;
import hello.moviebook.movie.domain.Movie;
import hello.moviebook.user.domain.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class MovieSpecifications {
    // 유저의 선호 장르에 속하는 영화를 필터링하는 Specification
    public static Specification<Movie> hasPreferredGenres(User user, List<Genre> preferredGenres) {
        return (root, query, criteriaBuilder) -> {
            // 선호 장르 이름 목록 생성
            List<String> genreNames = preferredGenres.stream()
                    .map(Genre::getName)
                    .toList();

            // 선호 장르 중 하나라도 포함된 영화를 찾기 위해 OR 조건 생성
            return criteriaBuilder.or(
                    genreNames.stream()
                            .map(genreName -> criteriaBuilder.like(root.get("genre"), "%" + genreName + "%"))
                            .toArray(jakarta.persistence.criteria.Predicate[]::new)
            );
        };
    }

    // 유저의 비선호 장르에 속하지 않는 영화를 필터링하는 Specification
    public static Specification<Movie> doesNotHaveDislikedGenres(User user, List<Genre> dislikedGenres) {
        return (root, query, criteriaBuilder) -> {
            // 비선호 장르 이름 목록 생성
            List<String> genreNames = dislikedGenres.stream()
                    .map(Genre::getName)
                    .toList();

            // 비선호 장르가 하나도 포함되지 않는 영화를 찾기 위해 AND 조건 생성
            return criteriaBuilder.and(
                    genreNames.stream()
                            .map(genreName -> criteriaBuilder.notLike(root.get("genre"), "%" + genreName + "%"))
                            .toArray(jakarta.persistence.criteria.Predicate[]::new)
            );
        };
    }
}
