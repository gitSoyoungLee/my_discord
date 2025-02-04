package discodeit.service.file;

import discodeit.dto.MessageDto;
import discodeit.enity.Channel;
import discodeit.enity.Message;
import discodeit.enity.User;
import discodeit.repository.file.FileMessageRepository;
import discodeit.service.MessageService;

import java.util.*;
import java.util.stream.Collectors;

public class FileMessageService implements MessageService {
    //싱글톤
    private static volatile FileMessageService instance;
    private final FileMessageRepository fileMessageRepository;
    private FileUserService fileUserService;
    private FileChannelService fileChannelService;

    public FileMessageService() {
        this.fileMessageRepository = new FileMessageRepository();
    }

    public static FileMessageService getInstance() {
        if (instance == null) {
            synchronized (FileMessageService.class) {
                if (instance == null) {
                    instance = new FileMessageService();
                }
            }
        }
        return instance;
    }

    protected void setService() {
        this.fileUserService = fileUserService.getInstance();
        this.fileChannelService = fileChannelService.getInstance();
    }

    @Override
    public UUID createMessage(UUID userId, UUID channelId, String content) {
        // 존재하는지 검증
        Channel channel = fileChannelService.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel ID: " + channelId + " Not Found"));
        User user = fileUserService.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User ID: " + userId + " Not Found"));

        if (!channel.getUsers().contains(userId)) {
            System.out.println("메세지를 보낼 수 없습니다: " +
                    user.getName() + "은 아직 '" + channel.getName() + "' 채널에 입장하지 않았습니다.");
            return null;
        }
        Message message = new Message(userId, channelId, content);
        fileMessageRepository.save(message);
        System.out.println("메세지 전송 완료: " + message.getContent());
        return message.getId();
    }

    @Override
    public MessageDto getMessageById(UUID messageId) {
        Message message = findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message ID: "+messageId+" Not Found"));
        return new MessageDto(message);
    }

    @Override
    public List<MessageDto> getAllMessages() {
        Map<UUID, Message> data = fileMessageRepository.findAll();
        if (data == null || data.isEmpty()) {
            System.out.println("메세지가 없습니다.");
            return null;
        }
        List<MessageDto> list = new ArrayList<>();
        data.values().stream()
                .sorted(Comparator.comparing(message-> message.getCreatedAt()))
                .forEach(message -> {
                    list.add(new MessageDto(message));
                });
        return list;
    }


    @Override
    public void updateMessage(UUID userId, UUID messageId, String newContent) {
        Message message = findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message ID: "+messageId+" Not Found"));
        // 메시지 작성자인 경우에만 수정 가능
        if (!message.getSenderId().equals(userId)) {
            System.out.println("메세지 작성자만 수정 가능합니다.");
            return;
        }
        message.updateContent(newContent);
        // 객체 수정 후 저장
        fileMessageRepository.save(message);
    }

    @Override
    public void deleteMessage(UUID messageId) {
        Message message = findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message ID: "+messageId+" Not Found"));
        fileMessageRepository.delete(messageId);
    }

    @Override
    public Optional<Message> findById(UUID messageId) {
        return fileMessageRepository.findById(messageId);
    }


    // 해당 채널에 쓰여진 메세지들을 모두 삭제
    @Override
    public void deleteMessagesInChannel(UUID channelId) {
        Map<UUID, Message> data = fileMessageRepository.findAll();
        if (data == null) return;
        data.values().stream()
                .forEach(message -> {
                    // 해당 채널에 쓰여진 메세지 객체를 찾고
                    if (message.getChannelId().equals(channelId)) {
                        // 그 객체를 레포지토리에서 삭제
                        fileMessageRepository.delete(message.getId());
                    }
                });
    }
}
