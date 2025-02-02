package discodeit.repository.file;

import discodeit.enity.User;
import discodeit.repository.UserRepository;

import java.io.*;
import java.util.*;

public class FileUserRepository extends FileRepository implements UserRepository {
    public FileUserRepository() {
        this.setFileName("user.ser");
    }

    @Override
    public void save(User user) {
        // ser 파일에 Map으로 저장
        Map<UUID, User> users = loadFromFile();
        // 기존 Map을 읽어와 새로운 객체를 추가한 후 덮어씌움
        users.put(user.getId(), user);
        saveToFile(users);
    }

    @Override
    public void delete(UUID userId) {
        Map<UUID, User> users = loadFromFile();
        // 기존 Map을 읽어와 데이터를 삭제한 후 덮어씌움
        if (!users.containsKey(userId)) {
            throw new NoSuchElementException("User ID: " + userId + " not found");
        }
        // 기존 Map을 읽어와 데이터를 삭제한 후 덮어씌움
        users.remove(userId);
        saveToFile(users);
    }

    @Override
    public User findById(UUID userId) {
        Map<UUID, User> users = loadFromFile();
        return Optional.ofNullable(users.get(userId))
                .orElseThrow(() -> new NoSuchElementException("User ID: " + userId + " not found"));
    }

    public Map<UUID, User> findAll() {
        return loadFromFile();
    }
}
