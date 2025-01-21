package discodeit;

import discodeit.enity.ChannelType;
import discodeit.repository.file.FileChannelRepository;
import discodeit.repository.file.FileMessageRepository;
import discodeit.repository.file.FileUserRepository;
import discodeit.repository.jcf.JCFChannelRepository;
import discodeit.repository.jcf.JCFMessageRepository;
import discodeit.repository.jcf.JCFUserRepository;
import discodeit.service.ServiceFactory;
//import discodeit.service.jcf.JCFChannelService;
//import discodeit.service.jcf.JCFMessageService;
import discodeit.service.basic.BasicChannelService;
import discodeit.service.basic.BasicMessageService;
import discodeit.service.basic.BasicUserService;
import discodeit.service.file.FileChannelService;
import discodeit.service.file.FileMessageService;
import discodeit.service.file.FileUserService;
import discodeit.service.jcf.JCFChannelService;
import discodeit.service.jcf.JCFMessageService;
import discodeit.service.jcf.JCFUserService;

import java.io.File;
import java.util.UUID;

public class javaApplication {
    static void testSprint1() {
        ServiceFactory serviceFactory = new ServiceFactory();
        JCFUserService jcfUserService = serviceFactory.getJcfUserService();
        JCFChannelService jcfChannelService = serviceFactory.getJcfchannelService();
        JCFMessageService jcfMessageService = serviceFactory.getJcfMessageService();

        //유저 등록
        System.out.println("==== Create User ====");
        UUID user1 = jcfUserService.createUser("Alice@gmail.com", "Alice", "12345");
        UUID user2 = jcfUserService.createUser("Bob@gmail.com", "Bob", "12345");
        UUID user3 = jcfUserService.createUser("Cindy@gmail.com", "Cindy", "12345");
        UUID user4 = jcfUserService.createUser("Dan@gmail.com", "Dan", "12345");
        UUID user5 = jcfUserService.createUser("Edward@gmail.com", "Edward", "12345");
        UUID user6 = jcfUserService.createUser("Felix@gmail.com", "felix", "12345");
        jcfUserService.createUser("Alice@gmail.com", "Alice", "12345");
        jcfUserService.createUser("Gary@gmail.com", "Gary", "1234");
        System.out.println("====================\n\n");

        System.out.println("==== Read User ====");
        //유저 다건 조회
        jcfUserService.viewAllUser();
        System.out.println();
        //유저 단건 조회
        jcfUserService.viewUserInfo(user1);
        System.out.println("====================\n\n");

        // 유저 수정
        System.out.println("==== Update User ====");
        jcfUserService.updateUserName(user1, "Andy");
        jcfUserService.updateUserEmail(user1, "Andy@gmail.com");
        jcfUserService.updateUserEmail(user1, "Bob@gmail.com");
        System.out.println();
        jcfUserService.viewAllUser();
        System.out.println("====================\n\n");

        // 유저 삭제
        System.out.println("==== Delete User ====");
        jcfUserService.deleteUser(user6);
        System.out.println();
        jcfUserService.viewAllUser();
        System.out.println();
        System.out.println("// test: 삭제된 유저 정보 조회 시도: ");
        jcfUserService.viewUserInfo(user6);
        System.out.println("====================\n\n");

        //채널 등록
        System.out.println("==== Create Channel ====");
        UUID channel1 = jcfChannelService.createChannel("채널 1", "첫 번째 채널", ChannelType.PUBLIC);
        UUID channel2 = jcfChannelService.createChannel("채널 2", "두 번째 채널", ChannelType.PUBLIC);
        UUID channel3 = jcfChannelService.createChannel("채널 3", "세 번째 채널", ChannelType.PUBLIC);
        UUID channel4 = jcfChannelService.createChannel("채널 4", "네 번째 채널", ChannelType.PUBLIC);
        System.out.println("====================\n\n");

        //채널 다건 조회
        System.out.println("==== Read Channel ====");
        jcfChannelService.viewAllChannels();
        System.out.println();
        //채널 단건 조회
        jcfChannelService.viewChannelInfo(channel1);
        System.out.println("====================\n\n");

        //채널 수정
        System.out.println("==== Update Channel ====");
        jcfChannelService.updateChannelName(channel1, "채널 11");
        // 채널에 유저 등록
        jcfChannelService.addUserIntoChannel(channel1, user1);
        jcfChannelService.addUserIntoChannel(channel1, user2);
        jcfChannelService.addUserIntoChannel(channel1, user3);
        jcfChannelService.addUserIntoChannel(channel1, user4);
        jcfChannelService.viewAllChannels();
        jcfChannelService.viewChannelInfo(channel1);
        System.out.println();
        jcfUserService.viewUserInfo(user1);
        //채널에서 유저 삭제
        jcfChannelService.deleteUserInChannel(channel1, user4);
        System.out.println();
        jcfChannelService.viewChannelInfo(channel1);
        System.out.println();
        //유저 삭제 시 채널에서도 함께 삭제
        System.out.println("// test: 사용자 삭제 시 채널에서도 정보 삭제");
        jcfChannelService.addUserIntoChannel(channel1, user4);
        jcfChannelService.viewChannelInfo(channel1);
        jcfUserService.deleteUser(user4);
        jcfChannelService.viewChannelInfo(channel1);
        System.out.println("====================\n\n");


        //채널 삭제
        System.out.println("==== Delete Channel ====");
        jcfChannelService.addUserIntoChannel(channel4, user1);
        jcfChannelService.addUserIntoChannel(channel4, user2);
        jcfChannelService.deleteChannel(channel4);
        jcfChannelService.viewAllChannels();
        System.out.println("\n//test: 삭제한 채널 정보 조회:");
        jcfChannelService.viewChannelInfo(channel4);
        jcfUserService.viewUserInfo(user1);
        jcfUserService.viewUserInfo(user2);
        System.out.println("====================\n\n");


        // 메시지 등록(전송)
        System.out.println("==== Create Message ====");
        UUID message1 = jcfMessageService.createMessage(user1, channel1, "안녕하세요");
        UUID message2 = jcfMessageService.createMessage(user1, channel1, "저는 Andy입니다.");
        UUID message3 = jcfMessageService.createMessage(user2, channel1, "반가워요 Andy.");
        UUID message4 = jcfMessageService.createMessage(user5, channel1, "아직 입장하지 않은 채널");
        System.out.println();
        jcfChannelService.viewChannelInfo(channel1);
        System.out.println("====================\n\n");


        System.out.println("==== Read Message ====");
        System.out.println("//test: 메세지 단건 조회");
        // 메시지 단건 조회
        jcfMessageService.viewMessage(message1);
        System.out.println();
        // 메시지 다건 조회
        jcfChannelService.addUserIntoChannel(channel2, user1);
        jcfChannelService.addUserIntoChannel(channel2, user2);
        jcfChannelService.addUserIntoChannel(channel2, user3);
        UUID message5 = jcfMessageService.createMessage(user1, channel2, "채널 2 시작");
        UUID message6 = jcfMessageService.createMessage(user2, channel2, "안녕하세요.");
        UUID message7 = jcfMessageService.createMessage(user3, channel2, "채널 2는 무엇을 하는 채널인가요?");
        System.out.println();
        jcfMessageService.viewAllMessages();
        System.out.println("====================\n\n");

        System.out.println("==== Update Message ====");
        //메시지 수정
        jcfMessageService.updateMessage(user2, message1, "잘못된 작성자");
        jcfMessageService.updateMessage(user1, message1, "수정) 안녕하세요 여러분");
        jcfMessageService.viewMessage(message1);
        System.out.println();
        jcfChannelService.viewChannelInfo(channel1);
        System.out.println();
        jcfMessageService.viewAllMessages();
        System.out.println("====================\n\n");

        System.out.println("==== Delete Message ====");
        // 메시지 삭제
        jcfChannelService.viewChannelInfo(channel1);
        System.out.println();
        jcfMessageService.deleteMessage(message1);
        System.out.println();
        jcfChannelService.viewChannelInfo(channel1);
        System.out.println();
        jcfMessageService.viewAllMessages();
        System.out.println("====================\n\n");


        System.out.println("--- 유저 삭제 시 메세지 표시 ---");
        jcfUserService.deleteUser(user2);
        jcfChannelService.deleteUserInChannel(channel2, user3);
        jcfChannelService.viewChannelInfo(channel2);
        System.out.println();
    }

    static void testSprint2() {
        // 서비스 생성
        FileUserService fileUserService = new FileUserService();
        FileChannelService fileChannelService = new FileChannelService();
        FileMessageService fileMessageService = new FileMessageService();
        // 서비스 간 의존성 주입
        fileUserService.setService(fileChannelService, fileMessageService);
        fileChannelService.setService(fileUserService, fileMessageService);
        fileMessageService.setService(fileUserService, fileChannelService);


        // Create User
        UUID user1 = fileUserService.createUser("Alice@gmail.com", "Alice", "12345");
        fileUserService.createUser("Alice@gmail.com", "Alice", "12345");
        UUID user2 = fileUserService.createUser("Bob@gmail.com", "Bob", "12345");
        UUID user3 = fileUserService.createUser("Cindy@gmail.com", "Cindy", "12345");
        // Read User
        fileUserService.viewAllUser();
        fileUserService.viewUserInfo(user1);
        fileUserService.viewUserInfo(user2);
        // Update User
        fileUserService.updateUserName(user1, "Andy");
        fileUserService.updateUserEmail(user1, "Andy@gmail.com");
        fileUserService.updateUserPassword(user1, "123456");
        fileUserService.viewUserInfo(user1);
        // Delete User
        fileUserService.deleteUser(user3);
        fileUserService.viewAllUser();
        fileUserService.viewUserInfo(user3);

        // Create Channel
        UUID channel1 = fileChannelService.createChannel("Ch 1", "채널 1입니다.", ChannelType.PUBLIC);
        UUID channel2 = fileChannelService.createChannel("Ch 2", "채널 2입니다.", ChannelType.PUBLIC);
        UUID channel3 = fileChannelService.createChannel("Ch 3", "채널 3입니다.", ChannelType.PUBLIC);
        // Read Channel
        fileChannelService.viewAllChannels();
        fileChannelService.viewChannelInfo(channel1);
        // Update Channel
        fileChannelService.updateChannelName(channel2, "New Ch 2");
        fileChannelService.updateChannelDescription(channel2, "새로워진 채널 2입니다.");
        fileChannelService.viewChannelInfo(channel2);
        // Delete Channel
        fileChannelService.deleteChannel(channel3);
        fileChannelService.viewAllChannels();
        fileChannelService.viewChannelInfo(channel3);
        // Add user into channel
        fileChannelService.addUserIntoChannel(channel1, user1);
        fileChannelService.viewChannelInfo(channel1);
        fileUserService.viewUserInfo(user1);
        // Delete User from channel
        fileChannelService.deleteUserInChannel(channel1, user1);
        fileChannelService.viewChannelInfo(channel1);
        fileUserService.viewUserInfo(user1);

        // Create Message
        fileChannelService.addUserIntoChannel(channel1, user1);
        UUID message1=fileMessageService.createMessage(user1, channel1, "안녕하세요");
        // Read Message
        fileMessageService.viewMessage(message1);
        UUID message2=fileMessageService.createMessage(user1, channel1, "만나서 반갑습니다");
        UUID message3=fileMessageService.createMessage(user1, channel1, "잘 부탁해요");
        fileMessageService.viewAllMessages();
        // Update Message
        fileMessageService.updateMessage(user1, message1, "수정) 안녕하세요 여러분");
        fileMessageService.viewMessage(message1);
        // Delete Message
        fileMessageService.deleteMessage(message3);
        fileMessageService.viewAllMessages();

    }

    static void testSprint2Advanced() {
//        BasicUserService userService = new BasicUserService(new JCFUserRepository());
        BasicUserService userService = new BasicUserService(new FileUserRepository());
        UUID user1 = userService.createUser("Alice@gmail.com", "Alice", "12345");
        UUID user2 = userService.createUser("Bob@gmail.com", "Bob", "12345");
        userService.viewAllUser();
        userService.viewUserInfo(user1);
        userService.updateUserEmail(user1,"Andy@gmail.com");
        userService.updateUserName(user1,"Andy");
        userService.viewUserInfo(user1);
        userService.deleteUser(user2);
        userService.viewAllUser();
//        BasicChannelService channelService = new BasicChannelService(new JCFChannelRepository());
        BasicChannelService channelService = new BasicChannelService(new FileChannelRepository());
        UUID channel1 = channelService.createChannel("Ch 1", "This is Ch 1", ChannelType.PUBLIC);
        UUID channel2=channelService.createChannel("Ch 2", "This is Ch 2", ChannelType.PUBLIC);
        channelService.viewAllChannels();
        channelService.viewChannelInfo(channel1);
        channelService.updateChannelName(channel1, "New Ch 1");
        channelService.viewChannelInfo(channel1);
        channelService.deleteChannel(channel1);
        channelService.viewAllChannels();
//        BasicMessageService messageService = new BasicMessageService(new JCFMessageRepository());
        BasicMessageService messageService = new BasicMessageService(new FileMessageRepository());
        UUID message1 = messageService.createMessage(user1, channel2, "Hello,");
        UUID message2 = messageService.createMessage(user1, channel2, "World!");
        messageService.viewAllMessages();
        messageService.viewMessage(message1);
        messageService.updateMessage(user1, message1, "hello");
        messageService.deleteMessage(message2);
        messageService.viewAllMessages();
    }
    public static void main(String[] args) {

        //testSprint1();

        String[] filesToDelete = {"user.ser", "channel.ser", "message.ser"};
        for (String fileName : filesToDelete) {
            File file = new File(fileName);
            if (file.exists()) {
                file.delete();
            }
        }

        //testSprint2();

        testSprint2Advanced();

    }
}
