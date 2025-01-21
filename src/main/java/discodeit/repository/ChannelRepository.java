package discodeit.repository;

import discodeit.enity.Channel;

import java.util.Map;
import java.util.UUID;

public interface ChannelRepository {
    void save(Channel channel);
    void delete(UUID channelId);
    Channel findById(UUID channelId);

    Map<UUID, Channel> findAll();
}
