package com.spirnt.mission.discodeit.mapper;

import com.spirnt.mission.discodeit.dto.channel.ChannelDto;
import com.spirnt.mission.discodeit.dto.user.UserDto;
import com.spirnt.mission.discodeit.enity.Channel;
import com.spirnt.mission.discodeit.enity.ChannelType;
import com.spirnt.mission.discodeit.enity.Message;
import com.spirnt.mission.discodeit.repository.MessageRepository;
import com.spirnt.mission.discodeit.repository.ReadStatusRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelMapper {

  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;
  private final UserMapper userMapper;

  public ChannelDto toDto(Channel channel) {
    // PRIVATE 채널인 경우 참여 유저 찾기
    List<UserDto> participants = new ArrayList<>();
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      readStatusRepository.findAllByChannelId(channel.getId()).stream()
          .map(readStatus -> userMapper.toDto(readStatus.getUser()))
          .forEach(participants::add);
    }
    // 해당 채널의 가장 최근 메세지 작성 시간 찾기
    Instant lastMessageAt = messageRepository.findTopByChannelIdOrderByCreatedAtDesc(
            channel.getId()).stream()
        .map(Message::getCreatedAt)
        .findFirst()
        .orElse(channel.getCreatedAt());    // 채널 내 메세지가 없는 경우 채널 생성 시간을 디폴트로 함

    return ChannelDto.of(channel, participants, lastMessageAt);
  }

}
