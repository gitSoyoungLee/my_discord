package com.spirnt.mission.discodeit.service.implement;

import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentCreate;
import com.spirnt.mission.discodeit.enity.BinaryContent;
import com.spirnt.mission.discodeit.repository.BinaryContentRepository;
import com.spirnt.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BinaryContentServiceImpl implements BinaryContentService {
    @Autowired
    private final BinaryContentRepository repository;


    @Override
    public BinaryContent create(BinaryContentCreate dto) {
        try {
            BinaryContent binaryContent = new BinaryContent(dto);
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

    @Override
    public void deleteUserProfile(UUID userId) {
        BinaryContent binaryContent = findUserProfile(userId);
        if(binaryContent!=null) {
            delete(binaryContent.getId());
        }
    }

    @Override
    public void deleteByMessageId(UUID messageId) {
        List<BinaryContent> list = findByMessageId(messageId);
        for (BinaryContent binaryContent : list) {
            delete(binaryContent.getId());
        }
    }
}
