package com.spirnt.mission.discodeit.mapper;

import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.spirnt.mission.discodeit.dto.message.MessageDto;
import com.spirnt.mission.discodeit.dto.user.UserDto;
import com.spirnt.mission.discodeit.enity.Message;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageMapper {

  private final UserMapper userMapper;
  private final BinaryContentMapper binaryContentMapper;

  public MessageDto toDto(Message message) {
    UserDto authorDto = userMapper.toDto(message.getAuthor());
    List<BinaryContentDto> attachmentsDto = message.getAttachments().stream()
        .map(binaryContentMapper::toDto)
        .toList();
    return new MessageDto(message.getId(),
        message.getCreatedAt(),
        message.getUpdatedAt(),
        message.getContent(),
        message.getChannel().getId(),
        authorDto,
        attachmentsDto);
  }
}
