package com.spirnt.mission.discodeit.service.basic;

import com.spirnt.mission.discodeit.dto.channel.ChannelCreateRequest;
import com.spirnt.mission.discodeit.dto.channel.ChannelResponse;
import com.spirnt.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.spirnt.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.spirnt.mission.discodeit.enity.Channel;
import com.spirnt.mission.discodeit.enity.ChannelType;
import com.spirnt.mission.discodeit.repository.ChannelRepository;
import com.spirnt.mission.discodeit.service.ChannelService;
import com.spirnt.mission.discodeit.service.MessageService;
import com.spirnt.mission.discodeit.service.ReadStatusService;
import com.spirnt.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private ReadStatusService readStatusService;

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
        //ReadStatus 생성
        for (UUID userId : dto.getUsersId()) {
            readStatusService.create(new ReadStatusDto(userId, channel.getId(), Instant.now()));
        }
        return channel;
    }

    @Override
    public ChannelResponse find(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel ID: " + channelId + " Not Found"));
        // 해당 채널의 가장 최근 메세지 작성 시간 찾기
        Instant lastSeenAt = messageService.findLastMessageInChannel(channelId)
                .orElse(channel.getCreatedAt());    // 채널 내 메세지가 없는 경우 채널 생성 시간을 디폴트로 함
        return new ChannelResponse(channel, lastSeenAt);
    }

    @Override
    public List<ChannelResponse> findAllByUserId(UUID userId) {
        Map<UUID, Channel> data = channelRepository.findAll();
        List<ChannelResponse> list = new ArrayList<>();
        data.values().stream()
                .forEach(channel -> {
                    //PUBLIC 채널은 모든 유저가 조회할 수 있음
                    //PRIVATE 채널은 조회한 User가 참여한 채널만 조회할 수 있음
                    if (channel.getType() == ChannelType.PUBLIC ||
                            (channel.getType() == ChannelType.PRIVATE && channel.containsUser(userId))) {
                        list.add(find(channel.getId()));
                    }
                });
        return list;
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
        // 채널에 속해있던 메세지도 삭제
        messageService.deleteByChannelId(channelId);
        // 관련 ReadStatus 삭제하기
        readStatusService.deleteByChannelId(channelId);
        channelRepository.delete(channelId);
    }

    @Override
    public void addUserIntoChannel(UUID channelId, UUID userId) {
        // 존재하는지 검증
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel ID: " + channelId + " Not Found"));
        try {
            userService.find(userId);
        } catch (NoSuchElementException e) {
            throw e;
        }
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
        try {
            userService.find(userId);
        } catch (NoSuchElementException e) {
            throw e;
        }
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

}
