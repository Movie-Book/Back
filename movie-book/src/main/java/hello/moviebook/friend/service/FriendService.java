package hello.moviebook.friend.service;

import hello.moviebook.friend.domain.Friend;
import hello.moviebook.friend.repository.FriendRepository;
import hello.moviebook.user.domain.User;
import hello.moviebook.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    public boolean addFriend(String userId, String friendId) {
        User user = userRepository.findUserById(userId);
        User friend = userRepository.findUserById(friendId);

        if (user == null || friend == null)
            return false;

        Friend newFriend = new Friend(user, friend);
        friendRepository.save(newFriend);

        return true;
    }

}
