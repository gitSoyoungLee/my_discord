package com.spirnt.mission.discodeit.repository.file;

import com.spirnt.mission.discodeit.enity.Channel;
import com.spirnt.mission.discodeit.repository.ChannelRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class FileChannelRepository extends FileRepository implements ChannelRepository {

    public FileChannelRepository() {
        super(Paths.get(System.getProperty("user.dir"), "file-data-map", Channel.class.getSimpleName()));
    }

    @Override
    public void save(Channel channel) {
        Path path = resolvePath(channel.getId());
        saveToFile(path, channel);
    }

    @Override
    public void delete(UUID channelId) {
        Path path = resolvePath(channelId);
        deleteFile(path);
    }

    @Override
    public Optional<Channel> findById(UUID channelId) {
        Path path = resolvePath(channelId);
        return loadFromFile(path);
    }

    public Map<UUID, Channel> findAll() {
        Map<UUID, Channel> channels = new HashMap<>();
        // 폴더 내 모든 .ser 파일을 찾음
        try (Stream<Path> paths = Files.walk(this.getDIRECTORY())) {
            paths.filter(path -> path.toString().endsWith(".ser"))  // .ser 파일만 필터링
                    .forEach(path -> {
                        Optional<Channel> channelOptional = loadFromFile(path);
                        channelOptional.ifPresent(channel -> channels.put(channel.getId(), channel));
                    });
        } catch (IOException e) {
            System.out.println("파일을 읽는 중 오류가 발생했습니다: " + e.getMessage());
        }

        return channels;
    }
}
