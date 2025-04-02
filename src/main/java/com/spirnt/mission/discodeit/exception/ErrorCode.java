package com.spirnt.mission.discodeit.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
  USER_NOT_FOUND("", "User를 찾을 수 없습니다."),
  DUPLICATE_USER("", "이메일 또는 이름이 중복됩니다."),
  INVALID_PASSWORD("", "비밀번호가 일치하지 않습니다."),

  CHANNEL_NOT_FOUND("", "Channel을 찾을 수 없습니다."),
  PRIVATE_CHANNEL_UPDATE("", "비공개 채널은 수정할 수 없습니다."),

  MESSAGE_NOT_FOUND("", "Message를 찾을 수 없습니다."),

  READSTATUS_NOT_FOUND("", "ReadStatus를 찾을 수 없습니다."),
  READSTATUS_ALREADY_EXISTS("", "이미 동일한 ReadStatus가 존재합니다."),

  USERSTATUS_NOT_FOUND("", "UserStatus를 찾을 수 없습니다."),
  USERSTATUS_ALREADY_EXISTS("", "이미 동일한 UserStatus가 존재합니다."),

  BINARYCONTENT_NOT_FOUND("", "BinaryContent를 찾을 수 없습니다."),
  FILE_ERROR("", "파일을 읽거나 쓰는 중 오류가 발생했습니다.");

  private final String code;
  private final String message;

  ErrorCode(String code, String message) {
    this.code = code;
    this.message = message;
  }


}
