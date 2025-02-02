package discodeit.repository.file;

import discodeit.enity.Channel;
import discodeit.repository.ChannelRepository;

import java.io.*;
import java.util.*;

public class FileChannelRepository extends FileRepository implements ChannelRepository {

    public FileChannelRepository() {
        this.setFileName("channel.ser");
    }

    @Override
    public void save(Channel channel) {
        // ser 파일에 Map으로 저장
        Map<UUID, Channel> channels = loadFromFile();
        channels.put(channel.getId(), channel);
        saveToFile(channels);
    }

    @Override
    public void delete(UUID channelId) {
        Map<UUID, Channel> channels = loadFromFile();
        if (!channels.containsKey(channelId)) {
            throw new NoSuchElementException("Channel ID: " + channelId + " not found");
        }
        // 기존 Map을 읽어와 데이터를 삭제한 후 덮어씌움
        channels.remove(channelId);
        saveToFile(channels);
    }

    @Override
    public Channel findById(UUID channelId) {
        Map<UUID, Channel> channels = loadFromFile();
        return Optional.ofNullable(channels.get(channelId))
                .orElseThrow(() -> new NoSuchElementException("Channel ID: " + channelId + " not found"));
    }

    public Map<UUID, Channel> findAll() {
        return loadFromFile();
    }
}
