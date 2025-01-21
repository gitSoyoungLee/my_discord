package discodeit.repository;

import discodeit.enity.User;

import java.util.Map;
import java.util.UUID;

public interface UserRepository {
    void save(User user);
    void delete(UUID userId);

    User findById(UUID userId);

    Map<UUID, User> findAll();
    boolean checkEmailDuplicate(String email);
}
