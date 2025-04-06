-- 1. binary_contents에 프로필 이미지 데이터 삽입
INSERT INTO binary_contents (id, created_at, file_name, size, content_type) VALUES ('00000000-0000-0000-0000-000000000001', CURRENT_TIMESTAMP, 'profile1.png', 2048, 'image/png'),  ('00000000-0000-0000-0000-000000000002', CURRENT_TIMESTAMP, 'profile2.jpg', 1024, 'image/jpeg');

-- 2. users에 사용자 데이터 삽입 (profile_id를 binary_contents의 id와 연결)
INSERT INTO users (id, created_at, updated_at, username, email, password, profile_id) VALUES ('00000000-0000-0000-0000-000000000101', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,'alice', 'alice@example.com', 'password123',   '00000000-0000-0000-0000-000000000001'),('00000000-0000-0000-0000-000000000102', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,   'bob', 'bob@example.com', 'securepass456', '00000000-0000-0000-0000-000000000002');

-- 3. user_statuses에 사용자 상태 데이터 삽입 (user_id는 users의 id)
INSERT INTO user_statuses (id, created_at, updated_at, user_id, last_active_at) VALUES ('00000000-0000-0000-0000-000000001001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,'00000000-0000-0000-0000-000000000101', CURRENT_TIMESTAMP),  ('00000000-0000-0000-0000-000000001002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '00000000-0000-0000-0000-000000000102', CURRENT_TIMESTAMP);

