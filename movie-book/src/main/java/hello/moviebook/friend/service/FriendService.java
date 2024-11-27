package hello.moviebook.friend.service;

import hello.moviebook.book.domain.UserBook;
import hello.moviebook.book.repository.UserBookRepository;
import hello.moviebook.friend.domain.Friend;
import hello.moviebook.friend.dto.FriendBookListRes;
import hello.moviebook.friend.dto.FriendListRes;
import hello.moviebook.friend.repository.FriendRepository;
import hello.moviebook.user.domain.User;
import hello.moviebook.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final UserRepository userRepository;
    private final UserBookRepository userBookRepository;
    private final FriendRepository friendRepository;

    public List<FriendListRes> getFriends(String userId) {
        User user = userRepository.findUserById(userId);

        return friendRepository.findAllByUser1OrUser2(user, user).stream()
                .map(friend -> FriendListRes.builder()
                        .userId(userId)
                        .friend(friend)
                        .build())
                .toList();
    }

    public boolean addFriend(String userId, String friendId) {
        User user = userRepository.findUserById(userId);
        User friend = userRepository.findUserById(friendId);

        if (user == null || friend == null)
            return false;

        Friend newFriend = new Friend(user, friend);
        friendRepository.save(newFriend);

        return true;
    }

    public boolean deleteFriend(String userId, String friendId) {
        User user = userRepository.findUserById(userId);
        User friend = userRepository.findUserById(friendId);

        if (user == null || friend == null)
            return false;

        if (userId.compareTo(friendId) < 0) {
            friendRepository.delete(friendRepository.findByUser1AndUser2(user, friend));
        } else {
            friendRepository.delete(friendRepository.findByUser1AndUser2(friend, user));
        }

        return true;
    }

    public FriendBookListRes getFriendBookList(User friend) {
        List<UserBook> userBookList = userBookRepository.findAllByUser(friend);

        return FriendBookListRes.builder()
                    .user(friend)
                    .userBookList(userBookList)
                    .build();
    }
}
