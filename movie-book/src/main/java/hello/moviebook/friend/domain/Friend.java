package hello.moviebook.friend.domain;

import hello.moviebook.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "friend")
@Getter
@NoArgsConstructor
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long friendNumber;

    @ManyToOne
    @JoinColumn(name = "user1")
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user2")
    private User user2;

    @Builder
    public Friend(User user1, User user2) {
        if (user1.getId().compareTo(user2.getId()) < 0) {
            this.user1 = user1;
            this.user2 = user2;
        }
        else {
            this.user1 = user2;
            this.user2 = user1;
        }
    }
}
