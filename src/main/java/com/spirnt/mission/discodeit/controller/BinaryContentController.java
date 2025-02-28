package com.spirnt.mission.discodeit.controller;

import com.spirnt.mission.discodeit.enity.BinaryContent;
import com.spirnt.mission.discodeit.service.BinaryContentService;
import com.spirnt.mission.discodeit.swagger.BinaryContentApiDocs;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/binaryContents")
public class BinaryContentController implements BinaryContentApiDocs {

  private final BinaryContentService binaryContentService;

  private boolean isImage(String fileType) {
    return fileType.startsWith("image/");
  }

  // 단건 조회 및 다운로드
  @GetMapping("/{binaryContentId}")
  public ResponseEntity<BinaryContent> getFile(@PathVariable UUID fileId) {
    BinaryContent binaryContent = binaryContentService.find(fileId);
//    return ResponseEntity.ok()
//        .header(HttpHeaders.CONTENT_TYPE, binaryContent.getFileType())
//        // 이미지인 경우 inline으로 바로 보이게, 아닌 경우 다운로드 가능한 첨부파일 형태로
//        .header(HttpHeaders.CONTENT_DISPOSITION, isImage(binaryContent.getFileType())
//            ? "inline"
//            : "attachment; filename=\"" + binaryContent.getFileName() + "\"")
//        .body(resource);
    return ResponseEntity.ok(binaryContent);
  }

  @GetMapping()
  public ResponseEntity<List<BinaryContent>> getFiles(List<UUID> binaryContentIds) {
    List<BinaryContent> binaryContents = binaryContentService.findAllByIdIn(binaryContentIds);
    return ResponseEntity.ok(binaryContents);
  }

//  // 삭제
//  @RequestMapping(value = "/{fileId}", method = RequestMethod.DELETE)
//  public ResponseEntity<?> deleteFile(@PathVariable UUID fileId) {
//    binaryContentService.delete(fileId);
//    return ResponseEntity.noContent().build();
//  }

}
