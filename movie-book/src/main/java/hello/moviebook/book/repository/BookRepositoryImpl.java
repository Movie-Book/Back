package hello.moviebook.book.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.moviebook.book.domain.Book;
import hello.moviebook.book.domain.QBook;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public List<Book> findBooksByKeywords(List<String> keywords) {
        QBook b = QBook.book;
        int N = 3; // 최소 포함되어야 하는 키워드 개수

        // BooleanBuilder로 기본 조건을 작성
        BooleanBuilder builder = new BooleanBuilder();
        for (String keyword : keywords) {
            builder.or(b.description.containsIgnoreCase(keyword));
        }

        // 먼저 키워드가 포함된 책을 찾고
        List<Book> candidateBooks = queryFactory.selectFrom(b)
                .where(builder)
                .fetch();

        // N개 이상의 키워드가 포함된 책만 필터링하여 반환
        return candidateBooks.stream()
                .filter(book -> {
                    long count = keywords.stream()
                            .filter(keyword -> book.getDescription() != null && book.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                            .count();
                    return count >= N;
                })
                .toList();
    }
}
