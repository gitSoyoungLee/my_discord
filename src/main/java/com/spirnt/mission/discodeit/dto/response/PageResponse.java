package com.spirnt.mission.discodeit.dto.response;

import java.util.List;

public class PageResponse<T> {

  private List<T> content;  // 실제 데이터
  private int number; // 페이지 번호
  private int size; // 페이지 크기
  private boolean hasNext;

  private Long totalElements; // T 데이터의 총 갯수

}
