package discodeit.repository.file;

import discodeit.enity.Message;
import discodeit.enity.User;
import discodeit.repository.UserRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class FileUserRepository extends FileRepository implements UserRepository {
    public FileUserRepository() {
        // Java 프로그램이 실행되는 현재 작업 디렉토리 내 file-data-map/User 폴더로 지정
        super(Paths.get(System.getProperty("user.dir"), "file-data-map", User.class.getSimpleName()));
    }

    @Override
    public void save(User user) {
        Path path = resolvePath(user.getId());
        saveToFile(path, user);
    }

    @Override
    public void delete(UUID userId) {
        Path path = resolvePath(userId);
        deleteFile(path);
    }

    @Override
    public Optional<User> findById(UUID userId) {
        Path path = resolvePath(userId);
        return loadFromFile(path);
    }

    @Override
    public Map<UUID, User> findAll() {
        Map<UUID, User> users = new HashMap<>();
        // 폴더 내 모든 .ser 파일을 찾음
        try (Stream<Path> paths = Files.walk(this.getDIRECTORY())) {
            paths.filter(path -> path.toString().endsWith(".ser"))  // .ser 파일만 필터링
                    .forEach(path -> {
                        Optional<User> userOptional = loadFromFile(path);
                        userOptional.ifPresent(user -> users.put(user.getId(), user));
                    });
        } catch (IOException e) {
            System.out.println("파일을 읽는 중 오류가 발생했습니다: " + e.getMessage());
        }

        return users;
    }
}
