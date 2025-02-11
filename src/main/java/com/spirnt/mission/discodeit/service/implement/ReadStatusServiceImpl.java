package com.spirnt.mission.discodeit.service.implement;

import com.spirnt.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.spirnt.mission.discodeit.enity.ReadStatus;
import com.spirnt.mission.discodeit.repository.ReadStatusRepository;
import com.spirnt.mission.discodeit.service.ChannelService;
import com.spirnt.mission.discodeit.service.ReadStatusService;
import com.spirnt.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReadStatusServiceImpl implements ReadStatusService {
    @Autowired
    private final ReadStatusRepository repository;
    @Autowired
    private UserService userService;
    @Autowired
    private ChannelService channelService;


    @Override
    public ReadStatus create(ReadStatusDto readStatusDto) {
        // User와 Channel 존재하지 않으면 예외 발생
        try {
            channelService.find(readStatusDto.channelId());
            userService.find(readStatusDto.userId());
        } catch (NoSuchElementException e) {
            throw e;
        }
        //이미 해당 채널-유저를 가진 ReadStatus가 있으면 예외 발생
        if (repository.findAll().values().stream()
                .anyMatch(readStatus -> readStatus.getChannelId().equals(readStatusDto.channelId())
                        && readStatus.getUserId().equals(readStatusDto.userId()))) {
            throw new IllegalStateException("The ReadStatus with UserId and ChannelId Already Exists");
        }
        ReadStatus readStatus = new ReadStatus(readStatusDto);
        repository.save(readStatus);
        return readStatus;
    }

    @Override
    public ReadStatus find(UUID readStatusId) {
        return repository.findById(readStatusId)
                .orElseThrow(() -> new NoSuchElementException("Read Status ID Not Found"));
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
    public ReadStatus update(UUID readStatusId, ReadStatusDto readStatusDto) {
        ReadStatus readStatus = repository.findById(readStatusId)
                .orElseThrow(() -> new NoSuchElementException("Read Status Not Found"));
        readStatus.update(readStatusDto.lastReadAt());
        repository.save(readStatus);
        return readStatus;
    }


    @Override
    public void delete(UUID id) {
        repository.delete(id);
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        Map<UUID, ReadStatus> map = repository.findAll();
        List<UUID> list = map.values().stream()
                .filter(readStatus -> readStatus.getUserId().equals(channelId))
                .map(readStatus -> readStatus.getId())
                .collect(Collectors.toList());
        for (UUID id : list) {
            delete(id);
        }
    }

}


