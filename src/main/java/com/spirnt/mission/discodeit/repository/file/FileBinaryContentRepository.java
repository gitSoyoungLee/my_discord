package com.spirnt.mission.discodeit.repository.file;

import com.spirnt.mission.discodeit.enity.BinaryContent;
import com.spirnt.mission.discodeit.repository.BinaryContentRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class FileBinaryContentRepository extends FileRepository implements BinaryContentRepository {
    public FileBinaryContentRepository() {
        super(Paths.get(System.getProperty("user.dir"), "file-data-map", BinaryContent.class.getSimpleName()));
    }

    @Override
    public void save(BinaryContent binaryContent) {
        Path path = resolvePath(binaryContent.getId());
        saveToFile(path, binaryContent);
    }

    @Override
    public void delete(UUID id) {
        Path path = resolvePath(id);
        deleteFile(path);
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        Path path = resolvePath(id);
        return loadFromFile(path);
    }

    @Override
    public Map<UUID, BinaryContent> findAll() {
        Map<UUID, BinaryContent> map = new HashMap<>();
        // 폴더 내 모든 .ser 파일을 찾음
        try (Stream<Path> paths = Files.walk(this.getDIRECTORY())) {
            paths.filter(path -> path.toString().endsWith(".ser"))  // .ser 파일만 필터링
                    .forEach(path -> {
                        Optional<BinaryContent> binaryContent = loadFromFile(path);
                        binaryContent.ifPresent(bc -> map.put(bc.getId(), bc));
                    });
        } catch (IOException e) {
            System.out.println("파일을 읽는 중 오류가 발생했습니다: " + e.getMessage());
        }

        return map;
    }
}
