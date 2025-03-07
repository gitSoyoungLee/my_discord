package com.spirnt.mission.discodeit.enity;

import com.spirnt.mission.discodeit.enity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 이미지, 파일 등 바이너리 데이터를 표현하는 도메인 모델
@Entity
@Table(name = "binary_contents")
@NoArgsConstructor
@Getter
public class BinaryContent extends BaseEntity {

  @Column(name = "file_name")
  private String fileName;
  private Long size;
  @Column(name = "content_type")
  private String contentType;
  private byte[] bytes;

  public BinaryContent(String fileName, Long size, String contentType, byte[] bytes) {
    super();
    this.fileName = fileName;
    this.size = size;
    this.contentType = contentType;
    this.bytes = bytes;
  }

}
