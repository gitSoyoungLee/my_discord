package discodeit.repository.file;

import discodeit.enity.Message;
import discodeit.repository.MessageRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class FileMessageRepository extends FileRepository implements MessageRepository {
    public FileMessageRepository() {
        super(Paths.get(System.getProperty("user.dir"), "file-data-map", Message.class.getSimpleName()));
    }

    @Override
    public void save(Message message) {
        Path path = resolvePath(message.getId());
        saveToFile(path, message);
    }

    @Override
    public void delete(UUID messageId) {
        Path path = resolvePath(messageId);
        deleteFile(path);
    }

    @Override
    public Optional<Message> findById(UUID messageId) {
        Path path = resolvePath(messageId);
        return loadFromFile(path);
    }

    @Override
    public Map<UUID, Message> findAll() {
        Map<UUID, Message> messages = new HashMap<>();
        // 폴더 내 모든 .ser 파일을 찾음
        try (Stream<Path> paths = Files.walk(this.getDIRECTORY())) {
            paths.filter(path -> path.toString().endsWith(".ser"))  // .ser 파일만 필터링
                    .forEach(path -> {
                        Optional<Message> messageOptional = loadFromFile(path);
                        messageOptional.ifPresent(message -> messages.put(message.getId(), message));
                    });
        } catch (IOException e) {
            System.out.println("파일을 읽는 중 오류가 발생했습니다: " + e.getMessage());
        }

        return messages;
    }
}
