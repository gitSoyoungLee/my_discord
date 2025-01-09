package discodeit.service.jcf;

import discodeit.enity.Channel;
import discodeit.enity.Message;
import discodeit.enity.User;
import discodeit.service.MessageService;
import discodeit.service.ServiceFactory;

import java.util.ArrayList;
import java.util.List;

public class JCFMessageService implements MessageService {

    private static volatile JCFMessageService instance;
    private final List<Message> data;

    private JCFMessageService() {
        this.data=new ArrayList<>();
    }

    public static JCFMessageService getInstance() {
        if(instance == null) {
            synchronized (JCFUserService.class) {
                if(instance == null) {
                    instance = new JCFMessageService();
                }
            }
        }
        return instance;
    }

    @Override
    public Message createMessage(User user, Channel channel, String content) {
        if(!channel.getUsers().contains(user)){
            System.out.println("메세지를 보낼 수 없습니다: " +
                    user.getName() + "은 아직 '" + channel.getName() + "' 채널에 입장하지 않았습니다.");
            return null;
        }
        Message message=new Message(user, channel, content);
        data.add(message);
        channel.getMessages().add(message);
        // [심화] 도메인 간 관계
        System.out.println("[심화] User: " + message.getSender().getName()
                + " Channel: " + message.getChannel().getName()
                + " Message: " + message.getContent());
        return message;
    }

    @Override
    public void viewMessage(Message message) {
        if(data.contains(message)){
            System.out.println( message.getChannel().getName() + " > " +
                    message.getSender().getName() + ": " +
                    message.getContent()
                    + " (시간: " + message.getUpdatedAt() + ")");
        } else {
            System.out.println("Error: 존재하지 않는 메시지");
        }
    }

    @Override
    public void viewAllMessages() {
        System.out.println("--- 디스코드잇에서 생성된 모든 메세지 ---");
        data.stream()
                .forEach( message -> {
                    System.out.println("채널 '" + message.getChannel().getName() + "' > "
                                        + message.getSender().getName() + ": "
                                        + message.getContent()
                                        + " (time: " + message.getUpdatedAt() + ")");
                });
    }

    @Override
    public void updateMessage(User user, Message message, String newContent) {
        try {
            // 메시지 작성자인 경우에만 수정 가능
            if(message.getSender()!=user) {
                throw new IllegalArgumentException("올바르지 않은 작성자");
            }
            message.updateContent(newContent);
            return;
        } catch (IllegalArgumentException e) {
            System.out.println("해당 메세지를 작성한 사용자가 아닙니다.");
            return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    @Override
    public void deleteMessage(Message message) {
        JCFChannelService jcfChannelService = ServiceFactory.getInstance().getJcfchannelService();
        jcfChannelService.deleteMessageInChannel(message);
        data.remove(message);
    }
}
