package com.spirnt.mission.discodeit.exception;

import com.spirnt.mission.discodeit.dto.ErrorResponse;
import com.spirnt.mission.discodeit.exception.BinaryContent.BinaryContentNotFoundException;
import com.spirnt.mission.discodeit.exception.BinaryContent.FileException;
import com.spirnt.mission.discodeit.exception.Channel.ChannelNotFoundException;
import com.spirnt.mission.discodeit.exception.Channel.PrivateChannelUpdateException;
import com.spirnt.mission.discodeit.exception.Message.MessageNotFoundException;
import com.spirnt.mission.discodeit.exception.ReadStatus.ReadStatusAlreadyExistException;
import com.spirnt.mission.discodeit.exception.ReadStatus.ReadStatusNotFoundException;
import com.spirnt.mission.discodeit.exception.User.InvalidPasswordException;
import com.spirnt.mission.discodeit.exception.User.UserAlreadyExistException;
import com.spirnt.mission.discodeit.exception.User.UserNotFoundException;
import com.spirnt.mission.discodeit.exception.UserStatus.UserStatusAlreadyExistException;
import com.spirnt.mission.discodeit.exception.UserStatus.UserStatusNotFoundException;
import java.time.Instant;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class DiscodeitExceptionHandler {

    @ExceptionHandler(DiscodeitException.class)
    public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                new ErrorResponse(Instant.now(), e.getErrorCode().getCode(),
                    e.getErrorCode().getMessage(),
                    e.getDetails(), e.getClass().getSimpleName(), 500));
    }

    // User

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(
                new ErrorResponse(Instant.now(), e.getErrorCode().getCode(),
                    e.getErrorCode().getMessage(),
                    e.getDetails(), e.getClass().getSimpleName(), 404));
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistException(
        UserAlreadyExistException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(
                new ErrorResponse(Instant.now(), e.getErrorCode().getCode(),
                    e.getErrorCode().getMessage(),
                    e.getDetails(), e.getClass().getSimpleName(), 400));
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPasswordException(
        InvalidPasswordException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(
                new ErrorResponse(Instant.now(), e.getErrorCode().getCode(),
                    e.getErrorCode().getMessage(),
                    e.getDetails(), e.getClass().getSimpleName(), 400));
    }

    // Channel
    @ExceptionHandler(ChannelNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleChannelNotFoundException(
        ChannelNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(
                new ErrorResponse(Instant.now(), e.getErrorCode().getCode(),
                    e.getErrorCode().getMessage(),
                    e.getDetails(), e.getClass().getSimpleName(), 404));
    }

    @ExceptionHandler(PrivateChannelUpdateException.class)
    public ResponseEntity<ErrorResponse> handlePrivateChannelUpdateException(
        PrivateChannelUpdateException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(
                new ErrorResponse(Instant.now(), e.getErrorCode().getCode(),
                    e.getErrorCode().getMessage(),
                    e.getDetails(), e.getClass().getSimpleName(), 400));
    }

    // Message
    @ExceptionHandler(MessageNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMessageNotFoundException(
        MessageNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(
                new ErrorResponse(Instant.now(), e.getErrorCode().getCode(),
                    e.getErrorCode().getMessage(),
                    e.getDetails(), e.getClass().getSimpleName(), 404));
    }

    // ReadStatus
    @ExceptionHandler(ReadStatusNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleReadStatusNotFoundException(
        ReadStatusNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(
                new ErrorResponse(Instant.now(), e.getErrorCode().getCode(),
                    e.getErrorCode().getMessage(),
                    e.getDetails(), e.getClass().getSimpleName(), 404));
    }

    @ExceptionHandler(ReadStatusAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleReadStatusAlreadyExistException(
        ReadStatusAlreadyExistException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(
                new ErrorResponse(Instant.now(), e.getErrorCode().getCode(),
                    e.getErrorCode().getMessage(),
                    e.getDetails(), e.getClass().getSimpleName(), 400));
    }

    // UserStatus
    @ExceptionHandler(UserStatusNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserStatusNotFoundException(
        UserStatusNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(
                new ErrorResponse(Instant.now(), e.getErrorCode().getCode(),
                    e.getErrorCode().getMessage(),
                    e.getDetails(), e.getClass().getSimpleName(), 404));
    }

    @ExceptionHandler(UserStatusAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleUserStatusAlreadyExistsException(
        UserStatusAlreadyExistException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(
                new ErrorResponse(Instant.now(), e.getErrorCode().getCode(),
                    e.getErrorCode().getMessage(),
                    e.getDetails(), e.getClass().getSimpleName(), 400));
    }

    // BinaryContent
    @ExceptionHandler(BinaryContentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBinaryContentNotFoundException(
        BinaryContentNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(
                new ErrorResponse(Instant.now(), e.getErrorCode().getCode(),
                    e.getErrorCode().getMessage(),
                    e.getDetails(), e.getClass().getSimpleName(), 404));
    }

    @ExceptionHandler(FileException.class)
    public ResponseEntity<ErrorResponse> handleFileException(
        FileException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                new ErrorResponse(Instant.now(), e.getErrorCode().getCode(),
                    e.getErrorCode().getMessage(),
                    e.getDetails(), e.getClass().getSimpleName(), 500));
    }
}
