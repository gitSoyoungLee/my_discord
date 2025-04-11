package com.spirnt.mission.discodeit.util;

import java.util.Properties;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class YamlPropertyManager {

  private final YamlPropertiesFactoryBean yamlPropertiesFactoryBean;

  public YamlPropertyManager() {
    this.yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
    ClassPathResource classPathResource = new ClassPathResource("application.yaml");
    yamlPropertiesFactoryBean.setResources(classPathResource);
  }

  public String getValueByKey(String keyName) {
    Properties properties = yamlPropertiesFactoryBean.getObject();
    return properties.getProperty(keyName);
  }


}
