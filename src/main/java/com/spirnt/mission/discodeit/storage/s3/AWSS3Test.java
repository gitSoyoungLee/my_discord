package com.spirnt.mission.discodeit.storage.s3;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.awscore.presigner.PresignRequest;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Component
@Slf4j
@RequiredArgsConstructor
public class AWSS3Test {

  @Value("${discodeit.s3.bucket}")
  private String bucketName;

  @Value("${discodeit.s3.region}")
  private String region;

  private final S3Client s3Client;

  public void upload(UUID binaryContentId, byte[] bytes) {
    // 파일 이름 생성
    String fileName = String.valueOf(binaryContentId);
    // PutObjectRequest 생성
    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(bucketName)
        .key(fileName)
        .build();
    // 업로드
    s3Client.putObject(putObjectRequest, RequestBody.fromBytes(bytes));
  }

  public byte[] download(UUID binaryContentId) {
    String fileKey = String.valueOf(binaryContentId);
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucketName)
        .key(fileKey)
        .build();

    ResponseInputStream<GetObjectResponse> objectData = s3Client.getObject(getObjectRequest);

    try {
      return objectData.readAllBytes();
    } catch (IOException e) {
      // 예외 처리
      throw new RuntimeException("Failed to download file from S3", e);
    } finally {
      try {
        objectData.close();
      } catch (IOException e) {
        // ingore
      }
    }
  }

  // S3에 잠시 접근할 수 있는 임시 서명된 URL
  public String createPresignedUrl(UUID binaryContentId) {
    String key = String.valueOf(binaryContentId);

    // 1) S3Presigner 생성
    S3Presigner presigner = S3Presigner.builder()
        .region(Region.of(region))
        .build();

    // 2) 다운로드 요청(GetObjectRequest) 생성
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucketName)
        .key(key)
        .build();

    // 3) 프리사인 요청(GetObjectPresignRequest) 생성 - 1시간 유효
    PresignRequest presignRequest = GetObjectPresignRequest.builder()
        .signatureDuration(Duration.ofHours(1))
        .getObjectRequest(getObjectRequest)
        .build();
    // 4) 프리사인드 URL 생성
    PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(
        (GetObjectPresignRequest) presignRequest);

    // 5) 최종 URL 문자열로 반환
    return presignedGetObjectRequest.url().toString();
  }

}
