package com.spirnt.mission.discodeit.service.basic;

import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.spirnt.mission.discodeit.dto.message.MessageCreateRequest;
import com.spirnt.mission.discodeit.dto.message.MessageDto;
import com.spirnt.mission.discodeit.dto.message.MessageUpdateRequest;
import com.spirnt.mission.discodeit.dto.response.PageResponse;
import com.spirnt.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.spirnt.mission.discodeit.entity.BinaryContent;
import com.spirnt.mission.discodeit.entity.Channel;
import com.spirnt.mission.discodeit.entity.Message;
import com.spirnt.mission.discodeit.entity.User;
import com.spirnt.mission.discodeit.exception.Channel.ChannelNotFoundException;
import com.spirnt.mission.discodeit.exception.Message.MessageNotFoundException;
import com.spirnt.mission.discodeit.exception.User.UserNotFoundException;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
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

    User user = userRepository.findById(authorId)
        .orElseThrow(() -> {
          log.warn("[Creating Message Failed: User with id {} not found]", authorId);
          return new UserNotFoundException(Instant.now(), Map.of("userId", authorId));
        });
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> {
          log.warn("[Creating Message Failed: Channel with id {} not found]", channelId);
          return new ChannelNotFoundException(Instant.now(), Map.of("channelId", channelId));
        });

    // 첨부 파일 업로드
    List<UUID> attachedFilesId = new ArrayList<>();
    for (MultipartFile file : Optional.ofNullable(attachments)
        .orElse(Collections.emptyList())) {
      BinaryContentCreateRequest binaryContentCreateRequest = new BinaryContentCreateRequest(file);
      BinaryContentDto binaryContent = binaryContentService.create(binaryContentCreateRequest);
      attachedFilesId.add(binaryContent.getId());
    }
    List<BinaryContent> attachedFiles = binaryContentRepository.findAllById(attachedFilesId);

    // 메시지 저장
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
            () -> new MessageNotFoundException(Instant.now(), Map.of("messageId", messageId)));
    return messageMapper.toDto(message);
  }

  @Transactional(readOnly = true)
  @Override
  public PageResponse findAllByChannelId(UUID channelId, Instant cursor, Pageable pageable) {
    if (!channelRepository.existsById(channelId)) {
      log.warn("[Finding Messages Failed: Channel with id {} not found]", channelId);
      throw new ChannelNotFoundException(Instant.now(), Map.of("channelId", channelId));
    }
    Slice<Message> messageSlice;
    // 첫 요청이면 가장 최신 메시지부터 가져오기
    if (cursor == null) {
      messageSlice = messageRepository.findByChannelId(channelId, pageable);
    } else {
      // 커서(가장 마지막으로 조회한 메시지 작성 시간) 이전에 작성된 메시지 메시지 가져오기
      messageSlice = messageRepository.findByChannelIdAndCreatedAtLessThan(
          channelId, cursor, pageable);
    }
    List<Message> messages = messageSlice.getContent();
    List<MessageDto> messageDtos = messages.stream()
        .map(messageMapper::toDto)
        .toList();
    boolean hasNext = messageSlice.hasNext();
    int size = messages.size();
    Object nextCursor =
        (hasNext && !messages.isEmpty()) ? messages.get(messages.size() - 1).getCreatedAt() : null;
    return new PageResponse<>(messageDtos,
        nextCursor,
        size,
        hasNext,
        0);
  }

  @Transactional
  @Override
  public MessageDto update(UUID messageId, MessageUpdateRequest dto) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> {
          log.warn("[Updating Message Failed: Message with id {} not found]", messageId);
          return new MessageNotFoundException(Instant.now(), Map.of("messageId", messageId));
        });
    message.update(dto.newContent());
    return messageMapper.toDto(message);
  }

  @Override
  public void delete(UUID messageId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> {
          log.warn("[Deleting Message Failed: Message with id {} not found]", messageId);
          return new MessageNotFoundException(Instant.now(), Map.of("messageId", messageId));
        });
    messageRepository.delete(message);
  }

}
