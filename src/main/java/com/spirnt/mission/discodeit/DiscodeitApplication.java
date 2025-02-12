package com.spirnt.mission.discodeit;

import com.spirnt.mission.discodeit.dto.channel.ChannelCreateRequest;
import com.spirnt.mission.discodeit.dto.channel.ChannelResponse;
import com.spirnt.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.spirnt.mission.discodeit.dto.message.MessageCreateRequest;
import com.spirnt.mission.discodeit.dto.message.MessageResponse;
import com.spirnt.mission.discodeit.dto.message.MessageUpdateRequest;
import com.spirnt.mission.discodeit.dto.user.UserCreateRequest;
import com.spirnt.mission.discodeit.dto.user.UserResponse;
import com.spirnt.mission.discodeit.dto.user.UserUpdateRequest;
import com.spirnt.mission.discodeit.enity.BinaryContent;
import com.spirnt.mission.discodeit.enity.Channel;
import com.spirnt.mission.discodeit.enity.Message;
import com.spirnt.mission.discodeit.enity.User;
import com.spirnt.mission.discodeit.service.*;
import com.spirnt.mission.discodeit.service.facade.ChannelFacade;
import com.spirnt.mission.discodeit.service.facade.MessageFacade;
import com.spirnt.mission.discodeit.service.facade.UserFacade;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

@SpringBootApplication
public class DiscodeitApplication {

    static void clearDataFiles() {
        // 현재 작업 디렉토리에 있는 file-data-map 경로
        Path basePath = Paths.get(System.getProperty("user.dir"), "file-data-map");

        // 삭제할 폴더 목록
        List<String> folders = List.of("User", "Channel", "Message", "ReadStatus", "UserStatus", "BinaryContent");

        for (String folder : folders) {
            Path folderPath = basePath.resolve(folder);
            if (Files.exists(folderPath) && Files.isDirectory(folderPath)) {
                try (Stream<Path> files = Files.list(folderPath)) {
                    files
                            .filter(path -> path.toString().endsWith(".ser")) // .ser 파일만 선택
                            .forEach(path -> {
                                try {
                                    Files.delete(path);
                                } catch (IOException e) {
                                    System.out.println("파일 삭제 실패: " + path + " - " + e.getMessage());
                                }
                            });
                } catch (IOException e) {
                    System.out.println("폴더 접근 실패: " + folderPath + " - " + e.getMessage());
                }
            }
        }
    }

    static void clearDataFiles2() {
        // 현재 작업 디렉토리에 있는 file-data-map 경로
        Path basePath = Paths.get(System.getProperty("user.dir"), "file-data-map2");

        // 삭제할 폴더 목록
        List<String> folders = List.of("User", "Channel", "Message", "ReadStatus", "UserStatus", "BinaryContent");

        for (String folder : folders) {
            Path folderPath = basePath.resolve(folder);
            if (Files.exists(folderPath) && Files.isDirectory(folderPath)) {
                try (Stream<Path> files = Files.list(folderPath)) {
                    files
                            .filter(path -> path.toString().endsWith(".ser")) // .ser 파일만 선택
                            .forEach(path -> {
                                try {
                                    Files.delete(path);
                                } catch (IOException e) {
                                    System.out.println("파일 삭제 실패: " + path + " - " + e.getMessage());
                                }
                            });
                } catch (IOException e) {
                    System.out.println("폴더 접근 실패: " + folderPath + " - " + e.getMessage());
                }
            }
        }
    }

    static MultipartFile convertFileToMultipartFile(String filePath) throws IOException {
        File file = new File(filePath);
        FileInputStream input = new FileInputStream(file);

        return new MockMultipartFile(
                "file",              // 필드명
                file.getName(),      // 원본 파일명
                "image/jpeg",        // MIME 타입 (파일 확장자에 맞게 설정 가능)
                input                // 파일 데이터
        );
    }

    static void readUser(UserFacade userFacade, User user) {
        try {
            UserResponse userResponse = userFacade.findUser(user.getId());
            System.out.println(userResponse);
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }


    }

    static void readAllUsers(UserFacade userFacade) {
        List<UserResponse> list = userFacade.findAll();
        System.out.println("---- All Users ----");
        for (UserResponse userResponse : list) {
            System.out.println(userResponse);
        }
        System.out.println("-------------------");
    }

    static void readChannel(ChannelFacade channelFacade, Channel channel) {
        try {
            ChannelResponse channelResponse = channelFacade.findChannel(channel.getId());
            System.out.println(channelResponse);
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
    }

    static void readChannelByUser(ChannelFacade channelFacade, User user) {
        List<ChannelResponse> list = channelFacade.findAllChannelsByUserId(user.getId());
        System.out.println("---- All Channels that the user can view ----");
        for (ChannelResponse channelResponse : list) {
            System.out.println(channelResponse);
        }
        System.out.println("---------------------------------------------");
    }

    static void readMessage(MessageFacade messageFacade, Message message) {
        try {
            System.out.println(messageFacade.findMessage(message.getId()));
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
    }

    static void readAllMessages(MessageFacade messageFacade) {
        List<MessageResponse> list = messageFacade.findAllMessages();
        System.out.println("---- All Messages ----");
        for (MessageResponse messageResponse : list) {
            System.out.println(messageResponse);
        }
        System.out.println("----------------------");
    }

    public static void main(String[] args) {

        //SpringApplication.run(DiscodeitApplication.class, args);
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        // 서비스 초기화
        // TODO context에서 Bean을 조회하여 각 서비스 구현체 할당 코드 작성하세요.
        // + Facade class 이용하면 필요 x
        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);
        // Facade
        UserFacade userFacade = context.getBean(UserFacade.class);
        ChannelFacade channelFacade = context.getBean(ChannelFacade.class);
        MessageFacade messageFacade = context.getBean(MessageFacade.class);
        //
        ReadStatusService readStatusService = context.getBean(ReadStatusService.class);
        UserStatusService userStatusService = context.getBean(UserStatusService.class);
        BinaryContentService binaryContentService = context.getBean(BinaryContentService.class);

        // 스프린트 미션 3 테스트
        clearDataFiles();
        clearDataFiles2();

        // BinaryContent 테스트를 위한 MultipartFile 변환
        MultipartFile testImageFile1;
        try {
            testImageFile1 = convertFileToMultipartFile("testFiles/userprofile.jpg");
        } catch (IOException e) {
            e.printStackTrace();
            testImageFile1 = null;
        }
        MultipartFile testImageFile2;
        try {
            testImageFile2 = convertFileToMultipartFile("testFiles/sky.jpg");
        } catch (IOException e) {
            e.printStackTrace();
            testImageFile2 = null;
        }

        // User Create
        UserCreateRequest ucr1 = new UserCreateRequest("Alice", "Alice@gmail.com", "password", null);
        User user1 = userFacade.createUser(ucr1);
        UserCreateRequest ucr2 = new UserCreateRequest("Bob", "Bob@gmail.com", "password", testImageFile1);
        User user2 = userFacade.createUser(ucr2);
        // 중복 검증
        try {
            userFacade.createUser(new UserCreateRequest("Alice", "Alice@gmail.com", "password", null));
        } catch (IllegalArgumentException e) {
            System.out.println("Error occured while creating User: " + e.getMessage());
        }
        // User Read
        readUser(userFacade, user1);
        readUser(userFacade, user2);

        //User Update
        userFacade.updateUser(user1.getId(), new UserUpdateRequest("Andy", "Andy@gmail.com", "password", null));
        readUser(userFacade, user1);
        // User Delete
        userFacade.deleteUser(user2.getId());
        readAllUsers(userFacade);

        // Channel Create - PUBLIC
        ChannelCreateRequest ccr1 = new ChannelCreateRequest("Ch 1", "This is public", null);
        Channel channel1 = channelFacade.createChannelPublic(ccr1);
        // Channel Create - PRIVATE
        user2 = userFacade.createUser(ucr2);
        ChannelCreateRequest ccr2 = new ChannelCreateRequest("Ch 2 ", "This is private", List.of(user1.getId(), user2.getId()));
        Channel channel2 = channelFacade.createChannelPrivate(ccr2);
        ccr2 = new ChannelCreateRequest("Ch 3 ", "This is private", List.of(user2.getId()));
        Channel channel3 = channelFacade.createChannelPrivate(ccr2);

        // Channel Read
        readChannel(channelFacade, channel1);
        readChannel(channelFacade, channel2);
        readChannel(channelFacade, channel3);
        readChannelByUser(channelFacade, user1);

        // Channel Update
        ChannelUpdateRequest cur = new ChannelUpdateRequest("New Ch 1", "This is public!");
        channelFacade.updateChannel(channel1.getId(), cur);
        readChannel(channelFacade, channel1);

        // Channel Delete
        channelFacade.deleteChannel(channel3.getId());
        readChannel(channelFacade, channel3);

        // Message Create
        MessageCreateRequest mcr1 = new MessageCreateRequest(user1.getId(), channel1.getId(), "Hi Ch 1", null);
        Message message1 = messageFacade.createMessage(mcr1);
        // 입장하지 않은 private 채널에 메세지 생성 시도 시 예외 발생
        User user3 = userFacade.createUser(new UserCreateRequest("Cindy", "Cindy@gmail.com", "12345", null));
        try {
            MessageCreateRequest mcr2 = new MessageCreateRequest(user3.getId(), channel2.getId(), "This message will not be sent", null);
            messageFacade.createMessage(mcr2);
        } catch (IllegalStateException e) {
            System.out.println("Error occured while creating message: " + e.getMessage());
        }
        mcr1 = new MessageCreateRequest(user2.getId(), channel2.getId(), "Hi Ch 2", List.of(testImageFile1));
        Message message2 = messageFacade.createMessage(mcr1);

        // Message Read
        readMessage(messageFacade, message1);
        readAllMessages(messageFacade);

        // Message Update
        messageFacade.updateMessage(message1.getId(), new MessageUpdateRequest("New Message"));
        readMessage(messageFacade, message1);

        // Message Delete
        messageFacade.deleteMessage(message2.getId());
        readAllMessages(messageFacade);

        // UserStatus
        System.out.println(userStatusService.findByUserId(user1.getId()));
        System.out.println(userStatusService.findByUserId(user2.getId()));

        // ReadStatus
        System.out.println(readStatusService.findAllByUserId(user1.getId()));
        System.out.println(readStatusService.findAllByUserId(user2.getId()));

        //BinaryContent
        //User2 프로필이미지 확인
        System.out.println("User2's profile image: ");
        System.out.println(binaryContentService.findUserProfile(user2.getId()));
        //Message3 첨부파일 확인
        System.out.println("Attached Files in Message 3:");
        Message message3 = messageFacade.createMessage(new MessageCreateRequest(user1.getId(),
                channel1.getId(), "Files", List.of(testImageFile1, testImageFile2)));
        List<BinaryContent> message3files = binaryContentService.findByMessageId(message3.getId());
        for (BinaryContent file : message3files) {
            System.out.println(file);
        }
    }

}
