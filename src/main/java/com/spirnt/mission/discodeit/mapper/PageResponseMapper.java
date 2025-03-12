package com.spirnt.mission.discodeit.mapper;

import com.spirnt.mission.discodeit.dto.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
public class PageResponseMapper<T> {

  public PageResponse<T> fromSlice(Slice<T> slice) {
    return new PageResponse(slice.getContent(), slice.getNumber(), slice.getSize(), slice.hasNext(),
        slice.getNumberOfElements());
  }

  public PageResponse<T> fromPage(Page<T> page) {
    return new PageResponse(page.getContent(), page.getNumber(), page.getSize(),
        page.hasNext(), page.getTotalElements());
  }
}
