package com.spirnt.mission.discodeit.service.basic;

import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentCreate;
import com.spirnt.mission.discodeit.enity.BinaryContent;
import com.spirnt.mission.discodeit.repository.BinaryContentRepository;
import com.spirnt.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RequiredArgsConstructor
public class BinaryContentServiceImpl implements BinaryContentService {
    private final BinaryContentRepository repository;

    @Override
    public BinaryContent create(BinaryContentCreate dto) {
        try {
            BinaryContent binaryContent = new BinaryContent(dto);
            // 파일 업로드
            // uploadedFiles 디렉토리에 {UUID.확장자} 형태로 저장
            String uploadDir = "uploadedFiles/";
            String originalFilename = dto.file().getOriginalFilename();
            String fileExtension;   //확장자추출
            if (originalFilename == null || originalFilename.lastIndexOf(".") == -1) {
                fileExtension = "";
            } else fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
//            byte[] bytes = dto.file().getBytes();
            Path path = Paths.get(uploadDir + binaryContent.getId() + fileExtension);
            File newFile = new File(uploadDir + binaryContent.getId() + fileExtension);
            try (FileOutputStream fos = new FileOutputStream(newFile)) {
                fos.write(dto.file().getBytes());
            }
            binaryContent.setFilePath(newFile.getPath());
            repository.save(binaryContent);
            return binaryContent;
        } catch (IOException e) {   // MultipartFile 오류 발생 시
            return null;
        }
    }

    @Override
    public BinaryContent find(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("BinaryContent Not Found"));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> uuids) {
        List<BinaryContent> list = new ArrayList<>();
        for (UUID id : uuids) {
            list.add(find(id));
        }
        return list;
    }

    @Override
    public void delete(UUID id) {
        repository.delete(id);
    }

    //프로필사진 찾기는 하나만
    @Override
    public BinaryContent findUserProfile(UUID userId) {
        Map<UUID, BinaryContent> map = repository.findAll();
        return map.values().stream()
                .filter(binaryContent -> binaryContent.getUserId().equals(userId) &&
                        binaryContent.getMessageId() == null)
                .findAny()
                .orElse(null);
    }

    // 메세지에 첨부된 파일은 여러 개 반환
    @Override
    public List<BinaryContent> findByMessageId(UUID messageId) {
        Map<UUID, BinaryContent> map = repository.findAll();
        List<BinaryContent> list = new ArrayList<>();
        map.values().stream()
                .filter(binaryContent -> binaryContent.getMessageId() != null &&
                        binaryContent.getMessageId().equals(messageId))
                .forEach(binaryContent -> list.add(binaryContent));
        return list;
    }
}
