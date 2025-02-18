package com.spirnt.mission.discodeit.dto.message;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MessageUpdateRequest  {
    private String content;

    public MessageUpdateRequest(String content) {
        this.content = content;
    }
}