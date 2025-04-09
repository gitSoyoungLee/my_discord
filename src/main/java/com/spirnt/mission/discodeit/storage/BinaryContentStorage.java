package com.spirnt.mission.discodeit.storage;

import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentDto;
import java.io.InputStream;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

public interface BinaryContentStorage {

  UUID put(UUID binaryContentId, byte[] bytes);

  InputStream get(UUID binaryContentId);

  ResponseEntity<?> download(BinaryContentDto binaryContentDto);

}
