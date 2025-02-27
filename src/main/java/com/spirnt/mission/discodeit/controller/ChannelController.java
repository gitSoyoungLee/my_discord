package com.spirnt.mission.discodeit.controller;

import com.spirnt.mission.discodeit.dto.channel.ChannelDto;
import com.spirnt.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.spirnt.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.spirnt.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.spirnt.mission.discodeit.enity.Channel;
import com.spirnt.mission.discodeit.service.ChannelService;
import com.spirnt.mission.discodeit.swagger.ChannelApiDocs;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController implements ChannelApiDocs {

  private final ChannelService channelService;

  // 공개 채널 생성
  @PostMapping("/public")
  public ResponseEntity<Channel> creatChannelPublic(
      @RequestBody PublicChannelCreateRequest request) {
    Channel channel = channelService.createChannelPublic(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(channel);
  }

  // 비공개 채널 생성
  @PostMapping("/private")
  public ResponseEntity<Channel> createChannelPrivate(
      @RequestBody PrivateChannelCreateRequest request) {
//    if (request.getUsersId().isEmpty()) {
//      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//          .body(new ErrorResponse("There are no participants."));
//    }
    Channel channel = channelService.createChannelPrivate(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(channel);
  }

  // 공개 채널 정보 수정
  @PatchMapping("/{channelId}")
  public ResponseEntity<Channel> updatePublicChannel(@PathVariable UUID channelId,
      @RequestBody ChannelUpdateRequest request) {
//    if (request.getName() == null && request.getDescription() == null) {
//      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//          .body(new ErrorResponse("You should enter at least one element to update."));
//    }
    Channel channel = channelService.update(channelId, request);
    return ResponseEntity.ok(channel);
  }

  // 채널 삭제
  @DeleteMapping("/{channelId}")
  public ResponseEntity<?> deleteChannel(@PathVariable UUID channelId) {
    channelService.delete(channelId);
    return ResponseEntity.ok().build();
  }


  // 특정 사용자가 볼 수 있는 채널 목록 조회
  @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
  public ResponseEntity<List<ChannelDto>> getAllChannelsByUserId(@PathVariable UUID userId) {
    List<ChannelDto> channelResponses = channelService.findAllByUserId(userId);
    return ResponseEntity.ok(channelResponses);
  }

}
