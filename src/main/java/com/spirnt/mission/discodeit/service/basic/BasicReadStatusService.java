package com.spirnt.mission.discodeit.service.basic;

import com.spirnt.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.spirnt.mission.discodeit.enity.Channel;
import com.spirnt.mission.discodeit.enity.ReadStatus;
import com.spirnt.mission.discodeit.enity.User;
import com.spirnt.mission.discodeit.repository.ReadStatusRepository;
import com.spirnt.mission.discodeit.service.ChannelService;
import com.spirnt.mission.discodeit.service.ReadStatusService;
import com.spirnt.mission.discodeit.service.UserService;
import lombok.Locked;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository repository;
    private final UserService userService;
    private final ChannelService channelService;

    @Override
    public UUID create(ReadStatusDto readStatusDto) {
        // User와 Channel 존재하지 않으면 예외 발생
        Channel channel = channelService.findById(readStatusDto.channelId())
                .orElseThrow(()->new NoSuchElementException("Channel ID: "+readStatusDto.channelId()+" Not Found"));
        User user = userService.findById(readStatusDto.userId())
                .orElseThrow(()->new NoSuchElementException("User ID: "+readStatusDto.userId()+" Not Found"));
        //이미 해당 채널-유저를 가진 ReadStatus가 있으면 예외 발생
        if(repository.findAll().values().stream()
                .anyMatch(readStatus -> readStatus.getChannelId().equals(readStatusDto.channelId())
                && readStatus.getUserId().equals(readStatusDto.userId()))) {
            throw new IllegalStateException("The ReadStatus with UserId and ChannelId Already Exists");
        }
        ReadStatus readStatus = new ReadStatus(readStatusDto);
        repository.save(readStatus);
        return readStatus.getId();
    }

    @Override
    public ReadStatus find(UUID readStatusId) {
        return repository.findById(readStatusId)
                .orElseThrow(()->new NoSuchElementException("Read Status ID Not Found"));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        Map<UUID, ReadStatus> map = repository.findAll();
        List<ReadStatus> list = map.values().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .collect(Collectors.toList());
        return list;
    }

    @Override
    public void update(UUID readStatusId, ReadStatusDto readStatusDto) {
        ReadStatus readStatus = repository.findById(readStatusId)
                .orElseThrow(()->new NoSuchElementException("Read Status Not Found"));
        readStatus.update(readStatusDto.lastReadAt());
        repository.save(readStatus);
    }

    @Override
    public void delete(UUID id) {
        repository.delete(id);
    }
}
