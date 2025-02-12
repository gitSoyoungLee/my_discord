package com.spirnt.mission.discodeit.service.facade;

import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentCreate;
import com.spirnt.mission.discodeit.dto.message.MessageCreateRequest;
import com.spirnt.mission.discodeit.dto.message.MessageResponse;
import com.spirnt.mission.discodeit.dto.message.MessageUpdateRequest;
import com.spirnt.mission.discodeit.enity.Channel;
import com.spirnt.mission.discodeit.enity.ChannelType;
import com.spirnt.mission.discodeit.enity.Message;
import com.spirnt.mission.discodeit.service.BinaryContentService;
import com.spirnt.mission.discodeit.service.ChannelService;
import com.spirnt.mission.discodeit.service.MessageService;
import com.spirnt.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageFacade {
    private final MessageService messageService;
    private final UserService userService;
    private final ChannelService channelService;
    private final BinaryContentService binaryContentService;

    public Message createMessage(MessageCreateRequest messageCreateRequest) {
        // User와 Channel이 존재하는지 검증
        if (!channelService.existsById(messageCreateRequest.getChannelId())) {
            throw new NoSuchElementException("Channel id " + messageCreateRequest.getChannelId() + " does not exist");
        }
        if (!userService.existsById(messageCreateRequest.getUserId())) {
            throw new NoSuchElementException("User id " + messageCreateRequest.getUserId() + " does not exist");
        }
        Channel channel = channelService.find(messageCreateRequest.getChannelId());
        // Private 채널은 입장한 경우에만 가능
        if (channel.getType() == ChannelType.PRIVATE &&
                !channel.getUsersId().contains(messageCreateRequest.getUserId())) {
            throw new IllegalStateException("User has not joined this private channel.");
        }
        // 메세지 생성
        Message message = messageService.create(messageCreateRequest);
        // 첨부 파일 업로드
        for (MultipartFile file : messageCreateRequest.getFiles()) {
            BinaryContentCreate binaryContentCreate = new BinaryContentCreate(messageCreateRequest.getUserId(), message.getId(), file);
            binaryContentService.create(binaryContentCreate);
        }
        return message;
    }

    public MessageResponse findMessage(UUID messageId) {
        Message message = messageService.find(messageId);
        return new MessageResponse(message);
    }

    public List<MessageResponse> findAllMessages() {
        List<Message> messages = messageService.findAll();
        return messages.stream()
                .map(message -> new MessageResponse(message))
                .collect(Collectors.toList());
    }

    public void updateMessage(UUID messageId, MessageUpdateRequest messageUpdateRequest) {
        messageService.update(messageId, messageUpdateRequest);
    }

    public void deleteMessage(UUID messageId) {
        // 첨부파일 삭제
        binaryContentService.deleteByMessageId(messageId);
        messageService.delete(messageId);
    }
}
