package com.spirnt.mission.discodeit;

import com.spirnt.mission.discodeit.dto.ChannelDto;
import com.spirnt.mission.discodeit.dto.MessageDto;
import com.spirnt.mission.discodeit.dto.UserDto;
import com.spirnt.mission.discodeit.enity.ChannelType;
import com.spirnt.mission.discodeit.repository.ChannelRepository;
import com.spirnt.mission.discodeit.repository.MessageRepository;
import com.spirnt.mission.discodeit.repository.UserRepository;
import com.spirnt.mission.discodeit.repository.file.FileChannelRepository;
import com.spirnt.mission.discodeit.repository.file.FileMessageRepository;
import com.spirnt.mission.discodeit.repository.file.FileUserRepository;
import com.spirnt.mission.discodeit.service.ChannelService;
import com.spirnt.mission.discodeit.service.MessageService;
import com.spirnt.mission.discodeit.service.ServiceFactory;
import com.spirnt.mission.discodeit.service.UserService;
import com.spirnt.mission.discodeit.service.basic.BasicChannelService;
import com.spirnt.mission.discodeit.service.basic.BasicMessageService;
import com.spirnt.mission.discodeit.service.basic.BasicUserService;
import com.spirnt.mission.discodeit.service.file.FileServiceFactory;
import com.spirnt.mission.discodeit.service.file.FileUserService;
import com.spirnt.mission.discodeit.service.jcf.JCFServiceFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class DiscodeitApplication {

	static public void clearDataFiles() {
		// 현재 작업 디렉토리에 있는 file-data-map 경로
		Path basePath = Paths.get(System.getProperty("user.dir"), "file-data-map");

		// 삭제할 폴더 목록
		List<String> folders = List.of("User", "Channel", "Message");

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

	static void printUserInfo(UUID userId, UserService userService) {
		System.out.println("--- UUID로 사용자 조회 ---");
		try {
			UserDto userInfoDto = userService.getUserInfoById(userId);
			System.out.println(userInfoDto);
		} catch (NoSuchElementException e) {
			System.out.println(e.getMessage());
		}
	}

	static void printAllUserInfo(UserService userService) {
		System.out.println("--- 전체 사용자 조회 ---");
		List<UserDto> userDtoList = userService.getAllUsersInfo();
		if (userDtoList.isEmpty()) {
			System.out.println("사용자가 없습니다.");
			return;
		}
		userDtoList.stream()
				.forEach(System.out::println);
	}

	static void printChannelInfo(UUID channelId, ChannelService channelService) {
		System.out.println("--- UUID로 특정 채널 조회 ---");
		try {
			ChannelDto channelDto = channelService.getChannelInfoById(channelId);
			System.out.println(channelDto);
		} catch (NoSuchElementException e) {
			System.out.println(e.getMessage());
		}
	}

	static void printAllChannelInfo(ChannelService channelService) {
		System.out.println("--- 전체 채널 조회 ---");
		List<ChannelDto> channelDtoList = channelService.getAllChannelsInfo();
		if (channelDtoList.isEmpty()) {
			System.out.println("채널이 없습니다.");
			return;
		}
		channelDtoList.stream()
				.forEach(System.out::println);
	}

	static void printMessageInfo(UUID messageId, MessageService messageService) {
		System.out.println("--- UUID로 특정 메세지 조회 ---");
		try {
			MessageDto messageDto = messageService.getMessageById(messageId);
			System.out.println(messageDto);
		} catch (NoSuchElementException e) {
			System.out.println(e.getMessage());
		}
	}

	static void printAllMessageInfo(MessageService messageService) {
		System.out.println("--- 디스코드잇에서 작성된 모든 메세지 조회 ---");
		List<MessageDto> messageList = messageService.getAllMessages();
		if (messageList.isEmpty()) {
			System.out.println("메세지가 없습니다.");
			return;
		}
		messageList.stream()
				.forEach(System.out::println);
	}


	static void testSprint2Advanced() {
		// JCF
//        BasicUserService userService = new BasicUserService(new JCFUserRepository());
//        BasicChannelService channelService = new BasicChannelService(new JCFChannelRepository());
//        BasicMessageService messageService = new BasicMessageService(new JCFMessageRepository());
		// File
		BasicUserService userService = new BasicUserService(new FileUserRepository());
		BasicChannelService channelService = new BasicChannelService(new FileChannelRepository());
		BasicMessageService messageService = new BasicMessageService(new FileMessageRepository());
		userService.setService(channelService, messageService);
		channelService.setService(userService, messageService);
		messageService.setService(userService, channelService);

		//유저 등록
		System.out.println("==== Create User ====");
		UUID user1Id = userService.createUser("Alice@gmail.com", "Alice", "12345");
		UUID user2Id = userService.createUser("Bob@gmail.com", "Bob", "12345");
		UUID user3Id = userService.createUser("Cindy@gmail.com", "Cindy", "12345");
		UUID user4Id = userService.createUser("Dan@gmail.com", "Dan", "12345");
		UUID user5Id = userService.createUser("Edward@gmail.com", "Edward", "12345");
		UUID user6Id = userService.createUser("Felix@gmail.com", "felix", "12345");
		System.out.println("// 이메일 중복 검사 확인");
		userService.createUser("Alice@gmail.com", "Alice", "12345");
		System.out.println("// 비밀번호 형식 검사 확인");
		userService.createUser("Gary@gmail.com", "Gary", "1234");
		System.out.println("====================\n\n");

		System.out.println("==== Read User ====");
		//유저 단건 조회
		printUserInfo(user1Id, userService);
		//유저 다건 조회
		printAllUserInfo(userService);
		System.out.println("====================\n\n");


		// 유저 수정
		System.out.println("==== Update User ====");
		System.out.println("// 이름과 이메일 변경");
		userService.updateUserName(user1Id, "Andy");
		userService.updateUserEmail(user1Id, "Andy@gmail.com");
		System.out.println("// 이미 존재하는 이메일로 바꾸려는 경우");
		userService.updateUserEmail(user1Id, "Bob@gmail.com");
		System.out.println("// 수정 후 전체 사용자 조회");
		printAllUserInfo(userService);
		System.out.println("====================\n\n");

		// 유저 삭제
		System.out.println("==== Delete User ====");
		userService.deleteUser(user6Id);
		System.out.println("// 삭제 후 전체 사용자 조회");
		printAllUserInfo(userService);
		System.out.println("// 삭제된 유저 정보 조회 시도: ");
		printUserInfo(user6Id, userService);
		System.out.println("====================\n\n");

		//채널 등록
		System.out.println("==== Create Channel ====");
		UUID channel1 = channelService.createChannel("채널 1", "첫 번째 채널", ChannelType.PUBLIC);
		UUID channel2 = channelService.createChannel("채널 2", "두 번째 채널", ChannelType.PUBLIC);
		UUID channel3 = channelService.createChannel("채널 3", "세 번째 채널", ChannelType.PUBLIC);
		UUID channel4 = channelService.createChannel("채널 4", "네 번째 채널", ChannelType.PUBLIC);
		System.out.println("====================\n\n");

		System.out.println("==== Read Channel ====");
		printChannelInfo(channel1, channelService);
		printAllChannelInfo(channelService);
		System.out.println("====================\n\n");

		//채널 수정
		System.out.println("==== Update Channel ====");
		System.out.println("// 채널 이름 변경");
		channelService.updateChannelName(channel1, "채널 11");
		// 채널에 유저 등록
		System.out.println("// 채널에 유저 입장");
		channelService.addUserIntoChannel(channel1, user1Id);
		channelService.addUserIntoChannel(channel1, user2Id);
		channelService.addUserIntoChannel(channel1, user3Id);
		channelService.addUserIntoChannel(channel1, user4Id);
		System.out.println("// 채널에 유저 입장시킨 후 채널 정보 조회");
		printAllChannelInfo(channelService);
		System.out.println("--- 채널 정보 조회 ---");
		printChannelInfo(channel1, channelService);
		System.out.println("// 유저 정보 조회 시에도 채널 목록이 출력되는지 확인");
		printUserInfo(user1Id, userService);
		System.out.println("//채널에 유저 퇴장시킨 후 채널 정보 조회");
		//채널에서 유저 삭제
		channelService.deleteUserInChannel(channel1, user4Id);
		printChannelInfo(channel1, channelService);
		//유저 삭제 시 채널에서도 함께 삭제
		System.out.println("// 사용자 삭제 시 채널에서도 정보 삭제");
		System.out.println("// 삭제 전 채널에 입장");
		channelService.addUserIntoChannel(channel1, user4Id);
		printChannelInfo(channel1, channelService);
		userService.deleteUser(user4Id);
		System.out.println("// 삭제 후");
		printChannelInfo(channel1, channelService);
		System.out.println("====================\n\n");

		//채널 삭제
		System.out.println("==== Delete Channel ====");
		channelService.addUserIntoChannel(channel4, user1Id);
		channelService.addUserIntoChannel(channel4, user2Id);
		channelService.deleteChannel(channel4);
		System.out.println("// 채널 삭제 후 전체 채널 목록 조회");
		printAllChannelInfo(channelService);
		System.out.println("// 삭제한 채널 정보 조회: 채널 정보 조회가 불가능한지 확인");
		printChannelInfo(channel4, channelService);
		System.out.println("// 사용자 정보 조회 시에도 채널이 안 나오는지 확인");
		printUserInfo(user1Id, userService);
		printUserInfo(user2Id, userService);
		System.out.println("====================\n\n");


		// 메시지 등록(전송)
		System.out.println("==== Create Message ====");
		UUID message1 = messageService.createMessage(user1Id, channel1, "안녕하세요");
		UUID message2 = messageService.createMessage(user1Id, channel1, "저는 Andy입니다.");
		UUID message3 = messageService.createMessage(user2Id, channel1, "반가워요 Andy.");
		System.out.println("// 유저가 입장하지 않은 채널에 메세지를 전송하려는 경우");
		UUID message4 = messageService.createMessage(user5Id, channel1, "아직 입장하지 않은 채널");
		System.out.println("====================\n\n");

		System.out.println("==== Read Message ====");
		System.out.println("// 채널에 메세지가 작성되었는지 확인");
		printChannelInfo(channel1, channelService);
		System.out.println("// UUID로 특정 메세지 단건 조회");
		// 메시지 단건 조회
		printMessageInfo(message1, messageService);
		printMessageInfo(message2, messageService);
		// 메시지 다건 조회
		printAllMessageInfo(messageService);
		System.out.println("// 메세지 추가 후 확인");
		channelService.addUserIntoChannel(channel2, user1Id);
		channelService.addUserIntoChannel(channel2, user2Id);
		channelService.addUserIntoChannel(channel2, user3Id);
		UUID message5 = messageService.createMessage(user1Id, channel2, "채널 2 시작");
		UUID message6 = messageService.createMessage(user2Id, channel2, "안녕하세요.");
		UUID message7 = messageService.createMessage(user3Id, channel2, "채널 2는 무엇을 하는 채널인가요?");
		printAllMessageInfo(messageService);
		System.out.println("====================\n\n");

		System.out.println("==== Update Message ====");
		//메시지 수정
		System.out.println("// 메세지 작성자가 아닌 다른 유저가 수정하려는 경우");
		messageService.updateMessage(user2Id, message1, "잘못된 작성자");
		messageService.updateMessage(user1Id, message1, "수정) 안녕하세요 여러분");
		System.out.println("// 메세지 수정 후 확인");
		System.out.println(messageService.getMessageById(message1));
		System.out.println("// 메세지 수정 후 소속 채널에 반영됐는지 확인");
		printChannelInfo(channel1, channelService);
		printAllMessageInfo(messageService);
		System.out.println("====================\n\n");

		System.out.println("==== Delete Message ====");
		// 메시지 삭제
		System.out.println("// 메세지 삭제 전 채널 조회");
		printChannelInfo(channel1, channelService);
		messageService.deleteMessage(message1);
		System.out.println("// 메세지 삭제 후 채널 조회");
		printChannelInfo(channel1, channelService);
		printAllMessageInfo(messageService);
		//메세지 삭제 후 메세지 조회
		printMessageInfo(message1, messageService);
		System.out.println("====================\n\n");


		System.out.println("--- 유저 삭제/퇴장 시 메세지 표시 ---");
		System.out.println("// Bob 유저 삭제 시");
		userService.deleteUser(user2Id);
		printAllMessageInfo(messageService);
		System.out.println("// Ch 2에서 Cindy 유저 퇴장 시");
		channelService.deleteUserInChannel(channel2, user3Id);
		printChannelInfo(channel2, channelService);
	}

	public static void main(String[] args) {

		// SpringApplication.run(DiscodeitApplication.class, args);
		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

		// 서비스 초기화
		// TODO context에서 Bean을 조회하여 각 서비스 구현체 할당 코드 작성하세요.
		UserService userService = context.getBean(UserService.class);
		ChannelService channelService = context.getBean(ChannelService.class);
		MessageService messageService = context.getBean(MessageService.class);
		//레포지토리 할당
		userService.setUserRepository(context.getBean(UserRepository.class));
		channelService.setChannelRepository(context.getBean(ChannelRepository.class));
		messageService.setMessageRepository(context.getBean(MessageRepository.class));
		//서비스 의존성
		userService.setService(channelService, messageService);
		channelService.setService(userService, messageService);
		messageService.setService(userService, channelService);

		// 스프린트 미션 3 기본 요구사항 테스트
		clearDataFiles();
		UUID user = userService.createUser("Alice@gmail.com", "Alice", "password");
		UUID channel = channelService.createChannel("Ch 1", "This is Ch 1", ChannelType.PUBLIC);
		channelService.addUserIntoChannel(channel, user);
		UUID message = messageService.createMessage(user, channel, "Hello World");
		printMessageInfo(message, messageService);

	}

}
