package com.spirnt.mission.discodeit.config;


import com.spirnt.mission.discodeit.service.ChannelService;
import com.spirnt.mission.discodeit.service.MessageService;
import com.spirnt.mission.discodeit.service.UserService;
import com.spirnt.mission.discodeit.service.basic.BasicChannelService;
import com.spirnt.mission.discodeit.service.basic.BasicMessageService;
import com.spirnt.mission.discodeit.service.basic.BasicUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {
    // Basic*Service 구현체를 Service 인터페이스의 Bean으로 등록

    @Bean
    public UserService userService() {
        return new BasicUserService();
    }

    @Bean
    public ChannelService channelService() {
        return new BasicChannelService();
    }

    @Bean
    public MessageService messageService() {
        return new BasicMessageService();
    }
}
