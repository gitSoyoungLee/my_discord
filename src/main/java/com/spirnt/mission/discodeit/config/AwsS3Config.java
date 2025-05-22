package com.spirnt.mission.discodeit.config;

import com.spirnt.mission.discodeit.storage.s3.AWSS3Test;
import com.spirnt.mission.discodeit.storage.s3.S3BinaryContentStorage;
import com.spirnt.mission.discodeit.util.S3Properties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsS3Config {

  private String accessKey;
  private String secretKey;
  private String region;
  private String bucket;
  private String presignedUrlExpiration;

  public AwsS3Config(S3Properties s3Properties) {
    this.accessKey = s3Properties.getAccessKey();
    this.secretKey = s3Properties.getSecretKey();
    this.region = s3Properties.getRegion();
    this.bucket = s3Properties.getBucket();
    this.presignedUrlExpiration = s3Properties.getPresignedUrlExpiration();
  }

  @Bean
  public AWSS3Test awss3Test() {
    return new AWSS3Test(accessKey, secretKey, region, bucket, presignedUrlExpiration);
  }

  @Bean
  @ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
  public S3BinaryContentStorage s3BinaryContentStorage() {
    return new S3BinaryContentStorage(accessKey, secretKey, region, bucket, presignedUrlExpiration);
  }
}
