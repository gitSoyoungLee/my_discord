package com.spirnt.mission.discodeit.service.basic;

import com.spirnt.mission.discodeit.dto.channel.ChannelCreateRequest;
import com.spirnt.mission.discodeit.dto.channel.ChannelResponse;
import com.spirnt.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.spirnt.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.spirnt.mission.discodeit.enity.Channel;
import com.spirnt.mission.discodeit.enity.ChannelType;
import com.spirnt.mission.discodeit.repository.ChannelRepository;
import com.spirnt.mission.discodeit.repository.MessageRepository;
import com.spirnt.mission.discodeit.repository.UserRepository;
import com.spirnt.mission.discodeit.service.ChannelService;
import com.spirnt.mission.discodeit.service.MessageService;
import com.spirnt.mission.discodeit.service.ReadStatusService;
import com.spirnt.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    @Autowired
    private final ChannelRepository channelRepository;
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private MessageService messageService;
//    @Autowired
//    private ReadStatusService readStatusService;

    @Override
    public Channel createChannelPublic(ChannelCreateRequest dto) {
        Channel channel = new Channel(dto, ChannelType.PUBLIC);
        channelRepository.save(channel);
        return channel;
    }

    @Override
    public Channel createChannelPrivate(ChannelCreateRequest dto) {
        Channel channel = new Channel(dto, ChannelType.PRIVATE);
        channelRepository.save(channel);
//        //ReadStatus 생성
//        for (UUID userId : dto.getUsersId()) {
//            readStatusService.create(new ReadStatusDto(userId, channel.getId(), Instant.now()));
//        }
        return channel;
    }

    @Override
    public Channel find(UUID channelId) {
        return  channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel ID: " + channelId + " Not Found"));
    }

    @Override
    public List<Channel> findAllByUserId(UUID userId) {
        Map<UUID, Channel> data = channelRepository.findAll();
        return data.values().stream()
                .collect(Collectors.toList());
    }

    @Override
    public Channel update(UUID channelId, ChannelUpdateRequest channelUpdateRequest) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel ID: " + channelId + " Not Found"));
        // PRIVATE 채널은 업데이트할 수 없음
        if (channel.getType() == ChannelType.PRIVATE) return channel;
        channel.update(channelUpdateRequest);
        channelRepository.save(channel);
        return channel;
    }

    @Override
    public void delete(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel ID: " + channelId + " Not Found"));
        channelRepository.delete(channelId);
    }

    @Override
    public void addUserIntoChannel(UUID channelId, UUID userId) {
        // 존재하는지 검증
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel ID: " + channelId + " Not Found"));
//        try {
//            userService.find(userId);
//        } catch (NoSuchElementException e) {
//            throw e;
//        }
        // 유저가 채널에 이미 입장한 경우 조건 불만족으로 실패
        if (channel.containsUser(userId)) {
            return;
        }
        channel.getUsersId().add(userId);
        channelRepository.save(channel);
    }

    @Override
    public void deleteUserInChannel(UUID channelId, UUID userId) {
        // 존재하는지 검증
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel ID: " + channelId + " Not Found"));
//        try {
//            userService.find(userId);
//        } catch (NoSuchElementException e) {
//            throw e;
//        }
        // 유저가 채널에 없는 경우 조건 불만족으로 실패
        if (!channel.containsUser(userId)) {
            return;
        }
        channel.getUsersId().remove(userId);
        channelRepository.save(channel);
    }

    @Override
    public void deleteUserInAllChannels(UUID userId) {
        Map<UUID, Channel> data = channelRepository.findAll();
        if (data.isEmpty()) return;
        data.values().stream()
                .forEach(channel -> {
                    channel.getUsersId().remove(userId);
                    // 채널 객체 수정 후 ser 파일에 반영
                    channelRepository.save(channel);
                });
    }

    @Override
    public boolean existsById(UUID id) {
        return channelRepository.existsById(id);
    }
}
