package discodeit.repository.file;

import discodeit.enity.Channel;
import discodeit.enity.Message;
import discodeit.enity.User;
import discodeit.repository.MessageRepository;

import java.io.*;
import java.util.*;

public class FileMessageRepository implements MessageRepository {
    @Override
    public void save(Message message) {
        Map<UUID, Message> messages = this.findAll();
        if (messages == null) {
            messages = new HashMap<>();
        }
        try (FileOutputStream fos = new FileOutputStream("message.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            messages.put(message.getId(), message);
            oos.writeObject(messages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(UUID messageId) {
        Map<UUID, Message> messages = this.findAll();
        if (messages == null || !messages.containsKey(messageId)) {
            throw new NoSuchElementException("Message ID: " + messageId + " not found");
        }
        messages.remove(messageId);
        try (FileOutputStream fos = new FileOutputStream("message.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(messages);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchElementException e) {
            throw e;
        }
    }

    @Override
    public Message findById(UUID messageId) {
        Map<UUID, Message> messages= findAll();
        Optional<Message> finding_message =  messages.values().stream()
                .filter(message -> message.getId().equals(messageId))
                .findAny();
        return finding_message
                .orElseThrow(() -> new NoSuchElementException("Message ID: " + messageId + " not found"));
    }

    public Map<UUID, Message> findAll() {
        Map<UUID, Message> messages = new HashMap<>();
        try (FileInputStream fis = new FileInputStream("message.ser");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            messages = (Map<UUID, Message>) ois.readObject();
        } catch (EOFException e) {
            return messages;
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
        return messages;
    }
}
