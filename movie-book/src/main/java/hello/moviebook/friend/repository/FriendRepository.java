package hello.moviebook.friend.repository;

import hello.moviebook.friend.domain.Friend;
import hello.moviebook.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    Friend findByUser1AndUser2(User user1, User user2);
    List<Friend> findAllByUser1OrUser2(User user1, User user2);
}
