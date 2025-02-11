package com.spirnt.mission.discodeit.enity;

import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentCreate;
import lombok.Getter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

// 이미지, 파일 등 바이너리 데이터를 표현하는 도메인 모델
@Getter
public class BinaryContent extends Common implements Serializable {
    private static final long serialVersionUID = 1L;
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
        // 파일 업로드
        // uploadedFiles 디렉토리에 {UUID.확장자} 형태로 저장
        String uploadDir = "uploadedFiles/";
        String originalFilename = dto.file().getOriginalFilename();
        String fileExtension;   //확장자추출
        if (originalFilename == null || originalFilename.lastIndexOf(".") == -1) {
            fileExtension = "";
        } else fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        Path path = Paths.get(uploadDir + this.getId() + fileExtension);
        File newFile = new File(uploadDir + this.getId() + fileExtension);
        try (FileOutputStream fos = new FileOutputStream(newFile)) {
            fos.write(dto.file().getBytes());
        }
        this.filePath = newFile.getPath();
    }

    @Override
    public String toString() {
        return "BinaryContent[ID:" + this.getId() +
                " FilePath: " + filePath + "]";
    }
}
