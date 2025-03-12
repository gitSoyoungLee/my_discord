package com.spirnt.mission.discodeit.service.basic;

import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.spirnt.mission.discodeit.dto.message.MessageCreateRequest;
import com.spirnt.mission.discodeit.dto.message.MessageDto;
import com.spirnt.mission.discodeit.dto.message.MessageUpdateRequest;
import com.spirnt.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.spirnt.mission.discodeit.enity.BinaryContent;
import com.spirnt.mission.discodeit.enity.Channel;
import com.spirnt.mission.discodeit.enity.Message;
import com.spirnt.mission.discodeit.enity.User;
import com.spirnt.mission.discodeit.mapper.MessageMapper;
import com.spirnt.mission.discodeit.repository.BinaryContentRepository;
import com.spirnt.mission.discodeit.repository.ChannelRepository;
import com.spirnt.mission.discodeit.repository.MessageRepository;
import com.spirnt.mission.discodeit.repository.UserRepository;
import com.spirnt.mission.discodeit.service.BinaryContentService;
import com.spirnt.mission.discodeit.service.MessageService;
import com.spirnt.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageMapper messageMapper;

  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  private final BinaryContentService binaryContentService;
  private final BinaryContentRepository binaryContentRepository;
  private final UserStatusService userStatusService;

  @Transactional
  @Override
  public MessageDto create(MessageCreateRequest messageCreateRequest,
      List<MultipartFile> attachments) {
    // User와 Channel이 존재하는지 검증
    UUID authorId = messageCreateRequest.authorId();
    UUID channelId = messageCreateRequest.channelId();
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(
            () -> new NoSuchElementException("Channel with id: " + channelId + " not found"));
    User user = userRepository.findById(authorId)
        .orElseThrow(() -> new NoSuchElementException("User with id: " + channelId + " not found"));
    // 첨부 파일 업로드
    List<UUID> attachedFilesId = new ArrayList<>();
    for (MultipartFile file : Optional.ofNullable(attachments)
        .orElse(Collections.emptyList())) {
      BinaryContentCreateRequest binaryContentCreateRequest = new BinaryContentCreateRequest(file);
      BinaryContentDto binaryContent = binaryContentService.create(binaryContentCreateRequest);
      attachedFilesId.add(binaryContent.getId());
    }
    List<BinaryContent> attachedFiles = binaryContentRepository.findAllById(attachedFilesId);
    Message message = messageRepository.save(new Message(messageCreateRequest.content(),
        user, channel, attachedFiles));
    // 메세지 작성자를 Online 상태로
    userStatusService.updateByUserId(authorId, new UserStatusUpdateRequest(Instant.now()));
    return messageMapper.toDto(message);
  }

  @Override
  public MessageDto find(UUID messageId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));
    return messageMapper.toDto(message);
  }

  @Override
  public List<MessageDto> findAllByChannelId(UUID channelId) {
    if (!channelRepository.existsById(channelId)) {
      throw new NoSuchElementException("Channel with id " + channelId + " not found");
    }

    List<Message> messages = messageRepository.findAllByChannelId(channelId);
    return messages.stream()
        .sorted(Comparator.comparing(message -> message.getCreatedAt()))
        .map(messageMapper::toDto)
        .collect(Collectors.toList());
  }


  @Override
  public MessageDto update(UUID messageId, MessageUpdateRequest dto) {
    messageRepository.updateById(messageId, dto.newContent());
    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));
    return messageMapper.toDto(message);
  }

  @Override
  public void delete(UUID messageId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));
    messageRepository.delete(message);
  }

}
