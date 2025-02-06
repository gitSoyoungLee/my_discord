package com.spirnt.mission.discodeit.service.basic;

import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentCreate;
import com.spirnt.mission.discodeit.enity.BinaryContent;
import com.spirnt.mission.discodeit.repository.BinaryContentRepository;
import com.spirnt.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository repository;

    @Override
    public UUID create(BinaryContentCreate binaryContentCreate) {
        try {
            BinaryContent binaryContent= new BinaryContent(binaryContentCreate);
            return binaryContent.getId();
        } catch (IOException e) {   //파일을 바이트로 변환하며 발생할 수 있는 예외
            return null;
        }
    }

    @Override
    public BinaryContent find(UUID id) {
        return repository.findById(id)
                .orElseThrow(()-> new NoSuchElementException("BinaryContent Not Found"));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> uuids) {
        List<BinaryContent> list = new ArrayList<>();
        for(UUID id:uuids){
            list.add(find(id));
        }
        return list;
    }

    @Override
    public void delete(UUID id) {
        repository.delete(id);
    }
}
