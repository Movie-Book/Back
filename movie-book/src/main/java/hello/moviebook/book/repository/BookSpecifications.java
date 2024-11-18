package hello.moviebook.book.repository;

import hello.moviebook.book.domain.Book;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class BookSpecifications {
    // 줄거리 키워드가 모두 포함된 도서를 필터링하는 Specification
    public static Specification<Book> hasKeywords(List<String> keywords) {
        return (root, query, criteriaBuilder) -> {

            // 줄거리 키워드가 모두 포함된 도서를 찾기 위해 AND 조건 생성
            return criteriaBuilder.and(
                    keywords.stream()
                            .map(keyword -> criteriaBuilder.like(root.get("description"), "%" + keyword + "%"))
                            .toArray(jakarta.persistence.criteria.Predicate[]::new)
            );
        };
    }
}
