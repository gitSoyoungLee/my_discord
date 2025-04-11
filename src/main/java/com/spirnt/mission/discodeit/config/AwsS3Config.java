package com.spirnt.mission.discodeit.config;

import com.spirnt.mission.discodeit.storage.s3.AWSS3Test;
import com.spirnt.mission.discodeit.storage.s3.S3BinaryContentStorage;
import com.spirnt.mission.discodeit.util.YamlPropertyManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsS3Config {

  private final YamlPropertyManager yamlPropertyManager;

  private String accessKey;
  private String secretKey;
  private String region;
  private String bucket;
  private String presignedUrlExpiration;

  public AwsS3Config(YamlPropertyManager yamlPropertyManager) {
    this.accessKey = yamlPropertyManager.getValueByKey("discodeit.s3.access-key");
    this.secretKey = yamlPropertyManager.getValueByKey("discodeit.s3.secret-key");
    this.region = yamlPropertyManager.getValueByKey("discodeit.s3.region");
    this.bucket = yamlPropertyManager.getValueByKey("discodeit.s3.bucket");
    this.presignedUrlExpiration = yamlPropertyManager.getValueByKey(
        "discodeit.s3.presigned-url-expiration");
    this.yamlPropertyManager = yamlPropertyManager;
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
