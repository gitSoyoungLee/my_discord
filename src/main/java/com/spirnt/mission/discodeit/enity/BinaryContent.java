package com.spirnt.mission.discodeit.enity;

import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentCreate;
import lombok.Getter;

import java.io.IOException;
import java.util.UUID;

// 이미지, 파일 등 바이너리 데이터를 표현하는 도메인 모델
@Getter
public class BinaryContent extends Common {
    private UUID userId;    // 업로드한 유저
    private UUID messageId; //첨부된 메세지
    private String fileName;    // 파일 원본 이름
    private String fileType;    // 파일 타입
    private String filePath;    // 서버에 저장된 경로

    public BinaryContent(BinaryContentCreate dto) throws IOException {
        super();
        this.userId = dto.userId();
        this.messageId = dto.messageId();
        this.fileName = dto.file().getOriginalFilename();
        this.fileType = dto.file().getContentType();
    }

    public void setFilePath(String uploadPath) {
        this.filePath = uploadPath;
    }

    @Override
    public String toString() {
        return "BinaryContent[ID:" + this.getId() +
                " FilePath: " + filePath + "]";
    }
}
