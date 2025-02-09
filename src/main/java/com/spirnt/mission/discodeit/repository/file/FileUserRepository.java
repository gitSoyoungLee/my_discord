package com.spirnt.mission.discodeit.repository.file;

import com.spirnt.mission.discodeit.enity.User;
import com.spirnt.mission.discodeit.repository.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class FileUserRepository extends FileRepository implements UserRepository {


    public FileUserRepository(String fileDirectory) {
        super(fileDirectory + "/User");
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
