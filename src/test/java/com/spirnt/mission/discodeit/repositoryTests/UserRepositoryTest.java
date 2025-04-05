package com.spirnt.mission.discodeit.repositoryTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.spirnt.mission.discodeit.entity.User;
import com.spirnt.mission.discodeit.repository.UserRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataJpaTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private EntityManager em;


  @Test
  @DisplayName("사용자 저장 테스트 - 성공")
  void testSaveUser() {
    // given
    User user = new User("name", "email", "password");

    // when
    User result = userRepository.save(user);

    // then
    assertNotNull(result);
    assertEquals(result.getId(), user.getId());
    assertEquals(result.getUsername(), user.getUsername());
    assertEquals(result.getEmail(), user.getEmail());
    assertEquals(result.getPassword(), user.getPassword());
  }

  @Test
  @DisplayName("사용자 저장 테스트 - 실패")
  void testSaveUserFail() {
    // given
    // not null이어야 하는 항목을 null로 저장 시도
    User user = new User(null, null, "password");

    // when & then
    assertThrows(ConstraintViolationException.class, () -> {
      userRepository.save(user);
      em.flush();
      em.clear();
    });
  }

  @Test
  @DisplayName("findAllFetchJoin 메서드 테스트 - 성공")
  void testFindAllFetchJoinSuccess() {
    // when
    List<User> users = userRepository.findAllFetchJoin();

    // then
    assertNotNull(users);
    assertEquals(2, users.size());

    for (User user : users) {
      assertNotNull(user.getProfile());
      assertNotNull(user.getStatus());
      assertNotNull(user.getUsername());
      assertNotNull(user.getEmail());
    }
  }

  // 실패는 어떤 경우여야 할까?

}
