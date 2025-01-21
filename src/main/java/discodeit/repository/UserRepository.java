package discodeit.repository;

import discodeit.enity.User;

import java.util.UUID;

public interface UserRepository {
    void save(User user);
    void delete(UUID userId);

    User findById(UUID userId);

    boolean checkEmailDuplicate(String email);
}
