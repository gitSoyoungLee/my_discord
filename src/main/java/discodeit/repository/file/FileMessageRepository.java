package discodeit.repository.file;

import discodeit.enity.Message;
import discodeit.repository.MessageRepository;

import java.io.*;
import java.util.*;

public class FileMessageRepository extends FileRepository implements MessageRepository {
    public FileMessageRepository() {
        this.setFileName("message.ser");
    }

    @Override
    public void save(Message message) {
        // ser 파일에 Map으로 저장
        Map<UUID, Message> messages = loadFromFile();
        messages.put(message.getId(), message);
        saveToFile(messages);
    }

    @Override
    public void delete(UUID messageId) {
        Map<UUID, Message> messages = loadFromFile();
        if (!messages.containsKey(messageId)) {
            throw new NoSuchElementException("Message ID: " + messageId + " not found");
        }
        // 기존 Map을 읽어와 데이터를 삭제한 후 덮어씌움
        messages.remove(messageId);
        saveToFile(messages);
    }

    @Override
    public Message findById(UUID messageId) {
        Map<UUID, Message> messages = loadFromFile();
        return Optional.ofNullable(messages.get(messageId))
                .orElseThrow(() -> new NoSuchElementException("Message ID: " + messageId + " not found"));
    }

    public Map<UUID, Message> findAll() {
        return loadFromFile();
    }
}
