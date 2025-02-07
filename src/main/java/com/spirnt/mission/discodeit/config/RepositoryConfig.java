package com.spirnt.mission.discodeit.config;

import com.spirnt.mission.discodeit.repository.*;
import com.spirnt.mission.discodeit.repository.file.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {
    //File*Repository 구현체를 Repository 인터페이스의 Bean으로 등록
    @Bean
    public UserRepository userRepository() {
        return new FileUserRepository();
    }

    @Bean
    public ChannelRepository channelRepository() {
        return new FileChannelRepository();
    }

    @Bean
    public MessageRepository messageRepository() {
        return new FileMessageRepository();
    }

    //

    @Bean
    public ReadStatusRepository readStatusRepository() {
        return new FileReadStatusRepository();
    }

    @Bean
    public UserStatusRepository userStatusRepository() {
        return new FileUserStatusRepository();
    }

    @Bean
    public BinaryContentRepository binaryContentRepository() {
        return new FileBinaryContentRepository();
    }


}
