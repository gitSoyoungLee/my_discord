package discodeit.repository.file;

import discodeit.enity.User;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// Serializable한 객체만 T로 들어올 수 있음
public abstract class FileRepository<T extends Serializable> {
    private String fileName;

    protected void setFileName(String fileName) {
        this.fileName = fileName;
    }

    protected void saveToFile(Map<UUID, T> data) {
        try (FileOutputStream fos = new FileOutputStream(fileName);
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected Map<UUID, T> loadFromFile() {
        try (FileInputStream fis = new FileInputStream(fileName);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (Map<UUID, T>) ois.readObject();
        } catch (EOFException e) {
            return new HashMap<>(); // 파일이 비어있을 경우 새로운 맵 반환
        } catch (IOException | ClassNotFoundException e) {  // 파일이 없을 경우 새로운 맵 반환
            return new HashMap<>();
        }
    }
}
