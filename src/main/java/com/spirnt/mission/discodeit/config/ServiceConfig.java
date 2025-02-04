package com.spirnt.mission.discodeit.config;


import com.spirnt.mission.discodeit.repository.ChannelRepository;
import com.spirnt.mission.discodeit.repository.MessageRepository;
import com.spirnt.mission.discodeit.repository.UserRepository;
import com.spirnt.mission.discodeit.service.ChannelService;
import com.spirnt.mission.discodeit.service.MessageService;
import com.spirnt.mission.discodeit.service.UserService;
import com.spirnt.mission.discodeit.service.basic.BasicChannelService;
import com.spirnt.mission.discodeit.service.basic.BasicMessageService;
import com.spirnt.mission.discodeit.service.basic.BasicUserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {
    // Basic*Service 구현체를 Service 인터페이스의 Bean으로 등록

    //Repository Bean을 불러 오기 위한 context
    AnnotationConfigApplicationContext context =
            new AnnotationConfigApplicationContext(RepositoryConfig.class);
    @Bean
    public UserService userService() {
        return new BasicUserService(context.getBean(UserRepository.class));
    }

    @Bean
    public ChannelService channelService() {
        return new BasicChannelService(context.getBean(ChannelRepository.class));
    }

    @Bean
    public MessageService messageService() {
        return new BasicMessageService(context.getBean(MessageRepository.class));
    }
}
