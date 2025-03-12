package com.spirnt.mission.discodeit.repository;

import com.spirnt.mission.discodeit.enity.Message;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

  Message save(Message message);

  void delete(Message message);

  void deleteById(UUID messageId);

  Optional<Message> findById(UUID messageId);

  List<Message> findAllByChannelId(UUID channelId);

  List<Message> findAll();


  // 존재 검증
  boolean existsById(UUID messageId);

  @Transactional
  @Modifying
  @Query("UPDATE Message m SET m.content = :newContent WHERE m.id = :id")
  int updateById(@Param("id") UUID id, @Param("newContent") String newContent);
}
