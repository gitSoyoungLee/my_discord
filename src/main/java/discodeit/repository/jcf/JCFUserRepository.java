package discodeit.repository.jcf;

import discodeit.enity.User;
import discodeit.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> data;   // 모든 유저 데이터, key=id

    public JCFUserRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public void save(User user) {
        data.put(user.getId(), user);
    }

    @Override
    public void delete(UUID userId) {
        // 해당 유저가 속한 모든 채널에서 삭제
        User user = findById(userId);
        user.getChannels().stream()
                .forEach(channel -> {
                    channel.getUsers().remove(user);
                });
        data.remove(user.getId());
    }

    @Override
    public User findById(UUID userId) {
        User user = this.data.get(userId);
        return Optional.ofNullable(user)
                .orElseThrow(() -> new NoSuchElementException("User ID: " + userId + " not found"));
    }

    @Override
    public boolean checkEmailDuplicate(String email) {
        Collection <User> users = data.values();
        return users.stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    public Map<UUID, User> findAll() {
        return new HashMap<>(data);
    }
}
